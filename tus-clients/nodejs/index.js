
'use strict'

require('dotenv').config({ path: '../../.env' });
const fs = require('fs');
const tus = require('tus-js-client');

const path = `${__dirname}/../../upload-files/10MB-test-file`;
const file = fs.createReadStream(path);

const options = {
  endpoint: `${process.env.DEX_URL}/upload`,
  headers : {
    Authorization: `Bearer ${process.env.AUTH_TOKEN}`,
  },
  metadata: {
    filename           : '10MB-test-file',
    filetype           : 'text/plain',
    meta_destination_id: 'ndlp',
    meta_ext_event     : 'ri',
  },
  onError (error) {
    console.error('An error occurred:')
    console.error(error)
    process.exitCode = 1
  },
  onProgress (bytesUploaded, bytesTotal) {
    const percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2)
    console.log(bytesUploaded, bytesTotal, `${percentage}%`)
  },
  onSuccess () {
    console.log('Upload finished:', upload.url)
  },
}

const upload = new tus.Upload(file, options)
upload.start()
