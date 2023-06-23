## Browser Auth with SAMS, and Upload to API 

### Prerequisites

- add a file env.js at root, see example: env_example.js 

### Use DEV Only:

- browser may need to be in disabled cors mode such as windows run:
  ` chrome.exe --user-data-dir="C://Chrome_dev_session" --disable-web-security `

- example server:
  `python -m http.server {PORT}`

- open browser: http://localhost:{PORT}/

- the authentication is performed in the background by using the env.js values

- optional see browser console output for status preliminary to upload

- chose file to upload such as file in /upload-files at root of repo

### Reference:
tus-js-client
[https://github.com/tus/tus-js-client](https://github.com/tus/tus-js-client)
