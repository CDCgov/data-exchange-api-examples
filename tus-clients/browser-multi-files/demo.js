(async () => {

  console.log('demo.js is loaded')

  if (env !== null) {
    console.log('environment variables loaded')
  } else {
    console.error('error: environment variables not found, check env.js file is present and loaded with values')
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
    console.log('success auth response: ', response)
  } else {
    console.error('error auth response: ', response)
  } // .response.ok

  const loginResponse = await response.json()

  // console.log('loginResponse: ', loginResponse)
  // console.log('login response received..')


  // let upload          = null
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

  function reset (startTimeUpload, fileListBytesUploaded, fileListBytesTotal) {

    // ----------------------------------------------------
    if ( fileListBytesUploaded >= fileListBytesTotal) {

      console.log("files uploaded ok!")
      const durationUpload = (new Date().getTime()) - startTimeUpload
  
      console.log(`total upload duration [ms]: ${durationUpload}, [s]: ${durationUpload / 1000 }`)  

      input.value = ''
      toggleBtn.textContent = 'start upload'
      // upload = null
      uploadIsRunning = false

    } // .if

  } // .reset

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
  } // .askToResumeUpload


  function startUpload () {
    const fileList = Array.from(input.files) //[0]
    // Only continue if a file has actually been selected.
    // IE will trigger a change event even if we reset the input element
    // using reset() and we do not want to blow up later.

    // console.log('fileList', fileList)
    if (!fileList ) { return }

    const startTimeUpload = new Date().getTime()
    console.log('starting time: ', startTimeUpload) 

    //
    // uploads set-up
    // ----------------------------------------------------
    // const endpoint = endpointInput.value
    const endpoint = `${env.DEX_URL}/upload`
    console.log('starting upload to endpoint: ', endpoint) 

    let chunkSize = parseInt(chunkInput.value, 10)
    if (Number.isNaN(chunkSize)) {
      chunkSize = Infinity
    } // .if

    let parallelUploads = parseInt(parallelInput.value, 10)
    if (Number.isNaN(parallelUploads)) {
      // currently this is disabled as only parallelUploads = 1 works
      // multiple uploads can be started concurrent ( new tus.Upload ), however each one sends chuncks serially to the server
      // tusd azure does not support chuncks concatenation, ref: https://github.com/tus/tusd/issues/843 
      parallelUploads = 1
    } // .if

    toggleBtn.textContent = 'pause upload'

    const authToken = `Bearer ${loginResponse.access_token}`
    // console.log('authToken: ', authToken) 

    const fileListBytesTotal = fileList.reduce( 
      (acc, curr ) => acc + curr.size,
      0
    ) // .fileListBytesTotal
    console.log('fileListBytesTotal: ', fileListBytesTotal)

    //
    // uploading files
    // ----------------------------------------------------
    let fileListBytesUploaded = 0

    fileList.forEach( (file, index) => {

      console.log(`start uploading file: ${file.name}, file index: ${index}`)

      const metadata = {
        // REQUIRED
        meta_destination_id: "dextesting", // final container destination: dextesting-testevent1,
        meta_ext_event: "testevent1",
  
        // REQUIRED: original file name, see metadata for destination file repo
        filename: file.name, 
  
        // CUSTOM (PER USE_CASE) see metadata for destination file in repo
        meta_file_timestamp: file.lastModified,
        meta_ext_objectkey: crypto.randomUUID(), 
        filetype: "text/plain",
        meta_ext_source: "for_the_demo",
        meta_username: "example@cdc.gov",
      } // .metadata

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

          reset(startTimeUpload, fileListBytesUploaded, fileListBytesTotal)
        },
        onProgress (fileBytesUploaded, fileBytesTotal) {

          // onProgress is called 2x -> fileBytesUploaded / 2
          fileListBytesUploaded = fileListBytesUploaded + fileBytesUploaded / 2

  
          const percentageFile = ((fileBytesUploaded / fileBytesTotal) * 100).toFixed(2)
          const percentageTotal = ((fileListBytesUploaded / fileListBytesTotal) * 100).toFixed(2)
  
  
          progressBar.style.width = `${percentageTotal}%`
  
          console.log('file:', file.name, fileBytesUploaded, fileBytesTotal, `${percentageFile}%`)
          console.log('fileList (total):', fileListBytesUploaded, fileListBytesTotal, `${percentageTotal}%`)
  
        },
        onSuccess () {
  
          console.log(`file: ${file.name} uploaded`)
  
          // console.log(`upload tus status url (needs bearer token): ${env.DEX_URL}/upload/status/${upload.url}`)
          // console.log(`upload status url (supplemental api, needs bearer token): ${env.DEX_URL}/status/${upload.url}`)
  
          const anchor = document.createElement('a')
          anchor.textContent = `Download ${upload.file.name} (${upload.file.size} bytes)`
          anchor.href = upload.url
          anchor.className = 'btn btn-success'
          uploadList.appendChild(anchor)

          reset(startTimeUpload, fileListBytesUploaded, fileListBytesTotal)
        },
      } // .options
      
      let upload = new tus.Upload(file, options)
      upload.findPreviousUploads().then((previousUploads) => {
        askToResumeUpload(previousUploads, upload)
  
        upload.start()
        uploadIsRunning = true
      })


    }) // .fileList.forEach

  } // .startUpload

  if (!tus.isSupported) {
    alertBox.classList.remove('hidden')
  } // .if

  if (!toggleBtn) {
    throw new Error('Toggle button not found on this page. Aborting upload-demo. ')
  } // .if

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
  }) // .toggleBtn

  input.addEventListener('change', startUpload)

})() // async
