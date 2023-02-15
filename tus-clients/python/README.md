## Overview

This project acts as a python client to send files to CDC's Data Exchange Upload API.

## Prerequisites

- [Python 3.6+](https://www.python.org/downloads/)
- [CDC SAMS staging credentials](https://sams-stg.cdc.gov)

## Setting Up Credentials

This tool must have the following environment variables or create a .env file with the following information.

```bash
DEX_ACCESS_TOKEN=<bearer_token> \
DEX_TUS_UPLOAD_URL=<upload_api_endpoint> \
```

## Running the code

- Setup your environment

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python tuspy-uploader-test-ndlp-ri-good.py
```

- Upgrading old dependencies

```bash
pip install --upgrade --force-reinstall -r requirements.txt
```

- Freezing dependencies

```bash
pip freeze > requirements.txt
```
