/* global tus */
/* eslint-disable no-console, no-alert */

// 'use strict'
(async () => {

console.log('demo.js is loaded')

if (env !== null) {
  console.log('environment variables loaded')
} else {
  console.error('Error: environment variables not found')
} // .if 

authUrl = `${env.DEX_URL}/oauth`

const params = new URLSearchParams({
  'username': env.USER_NAME,
  'password': env.USER_PASSWORD,
});

// console.log('sending params: ', params.toString())
console.log('sending auth request..')

const response = await fetch(authUrl, {
    method: "POST",
    body: params,
})


if (response.ok) {
  console.log('Success response: ', response)
} else {
  console.error('Error response: ', response)
} // .response.ok

const loginResponse = await response.json()

// console.log('loginResponse: ', loginResponse)
console.log('login response received..')


let upload          = null
let uploadIsRunning = false
const toggleBtn       = document.querySelector('#toggle-btn')
const input           = document.querySelector('input[type=file]')
const progress        = document.querySelector('.progress')
const progressBar     = progress.querySelector('.bar')
const alertBox        = document.querySelector('#support-alert')
const uploadList      = document.querySelector('#upload-list')
const chunkInput      = document.querySelector('#chunksize')
const parallelInput   = document.querySelector('#paralleluploads')
const endpointInput   = document.querySelector('#endpoint')

function reset () {
  input.value = ''
  toggleBtn.textContent = 'start upload'
  upload = null
  uploadIsRunning = false
}

function askToResumeUpload (previousUploads, currentUpload) {
  if (previousUploads.length === 0) return

  let text = 'You tried to upload this file previously at these times:\n\n'
  previousUploads.forEach((previousUpload, index) => {
    text += `[${index}] ${previousUpload.creationTime}\n`
  })
  text += '\nEnter the corresponding number to resume an upload or press Cancel to start a new upload'

  const answer = prompt(text)
  const index = parseInt(answer, 10)

  if (!Number.isNaN(index) && previousUploads[index]) {
    currentUpload.resumeFromPreviousUpload(previousUploads[index])
  }
}

function startUpload () {
  const file = input.files[0]
  // Only continue if a file has actually been selected.
  // IE will trigger a change event even if we reset the input element
  // using reset() and we do not want to blow up later.
  if (!file) {
    return
  }

  // const endpoint = endpointInput.value
  const endpoint = `${env.DEX_URL}/upload`
  console.log('starting upload to endpoint: ', endpoint) 
  
  let chunkSize = parseInt(chunkInput.value, 10)
  if (Number.isNaN(chunkSize)) {
    chunkSize = Infinity
  }

  let parallelUploads = parseInt(parallelInput.value, 10)
  if (Number.isNaN(parallelUploads)) {
    parallelUploads = 1
  }

  toggleBtn.textContent = 'pause upload'

  // 
  const uuidv4ID = crypto.randomUUID()

  const metadata =   {
    filename: "10MB-test-file",
    filetype: "text/plain",
    meta_destination_id: "ndlp",
    meta_ext_event: "routineImmunization",
    meta_ext_source: "IZGW",
    meta_ext_sourceversion: "V2022-12-31",
    meta_ext_entity: "DD2",
    meta_username: "ygj6@cdc.gov",
    meta_ext_objectkey: uuidv4ID,
    meta_ext_filename: "10MB-test-file",
    meta_ext_submissionperiod: '1',
  }


  const authToken = `Bearer ${loginResponse.access_token}`
  // console.log('authToken: ', authToken)
  
  const options = {
    endpoint,
    headers: {
      Authorization: authToken,
    },
    chunkSize,
    retryDelays: [0, 1000, 3000, 5000],
    parallelUploads,
    metadata: metadata,
    // metadata   : {
    //   filename: file.name,
    //   filetype: file.type,
    // },
    onError (error) {
      if (error.originalRequest) {
        if (window.confirm(`Failed because: ${error}\nDo you want to retry?`)) {
          upload.start()
          uploadIsRunning = true
          return
        }
      } else {
        window.alert(`Failed because: ${error}`)
      }

      reset()
    },
    onProgress (bytesUploaded, bytesTotal) {
      const percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2)
      progressBar.style.width = `${percentage}%`
      console.log(bytesUploaded, bytesTotal, `${percentage}%`)
    },
    onSuccess () {
      const anchor = document.createElement('a')
      anchor.textContent = `Download ${upload.file.name} (${upload.file.size} bytes)`
      anchor.href = upload.url
      anchor.className = 'btn btn-success'
      uploadList.appendChild(anchor)

      reset()
    },
  }

  upload = new tus.Upload(file, options)
  upload.findPreviousUploads().then((previousUploads) => {
    askToResumeUpload(previousUploads, upload)

    upload.start()
    uploadIsRunning = true
  })
}

if (!tus.isSupported) {
  alertBox.classList.remove('hidden')
}

if (!toggleBtn) {
  throw new Error('Toggle button not found on this page. Aborting upload-demo. ')
}

toggleBtn.addEventListener('click', (e) => {
  e.preventDefault()

  if (upload) {
    if (uploadIsRunning) {
      upload.abort()
      toggleBtn.textContent = 'resume upload'
      uploadIsRunning = false
    } else {
      upload.start()
      toggleBtn.textContent = 'pause upload'
      uploadIsRunning = true
    }
  } else if (input.files.length > 0) {
    startUpload()
  } else {
    input.click()
  }
})

input.addEventListener('change', startUpload)


})()
