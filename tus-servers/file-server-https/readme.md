## Purpose


The purpose of this server is to be able to run the tus-client/broswer locally on https, and test APIM integration.
This is a DEV only test for integration.

## Prerequisites 

### Create Cert for Local Dev 

Create a folder __cert__, at the root of file-server-https.
This folder, cert, was added in .gitignore.

Create a key and a certificate pair, run at file-server-https folder level:

`
$ openssl req -x509 -newkey rsa:4096 -sha256 -days 3650 -nodes \
  -keyout cert/localhost.key -out cert/localhost.crt -subj "//CN=localhost_dev" \
  -addext "subjectAltName=DNS:localhost,IP:127.0.0.1" 
`

Ref: https://stackoverflow.com/questions/10175812/how-to-generate-a-self-signed-ssl-certificate-using-openssl 


### Import Cert 

(for windows): windows > certmgr > Trusted Root Certificate Authority > Certificates > All Tasks > Import 


### Server is Go (Golang) 

install go locally: [https://go.dev/doc/install](https://go.dev/doc/install)


## Run HTTPS Localhost Server

`
$ go main.go
`

open in browser https://localhost:8080 

Note: change port in main.go as needed per APIM

