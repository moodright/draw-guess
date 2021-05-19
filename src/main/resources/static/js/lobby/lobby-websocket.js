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
            // 向服务器发送上线信息
            sendPlayerOnlineMessage(username);
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
                switch (message.color) {
                    case 0: context.strokeStyle = "#000000"; break;
                    case 1: context.strokeStyle = "#d0010c"; break;
                    case 2: context.strokeStyle = "#ec7012"; break;
                    case 3: context.strokeStyle = "#f19913"; break;
                    case 4: context.strokeStyle = "#f6c71a"; break;
                    case 5: context.strokeStyle = "#fdf31c"; break;
                    case 6: context.strokeStyle = "#cadf1b"; break;
                    case 7: context.strokeStyle = "#97c71e"; break;
                    case 8: context.strokeStyle = "#019218"; break;
                    case 9: context.strokeStyle = "#0195a2"; break;
                    case 10: context.strokeStyle = "#0097cb"; break;
                    case 11: context.strokeStyle = "#0199f1"; break;
                    case 12: context.strokeStyle = "#007dd3"; break;
                    case 13: context.strokeStyle = "#0161b3"; break;
                    case 14: context.strokeStyle = "#004594"; break;
                    case 15: context.strokeStyle = "#000268"; break;
                    case 16: context.strokeStyle = "#51015c"; break;
                    case 17: context.strokeStyle = "#7a014a"; break;
                }
            }
            // 同步清空画板
            if(message.transferObjectName === 'clearBoard') {
                context.fillStyle="#FFFFFF";
                context.beginPath();
                context.fillRect(0,0, 852,520);
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
            // 同步其它用户上线信息
            if(message.transferObjectName === 'playerOnline') {
                // 将其它用户信息添加到玩家列表中
                addPlayerInfoToPlayerList(message);
                // 将其他用户上线信息添加到聊天框中
                addPlayerOnlineInfoToChatList(message);
            }
            // 同步其它用户下线信息
            if(message.transferObjectName === 'playerOffline') {
                // 将其它用户信息从玩家列表中删除
                removePlayerInfoFromPlayerList(message);
                // 将其它用户下线信息添加到聊天框中
                addPlayerOfflineInfoToChatList(message);
            }

            // console.log(message.toString());
        }
        // 关闭连接回调函数
        webSocket.onclose = function() {
            // 主动关闭浏览器是否触发存疑
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

/**
 * 客户端在线消息构造函数
 * @param username
 * @constructor
 */
function PlayerOnline(username) {
    this.transferObjectName = 'playerOnline';
    this.username = username;
}

/**
 * 发送同步用户上线信息
 * @param username
 */
function sendPlayerOnlineMessage(username) {
    var player = new PlayerOnline(username);
    webSocket.send(JSON.stringify(player));
}

/**
 * 客户端离线消息构造函数
 * @param username 客户端用户名
 * @constructor
 */
function PlayerOffline(username) {
    this.transferObjectName = 'playerOffline';
    this.username = username;
}

/**
 * 发送同步用户下线信息
 * @param username 客户端用户名
 */
function sendPlayerOfflineMessage(username) {
    var player = new PlayerOffline(username);
    webSocket.send(JSON.stringify(player));
}

/**
 * 设置关闭浏览器监听属性
 * @type {onbeforeunload_handler} 关闭浏览器时触发的函数
 */
window.onbeforeunload = onbeforeunload_handler;

/**
 * 关闭浏览器时触发的函数
 */
function onbeforeunload_handler() {
    // 向服务器发送下线信息
    sendPlayerOfflineMessage(username);
}






