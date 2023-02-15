import os
import uuid
from dotenv import load_dotenv

from tusclient import client
from tusclient.exceptions import TusCommunicationError

load_dotenv()

ACCESS_TOKEN = os.getenv('DEX_ACCESS_TOKEN')
TUS_UPLOAD_URL = os.getenv('DEX_TUS_UPLOAD_URL')

my_client = client.TusClient(
    url=TUS_UPLOAD_URL,
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
    uploader.upload()
except TusCommunicationError as error:
    print('TusCommunicationError: ' + str(error.response_content.decode('UTF-8')))
