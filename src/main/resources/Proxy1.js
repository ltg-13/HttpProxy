function beforeRequest(request) {
    print("method: "+ request.getMethod());
    logRequest(request);
    wait(1000);
}

function afterRequest(request, response) {
    print('nachher');
}