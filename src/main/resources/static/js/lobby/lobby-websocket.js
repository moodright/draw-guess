var webSocket;  // 客户端socket对象
var username; // 连接时请求的用户名
var webSocketUrl; // 连接时请求的url

/**
 * 页面加载完成后自动建立连接
 */
$(function() {
    openWebSocket();
})


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
            var message = JSON.parse(event.data);
            // 同步绘画
            if(message.transferObjectName === 'coordinate') {
                drawLine(message.x, message.y, message.x1, message.y1);
            }
            // 同步颜色
            if(message.transferObjectName === 'color') {
                if(message.color === 0) {
                    context.strokeStyle = "red";
                }else if (message.color === 1) {
                    context.strokeStyle = "green";
                }else if (message.color === 2) {
                    context.strokeStyle = "skyblue";
                }
            }
            // 同步清空画板
            if(message.transferObjectName === 'clearBoard') {
                context.fillStyle="#FFFFFF";
                context.beginPath();
                context.fillRect(0,0, 600,500);
                context.closePath();
            }
            // 同步线条宽度
            if(message.transferObjectName === 'lineWidth') {
                context.lineWidth = message.lineWidth;
            }
            // 同步绘画者
            if(message.transferObjectName === 'whoIsPainter') {
                console.log(message.painter);
            }
            // 同步接收的单词
            if(message.transferObjectName === 'word') {
                console.log(message.word);
            }

            // console.log(message.toString());
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
 * @param username 客户端用户名
 * @param x 起始点x坐标
 * @param y 起始点y坐标
 * @param x1 终点x轴坐标
 * @param y1 终点y轴坐标
 */
function Coordinate(username, x, y, x1, y1) {
    this.transferObjectName = 'coordinate';
    this.username = username;
    this.x = x;
    this.y = y;
    this.x1 = x1;
    this.y1 = y1;
}

/**
 * 发送同步坐标消息
 */
function sendCoordinateMessage(x, y, x1, y1) {
    var coordinate = new Coordinate(username, x, y, x1, y1);
    webSocket.send(JSON.stringify(coordinate));
}

/**
 * 颜色对象构造函数
 * @param username 客户端用户名
 * @param color 颜色类型
 * @constructor
 */
function Color(username, color) {
    this.transferObjectName = 'color';
    this.username = username;
    this.color = color;
}

/**
 * 发送同步颜色信息
 */
function sendColorMessage(color) {
    var colorObj = new Color(username, color);
    webSocket.send(JSON.stringify(colorObj));
}

/**
 * 清空画板构造函数
 * @param username 客户端用户名
 * @constructor
 */
function ClearBoard(username) {
    this.transferObjectName = 'clearBoard';
    this.username = username;
}

/**
 * 发送同步清空画板信息
 */
function sendClearBoardMessage() {
    var clearBoard = new ClearBoard(username);
    webSocket.send(JSON.stringify(clearBoard))
}

/**
 * 线条宽度对象构造函数
 * @param username 客户端用户名
 * @param lw 线条宽度
 */
function LineWidth(username, lw) {
    this.transferObjectName = 'lineWidth';
    this.username = username;
    this.lineWidth = lw;
}

/**
 * 发送同步线条宽度信息
 */
function sendLineWidthMessage(num) {
    var lw = new LineWidth(username, num);
    webSocket.send(JSON.stringify(lw));
}




