function beforeRequest(request) {
    removeHeaderFrom("Accept-Encoding", request);
    logRequest(request, 250);
    sendRequest(request);
}

function afterRequest(request, response) {
    logRequest(request, 250);
    logResponse(response, -1);
    sendResponse(response);
}
