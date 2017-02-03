/* 
 * Functions required to stub the adapter
 * ruicouto in 2/fev/2017
 */

/** List of available methods */
var methods;

/**
 * Get the list of methods available in the server
 * and initialize methods variable
 */
function setMethods() {
    var m = loadDoc("method=getMethods");
    methods = m.split(",");
    console.log(m);
}

/**
 * Get a page synchronously. 
 * In order to stub the adapter variable, requests 
 * must be synchronous.
 * @param String queryString Request.
 * @returns String The returned data.
 */
function loadDoc(queryString) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "http://localhost:8080/FXStub/Stub?" + queryString, false); // false for synchronous request
    xmlHttp.send(null);
    return xmlHttp.responseText;
}

/**
 * Initialize the adapter variable.
 */
var initializeAdapter = function () {
    setMethods();
    adapter = Object();
    for (var i = 0; i < methods.length; i++) {
        var n = methods[i];
        adapter[n] = genMethod(n);
    }
}

/**
 * Generate a function which generates a querystring for the 
 * corresponding method.
 * @param String name Name of the method to create.
 * @returns Function A function which returns the appropriate queryString
 */
var genMethod = function (name) {
    return function () {
        console.log("Creating method " + name);
        console.log("With params " + arguments.length);
        var request = "method=" + name + "&";
        for (var k = 0; k < arguments.length; k++) {
            request += "p" + k + "=" + arguments[k] + "&";
        }
        console.log(request);
        return loadDoc(request);
    }
}

//Check if the adapter is missing (i.e., outside FX app)
if (typeof (adapter) == 'undefined') {
    console.log("No adapter!");
    initializeAdapter();
}