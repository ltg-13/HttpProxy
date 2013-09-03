function beforeRequest(request) {
    removeHeaderFrom("Accept-Encoding", request);
    logRequest(request);
    sendRequest(request);
}

function afterRequest(request, response) {
    logRequest(request);
    logResponse(response);
    sendResponse(response);
}
