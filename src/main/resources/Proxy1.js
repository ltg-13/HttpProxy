function beforeRequest(request) {
    logRequest(request);
    sendRequest(request);
}

function afterRequest(request, response) {
    sendResponse(response);
}