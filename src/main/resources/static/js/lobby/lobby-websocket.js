var webSocket;  // 客户端socket对象
var username; // 连接时请求的用户名
var webSocketUrl; // 连接时请求的url

/**
 * 开启WebSocket连接
 */
function openWebSocket() {
    if('WebSocket' in window) {
        username = $('#socket-username').val();
        webSocketUrl = 'ws://localhost:8090/lobby/' + username;
        // 建立WebSocket连接
        webSocket = new WebSocket(webSocketUrl);
        // 建立连接回调函数
        webSocket.onopen = function() {
            console.log('用户：' + username + ' 连接建立成功！');
        }
        // 收到服务端消息回调函数
        webSocket.onmessage = function(event) {
            console.log("接收到的消息: " + event.data);
            var coordinate = JSON.parse(event.data);
            drawLine(coordinate.x, coordinate.y, coordinate.x1, coordinate.y1);
        }
        // 关闭连接回调函数
        webSocket.onclose = function() {
            console.log('用户：' + username + ' 关闭连接！');
        }
        // 发生错误回调函数
        webSocket.onerror = function() {
            console.log('用户：' + username + ' 连接建立失败！');
        }
    } else {
        alert('您的浏览器不支持WebSocket');
    }
}

/**
 * 坐标对象构造函数
 * @param x
 * @param y
 * @param x1
 * @param y1
 */
function Coordinate(x, y, x1, y1) {
    this.x = x;
    this.y = y;
    this.x1 = x1;
    this.y1 = y1;
}


/**
 * 发送消息
 */
function sendMessage(x, y, x1, y1) {
    var coordinate = new Coordinate(x, y, x1, y1);
    webSocket.send(JSON.stringify(coordinate));
}



