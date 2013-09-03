/**
 * This function waits the given number of milliseconds before contiuing.
 * 
 * @param {Number} milliseconds
 */
function wait(milliseconds) {
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(milliseconds);
}

/**
 * 
 * @param {object} request
 */
function logRequest(request) {
    providedFunctions.logRequest(request);
}

/**
 * 
 * @param {object} response
 */
function logResponse(response) {
    providedFunctions.logResponse(response);
}

/**
 * Logs a debug message to the script output.
 * 
 * @param {string} message
 */
function debug(message) {
    providedFunctions.debug(message);
}

function sendRequest(request) {
    providedFunctions.sendRequest(request);
}

function sendResponse(response) {
    providedFunctions.sendResponse(response);
}

/**
 * Removes a header from from the given request. If the request has no such 
 * header, nothing happens.
 * 
 * @param {string} header name of the header to remove
 * @param {object} request request to remove the header from
 */
function removeHeaderFrom(header, request) {
    providedFunctions.removeHeaderFrom(header, request);
}