function beforeRequest(request) {
    // removeHeaderFrom("Accept-Encoding", request);
    logRequest(request, 1250);
    sendRequest(request);
}

function afterRequest(request, response) {
    logRequest(request, 1250);
    logResponse(response, -1);
    sendResponse(response);
}
