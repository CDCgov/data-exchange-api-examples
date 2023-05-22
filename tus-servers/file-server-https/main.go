package main

import (

	"fmt"
	"log"
	"net/http"
)

func main() {
	
	// HTTPS server address and port:
	addr := "localhost:8080"

	// Serves the browser client:
	dir := "../../tus-clients/browser"

	// Create a file server handler for the specified directory
	fileServer := http.FileServer(http.Dir(dir))

	http.Handle("/", fileServer)

	// See readme.md, should have key, cert pair in cert folder
	// .crt should to be added to local root CA for browser to recognize

	// Load self-signed certificate and private key
	certFile := "cert/localhost.crt"
	keyFile := "cert/localhost.key"


	// Start the HTTPS server
	fmt.Printf("Server running at https://%s\n", addr)
	log.Fatal(http.ListenAndServeTLS(addr, certFile, keyFile, nil))

} // .main


