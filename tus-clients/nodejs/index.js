const fs = require("fs");
const tus = require("tus-js-client");
const { v4: uuidv4 } = require("uuid");

const config = require("./config");
const client = require("./client");

async function start() {
  const [username, password, url] = config.validateEnv();
  const loginResponse = await client.login(username, password, url);
  if (loginResponse === null) {
    console.error("Login failed. Exiting program.");
    process.exit(1);
  }

  const path = `${__dirname}/../../upload-files/upload test file here`;
  const file = fs.createReadStream(path);

  const options = {
    endpoint: `${url}/upload`,
    headers: {
      Authorization: `Bearer ${loginResponse.access_token}`,
    },
    metadata: {
      // DEX Metadata Fields - version 2 sender manifest
      version: "2.0",
      data_stream_id: "data stream id value here",
      data_stream_route: "data stream route value here",
      sender_id: "sender id value here",
      data_producer_id: "data producer id value here",
      jurisdiction: "jurisdiction value here",
      received_filename: "uploaded file name here"

      // Custom Metadata Fields - varies by data stream; optional
      //custom_field_1: "allowed custom field value",
      //custom_field_2: "allowed custom field value"
    },
    onError(error) {
      console.error("An error occurred:");
      console.error(error);
      process.exitCode = 1;
    },
    onProgress(bytesUploaded, bytesTotal) {
      const percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2);
      console.log(bytesUploaded, bytesTotal, `${percentage}%`);
    },
    onSuccess() {
      console.log("Upload finished:", upload.url);
    },
  };

  const upload = new tus.Upload(file, options);
  upload.start();
}

start();
