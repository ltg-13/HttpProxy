/**
 * This function waits the given number of milliseconds before contiuing.
 * 
 * @param {Number} milliseconds
 */
function wait(milliseconds) {
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(milliseconds);
}

function logRequest(request) {
    //logger.info("request: \n{}", requestWriter.writeToString(request, "      >"));
    providedFunctions.logInfo(request);
}

function sendRequest(request) {
    
}