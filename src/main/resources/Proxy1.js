function beforeRequest(request) {
    
//    debug(request.getMethod());
//    if (request.getMethod() == "GET") {
//        debug("waiting");
//        wait(10000);
//    }
    sendRequest(request);
}

function afterRequest(request, response) {
    logRequest(request);
    logResponse(response);
    sendResponse(response);
}