import os
import uuid
from dotenv import load_dotenv
import requests

from tusclient import client
from tusclient.exceptions import TusCommunicationError

load_dotenv()

ACCOUNT_USERNAME = os.getenv('ACCOUNT_USERNAME')
ACCOUNT_PASSWORD = os.getenv('ACCOUNT_PASSWORD')
TUS_UPLOAD_URL = os.getenv('DEX_URL')

if ACCOUNT_USERNAME == "" or ACCOUNT_PASSWORD == "" or TUS_UPLOAD_URL == "":
    print("missing environment variables")
    quit()
else:

    # Getting ACCESS_TOKEN from oauth:
    credsToSend = {'username':ACCOUNT_USERNAME, 'password': ACCOUNT_PASSWORD}
    # print("credsToSend: ", credsToSend)

    try:

        res = requests.post(f"{TUS_UPLOAD_URL}/oauth", data=credsToSend)
        print("oauth response: ", res.status_code, res.reason)

        credsServer = res.json()

        ACCESS_TOKEN = credsServer['access_token']

        tus_upload_url = f"{TUS_UPLOAD_URL}/upload"

        my_client = client.TusClient(
            url=tus_upload_url,
            headers={'Authorization':'Bearer ' + ACCESS_TOKEN}
        )

        filename = '10MB-test-file'
        # create the uploader
        uploader = my_client.uploader('../../upload-files/{0}'.format(filename),
            metadata={
                'filename':filename,
                'meta_destination_id':'ndlp',
                'meta_ext_event':'routineImmunization',
                'meta_ext_source':'IZGW',
                'meta_ext_sourceversion':'V2022-12-31',
                'meta_ext_entity':'DD2',
                'meta_username':'ygj6@cdc.gov',
                'meta_ext_objectkey':str(uuid.uuid4()),
                'meta_ext_filename':filename
                }
            )

        # upload the entire file
        try:
            print('startig upload at url: ', tus_upload_url)
            uploader.upload()
            print('file uploaded success')
        except TusCommunicationError as error:
            print('TusCommunicationError: ' + str(error.response_content.decode('UTF-8')))

    except Exception as e:
        print("exception: ", e)

