### Create Cert for Local Dev

openssl req -x509 -newkey rsa:4096 -sha256 -days 3650 -nodes \
  -keyout localhost.key -out localhost.crt -subj "//CN=localhost_dev" \
  -addext "subjectAltName=DNS:localhost,IP:127.0.0.1" 

ref: https://stackoverflow.com/questions/10175812/how-to-generate-a-self-signed-ssl-certificate-using-openssl 


### Import Cert 

windows > certmgr > Trusted Root Certificate Authority > Certificates > All Tasks > Import 


### Run HTTPS

`
$ go main.go
`

open in browser https://localhost:8080 

Note: change port in main.go as needed 

