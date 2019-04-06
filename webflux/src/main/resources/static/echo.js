function log(msg) {
    var messagesDiv = document.getElementById('messages');
    var elem = document.createElement('div');
    var txt = document.createTextNode(msg);
    elem.appendChild(txt);
    messagesDiv.append(elem);
}

var websocket = null;

document
    .getElementById('close')
    .addEventListener('click', function (evt) {
        evt.preventDefault();
        websocket.close();
        return false;
    });

window.addEventListener('load', function (e) {
    websocket = new WebSocket('ws://localhost:8080/ws/messages');
    websocket.addEventListener('message', function (e) {
        var msg = e.data;
        log(msg);
        websocket.send(msg + ' reply');
    });
});