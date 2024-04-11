# Azure Data Factory (ADF) - Processing Status API Integration

Azure Data Factory (ADF) is a powerful cloud-based data integration service that enables you to orchestrate data workflows efficiently. In this document, we'll explore how you can integrate Azure Data Factory with the Processing Status API to invoke various endpoints for tracing and reporting purposes.

# Data Factory Details:  
ADF Name: ededev-pstatus-adf-dev

Subscription: OCIO-EDEDEV-C1

Pipelines:
1. pstatus-api-dev-Tracing
2. pstatus-api-dev-Reporting


## Tracing Workflow

The Processing Status API offers endpoints for tracing activities. Here's an example workflow using Azure Data Factory:

1. **Health Check**: Verify the health of the processing-status API.
2. **Create Trace**: Trigger the `/createTrace` endpoint to generate a new trace.
3. **Fetch Trace Details**: Use the `/GetTrace` endpoint with the generated `traceId` to retrieve trace details.

This workflow ensures that you can monitor and trace activities effectively within Azure Data Factory using the Processing Status API.

## Reporting Workflow

Integrate Azure Data Factory with the Processing Status API to facilitate reporting tasks:

1. **Health Check**: Ensure the processing-status API is operational.
2. **Create Report**: Generate a report based on a specific `uploadId`. Call the `/report/json/uploadId/{uploadId}` endpoint to initiate report creation.
3. **Retrieve Report**: Use the `/report/uploadId/{uploadId}` endpoint to fetch detailed report information corresponding to the provided `uploadId`.

By leveraging Azure Data Factory alongside the Processing Status API, you can streamline reporting processes and obtain valuable insights from your data workflows.

## Conclusion

Azure Data Factory offers seamless integration capabilities with external APIs like the Processing Status API. By incorporating these workflows into your data pipelines, you can enhance monitoring, tracing, and reporting functionalities within your data integration solutions.
