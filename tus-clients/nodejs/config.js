const dotenv = require("dotenv");
dotenv.config({ path: "../../.env" });

/**
 * This ensures the credentials are set or fail out of the process.
 * We want to fail quickly whenever running a script that needs them
 * because the scripts will end up erroring out anyway.
 */
const config = {};

config.validateEnv = () => {
  const username = process.env.ACCOUNT_USERNAME;
  const password = process.env.ACCOUNT_PASSWORD;
  const url = process.env.DEX_URL;

  let error = "No ";
  let hasErrors = false;

  if (username == null || username === "") {
    hasErrors = true;
    error += "username";
  }

  if (password == null || password === "") {
    if (hasErrors) {
      error += ", password";
    } else {
      error += "password";
    }
    hasErrors = true;
  }

  if (url == null || url === "") {
    if (hasErrors) {
      error += ", data exchange url";
    } else {
      error += "data exchange url";
    }
    hasErrors = true;
  }

  if (hasErrors) {
    error += " has been set in environment.";
    console.error(`Terminating environment: ${error}`);
    process.exit(1);
  }

  return [username, password, url];
};

module.exports = config;
