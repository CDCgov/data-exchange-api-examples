from tusclient import client
from tusclient.exceptions import TusCommunicationError
import uuid

access_token = '{insert your access token here}'
tus_upload_url = 'https://apidev.cdc.gov'
my_client = client.TusClient(
    url=tus_upload_url,
    headers={'Authorization':'Bearer ' + access_token}
)

# create the uploader
uploader = my_client.uploader('10MB-test-file',
    metadata={
        'filename':'10MB-test-file',
        'meta_destination_id':'ndlp',
        'meta_ext_event':'ri',
        'meta_ext_source':'IZGW',
        'meta_ext_sourceversion':'V2022-12-31',
        'meta_ext_entity':'DD2',
        'meta_username':'ygj6@cdc.gov',
        'meta_ext_objectkey':str(uuid.uuid4()),
        'meta_ext_filename':'10MB-test-file'
        }
    )

# upload the entire file
try:
    uploader.upload()
except TusCommunicationError as error:
    print('TusCommunicationError: ' + str(error.response_content.decode('UTF-8')))
