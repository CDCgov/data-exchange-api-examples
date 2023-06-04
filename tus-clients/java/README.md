## Overview

This project acts as a java client to send files to CDC's Data Exchange Upload API.

You’ll generate a Java application that follows Gradle’s conventions.

## Prerequisites

- A text editor or IDE
- A Java Development Kit (JDK) >= Version 8
- The latest [Grade distribution](https://gradle.org/install/)

## Setting Up Credentials

This tool must have the following environment variables or create a .env file with the following information.

```bash
ACCOUNT_USERNAME=<sams_sys_account_name> \
ACCOUNT_PASSWORD=<sams_sys_account_pswd> \
DEX_URL=<dex_staging_url> \
```

## Running the code

- Run the init task

```bash
gradle init
```

- Run the application

```bash
./gradlew run
```

- Bundle the application

```bash
./gradlew build
```