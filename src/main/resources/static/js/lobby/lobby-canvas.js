var board; // 画板
var X, Y, X1, Y1; // 绘画首末坐标
var flag = 0; // Canvas开辟新的绘画路径判断属性
var color = 0; // 所选颜色
var lineWidth = 2; // 画笔粗细
var canvas; // canvas
var context; // canvas-context
var isMouseDown = false; // 鼠标按下判断属性

window.onload = function() {
    // 获取画布
    canvas = document.getElementById('myCanvas');
    // 获取上下文
    context = canvas.getContext('2d');
    // 获取画板
    board = document.getElementById('board');
    // 绑定鼠标事件
    board.onmousedown = mouseDownAction;
    board.onmousemove = mouseMoveAction;
    document.onmouseup = mouseUpAction;
}

/**
 * 鼠标按下
 */
function mouseDownAction(e) {
    isMouseDown = true;
    // 记录鼠标点击时候的位置
    X = e.offsetX;
    Y = e.offsetY;
}

/**
 * 鼠标移动
 */
function mouseMoveAction(e) {
    if(isMouseDown) {
        X1 = e.offsetX;
        Y1 = e.offsetY;
        flag++;
        // 向服务器发送绘画消息
        sendCoordinateMessage(X, Y, X1, Y1);
        drawLine(X, Y, X1, Y1);
    }
}

/**
 * 鼠标弹起
 */
function mouseUpAction(e) {
    isMouseDown = false;
    flag = 0;
}

/**
 * 绘图
 */
function drawLine(x, y, x1, y1) {
    // 开启新路径
    context.beginPath();
    // 移动画笔初始位置
    context.moveTo(x, y);
    // 移动画笔的结束位置
    context.lineTo(x1, y1);
    // 开始绘制
    context.stroke();
    // 闭合路径
    context.closePath();
    // 鼠标按下移动时更新起始坐标
    if(flag != 0) {
        X = X1;
        Y = Y1;
    }
}

/**
 * 清除画布
 */
function clearCanvas() {
    context.clearRect(0, 0, 600, 500);
    // 向服务器发送清空画板信息
    sendClearBoardMessage();
}

/**
 * 画笔颜色选择
 */
function colorChoose(num) {
    color = num;
    // 更新画笔颜色
    if(color === 0) {
        context.strokeStyle = "red";
    }else if (color === 1) {
        context.strokeStyle = "green";
    }else if (color === 2) {
        context.strokeStyle = "skyblue";
    }
    // 向服务器发送颜色信息
    sendColorMessage(num);
}

/**
 * 画笔粗细选择
 */
function lineWidthChoose(num) {
    lineWidth = num;
    // 更新画笔宽度
    context.lineWidth = lineWidth;
    // 向服务器发送宽度信息
    sendLineWidthMessage(num);
}