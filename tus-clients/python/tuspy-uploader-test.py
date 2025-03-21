import os
import uuid
from dotenv import load_dotenv
import requests

from tusclient import client
from tusclient.exceptions import TusCommunicationError

###################################################################
# Functions used
###################################################################

def load_environment():
    """
    Load needed environment global variables and check not empty
    """
    load_dotenv()

    ACCOUNT_USERNAME = os.getenv('ACCOUNT_USERNAME')
    ACCOUNT_PASSWORD = os.getenv('ACCOUNT_PASSWORD')
    TUS_UPLOAD_URL = os.getenv('DEX_URL')

    if ACCOUNT_USERNAME == "":
        print("ACCOUNT_USERNAME not found")
        quit()
    if ACCOUNT_PASSWORD == "":
        print("ACCOUNT_PASSWORD not found")
        quit()
    if TUS_UPLOAD_URL == "":
        print("TUS_UPLOAD_URL not found")
        quit()
    return ACCOUNT_USERNAME, ACCOUNT_PASSWORD, TUS_UPLOAD_URL


def get_access_token(url_oauth, user_name, user_pswd):
    """
    Requests and returns an oauth access_token
    """

    # Getting ACCESS_TOKEN from oauth:
    credsToSend = {'username':user_name, 'password': user_pswd}
    
    res = requests.post(url_oauth, data=credsToSend)
    print("oauth response: ", res.status_code, res.reason)
    if res.status_code != 200:
        print("oauth error, check user name and user password")
        quit()

    return res.json()['access_token']


###################################################################
# Main
###################################################################
print("started on oauth and upload...")
# Load needed environment global variables and check not empty 
ACCOUNT_USERNAME, ACCOUNT_PASSWORD, TUS_UPLOAD_URL = load_environment()

# Getting ACCESS_TOKEN from oauth
url_oauth = f"{TUS_UPLOAD_URL}/oauth"
ACCESS_TOKEN = get_access_token(url_oauth, ACCOUNT_USERNAME, ACCOUNT_PASSWORD)

tus_upload_url = f"{TUS_UPLOAD_URL}/upload"
# Upload a local file and metadata to the api
try:

    my_client = client.TusClient(
        url=tus_upload_url,
        headers={'Authorization':'Bearer ' + ACCESS_TOKEN}
    )

    filename = 'test upload file name here'
    # create the uploader
    uploader = my_client.uploader('../../upload-files/{0}'.format(filename),
        metadata={
            # DEX Metadata Fields - version 2 sender manifest
            'version':'2.0',
            'data_stream_id':'data stream id value here',
            'data_stream_route':'data stream route value here',
            'sender_id':'sender id value here',
            'data_producer_id':'data producer id value here',
            'jurisdiction':'jurisdiction value here',
            'received_filename':'uploaded file name here'

            # Custom Metadata Fields - varies by data stream; optional
            #'custom_field_1':'allowed custom field value',
            #'custom_field_2':'allowed custom field value'

            }
        )

    # upload the entire file
    try:
        print('starting upload at url: ', tus_upload_url)
        uploader.upload()
        print('file uploaded success')
    except TusCommunicationError as error:
        print('TusCommunicationError: ' + str(error.response_content.decode('UTF-8')))

except Exception as e:
    print("exception: ", e)

