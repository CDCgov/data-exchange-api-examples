package main

import (
	// "crypto/rand"
	// "crypto/rsa"
	// "crypto/x509"
	// "crypto/x509/pkix"
	// "encoding/pem"
	"fmt"
	"log"
	// "math/big"
	"net/http"
	// "os"
	// "time"
)

func main() {

	// http.HandleFunc("/", rootHandler)

	dir := "../../tus-clients/browser"
	// Create a file server handler for the specified directory
	fileServer := http.FileServer(http.Dir(dir))

	http.Handle("/", fileServer)


	// Load self-signed certificate and private key
	// .crt should to be added to local root CA for browser to recognize
	certFile := "cert/localhost.crt"
	keyFile := "cert/localhost.key"

	// Generate a self-signed certificate and private key
	// err := generateCertificate(certFile, keyFile)
	// if err != nil {
	// 	log.Fatal(err)
	// }

	// Start the HTTPS server
	addr := "localhost:8080"
	fmt.Printf("Server running at https://%s\n", addr)
	log.Fatal(http.ListenAndServeTLS(addr, certFile, keyFile, nil))
} // .main

// func rootHandler(w http.ResponseWriter, r *http.Request) {
// 	fmt.Fprintln(w, "Running file server at /upload")
// } // .rootHandler

// not used, cert generated with open ssl, see readme.md


// func generateCertificate(certFile, keyFile string) error {
// 	// Replace this with your own certificate generation logic
// 	fmt.Println("Generating self-signed certificate...")

// 	// In a real production environment, use a proper certificate authority (CA) to obtain a valid certificate
// 	// For testing purposes only, generating a self-signed certificate using Go's crypto/tls package
// 	// Note: This is a minimal example and not recommended for production use

// 	// Generate a new private key
// 	privateKey, err := rsa.GenerateKey(rand.Reader, 2048)
// 	if err != nil {
// 		return err
// 	}

// 	// Create a self-signed X.509 certificate
// 	template := x509.Certificate{
// 		SerialNumber:          big.NewInt(1),
// 		Subject:               pkix.Name{CommonName: "localhost"},
// 		NotBefore:             time.Now(),
// 		NotAfter:              time.Now().AddDate(1, 0, 0), // Valid for 1 year
// 		KeyUsage:              x509.KeyUsageKeyEncipherment | x509.KeyUsageDigitalSignature,
// 		ExtKeyUsage:           []x509.ExtKeyUsage{x509.ExtKeyUsageServerAuth},
// 		BasicConstraintsValid: true,
// 	}
// 	derBytes, err := x509.CreateCertificate(rand.Reader, &template, &template, &privateKey.PublicKey, privateKey)
// 	if err != nil {
// 		return err
// 	}

// 	// Save the certificate and private key to disk
// 	certOut, err := os.Create(certFile)
// 	if err != nil {
// 		return err
// 	}
// 	defer certOut.Close()
// 	if err := pem.Encode(certOut, &pem.Block{Type: "CERTIFICATE", Bytes: derBytes}); err != nil {
// 		return err
// 	}

// 	keyOut, err := os.OpenFile(keyFile, os.O_WRONLY|os.O_CREATE|os.O_TRUNC, 0600)
// 	if err != nil {
// 		return err
// 	}
// 	defer keyOut.Close()
// 	privBytes, err := x509.MarshalPKCS8PrivateKey(privateKey)
// 	if err != nil {
// 		return err
// 	}
// 	if err := pem.Encode(keyOut, &pem.Block{Type: "PRIVATE KEY", Bytes: privBytes}); err != nil {
// 		return err
// 	}

// 	fmt.Println("Self-signed certificate generated successfully!")
// 	return nil
// } // .generateCertificate
