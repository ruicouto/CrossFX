/* 
 * stub
 * ruicouto in 2/fev/2017
 */

var methods;

function setMethods() {
    var m = loadDoc("method=getMethods");
    methods = m.split(",");
    console.log(m);
}

function loadDoc(p) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "http://localhost:8080/FXStub/Stub?" + p, false); // false for synchronous request
    xmlHttp.send(null);
    return xmlHttp.responseText;
}

var initializeAdapter = function () {
    setMethods();
    adapter = Object();
    for (var i = 0; i < methods.length; i++) {
        var n = methods[i];
        adapter[n] = genMethod(n);
    }
}

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


if (typeof (adapter) == 'undefined') {
    console.log("No adapter!");
    initializeAdapter();
}