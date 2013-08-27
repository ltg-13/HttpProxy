function beforeRequest(request) {
    sendRequest(request);
}

function afterRequest(request, response) {
    sendResponse(response);
}