/**
 * 将用户信息添加到玩家列表
 * @param playerInfo 用户信息
 */
function addPlayerInfoToPlayerList(playerInfo) {
    // 获取玩家列表
    var playerList = $('#playerList');
    // 判断该标签是否存在
    if($('#' + playerInfo.username + '-info').length === 0) {
        // 创建玩家信息标签
        var playerInfoTag = $('<div ' + 'id=\"' + playerInfo.username + '-info\" ' +
            'class="player-info">\n' +
            '                    <div class="left-avatar">\n' +
            '                        <img class="avatar" src="/image/avatar.png" title="moodright"/>\n' +
            '                    </div>\n' +
            '                    <div class="right-info">\n' +
            '                        <div class="username">\n' +
            playerInfo.username + '\n' +
            '                        </div>\n' +
            '                        <div class="rank-info">\n' +
            '                            <div class="credit">\n' +
            '                                <i class="fa fa-trophy"></i><span'+ 'id=\"' + playerInfo.username  + '-credit\">0</span>\n' +
            '                            </div>\n' +
            '                            <div id="'+ playerInfo.username + '-ready" class="painter-icon">\n' +
            '                                <i class="fa fa-paint-brush"></i>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>');
        // 将玩家信息标签添加到玩家列表中
        playerList.append(playerInfoTag);
    }
}

/**
 * 将玩家信息从玩家列表移除
 * @param playerInfo
 */
function removePlayerInfoFromPlayerList(playerInfo) {
    var player = $('#' + playerInfo.username + '-info');
    if(player.length === 1) {
        player.remove();
    }
}

/**
 * 将玩家上线信息添加到聊天框中
 * @param playerInfo
 */
function addPlayerOnlineInfoToChatList(playerInfo) {
    var chatList = $('#chat-list');
    var onlineTag = $('<div class="single-chat">\n' +
        '                    <span class="username">' +
                            playerInfo.username +
        '</span><span> 加入了游戏</span>' +
        '                </div>');
    chatList.append(onlineTag);
}

/**
 * 将玩家下线信息添加到聊天框中
 * @param playerInfo
 */
function addPlayerOfflineInfoToChatList(playerInfo) {
    var chatList = $('#chat-list');
    var onlineTag = $('<div class="single-chat">\n' +
        '                    <span class="username">' +
        playerInfo.username +
        '</span><span> 离开了游戏</span>' +
        '                </div>');
    chatList.append(onlineTag);
}

/**
 * 准备
 */
function iamready() {
    // 获取准备图标元素
    var readyIcon = $('#' + username + '-ready');
    // 获取准备按钮元素
    var readyButton = $('#ready-button');
    if(readyIcon.css('display') === 'none') {
        // 准备
        readyIcon.css('display', 'block');
        readyButton.css('background', '#409EFF');
        readyButton.css('color', '#ffffff');
        readyButton.text("取消准备");
        // 向服务器发送准备消息
        sendReadyMessage(username, true);
    }else {
        // 取消准备
        readyIcon.css('display', 'none');
        readyButton.css('background', '#ffffff');
        readyButton.css('color', '#000000');
        readyButton.text("准备");
        // 向服务器发送取消准备消息
        sendReadyMessage(username, false);
    }
}

/**
 * 在游戏开始前禁用准备功能
 */
function disableReadyFunctionBeforeGameStart() {
    // 获取准备图标元素
    var readyIcon = $('.painter-icon');
    // 获取准备按钮元素
    var readyButton = $('#ready-button');
    // 重置准备图标
    readyIcon.css('display', 'none');
    // 重置准备按钮内容
    readyButton.css('background', '#ffffff');
    readyButton.css('color', '#000000');
    readyButton.text("准备");
    // 禁用准备按钮
    readyButton.css('display', 'none');
}

/**
 * 在游戏结束后恢复准备功能
 */
function enableReadyFunctionAfterGameStop() {
    // 获取准备图标元素
    var readyIcon = $('.painter-icon');
    // 获取准备按钮元素
    var readyButton = $('#ready-button');
    // 重置准备图标
    readyIcon.css('display', 'none');
    // 开启准备按钮
    readyButton.css('display', 'block');
}

/**
 * 开始画图
 */
function confirmPaint() {
    // 获取单词提示标签元素
    var wordAlertTag = $('#word-alert');
    // 获取遮罩层标签元素
    var maskTag = $('.mask');
    wordAlertTag.css('display', 'none');
    maskTag.css('display', 'none');
    // 发送确认消息
    sendWordConfirmMessage(username);
}

/**
 * 显示单词提示框
 * @param word 提示的单词
 */
function showWordAlert(word, title) {
    // 获取单词标题
    var wordTitle = $('#word-title');
    // 获取单词内容元素
    var wordContentTag = $('#word');
    // 获取单词提示标签元素
    var wordAlertTag = $('#word-alert');
    // 获取遮罩层标签元素
    var maskTag = $('.mask');
    wordTitle.text(title);
    wordContentTag.text(word);
    wordAlertTag.css('display', 'block');
    maskTag.css('display', 'block');
}

function hideWordAlert() {
    // 获取单词提示标签元素
    var wordAlertTag = $('#word-alert');
    // 获取遮罩层标签元素
    var maskTag = $('.mask');
    wordAlertTag.css('display', 'none');
    maskTag.css('display', 'none');
}



/**
 * 更新确认单词倒计时时间
 */
function updateWordPickCountDownTag(time) {
    var countDown = $('#time-left');
    countDown.text(time);
}

/**
 * 显示绘画图标
 */
function showPainterIcon(message) {
    // 获取准备图标元素
    var readyIcon = $('#' + message.username + '-ready');
    readyIcon.css('display', 'block');
}

function hidePainterIcon(message) {
    // 获取准备图标元素
    var readyIcon = $('#' + message.username + '-ready');
    readyIcon.css('display', 'none');
}

function hideEveryonesPainterIcon() {
    var readyIcon = $('.painter-icon');
    readyIcon.css('display', 'none');
}

/**
 * 开启绘画功能
 */
function enablePaintingFunction() {
    // 获取工具栏元素
    var tools = $('#board-tools');
    // 显示工具栏
    tools.css('visibility', 'visible');
    // 修改绘画判断字段
    isPainter = true;
}

/**
 * 关闭绘画功能
 */
function disablePaintingFunction() {
    // 获取工具栏元素
    var tools = $('#board-tools');
    // 隐藏工具栏
    tools.css('visibility', 'hidden');
    // 修改绘画判断字段
    isPainter = false;
}

/*
 * 设置输入域(input/textarea)光标的位置
 * @param {HTMLInputElement/HTMLTextAreaElement} elem
 * @param {Number} index
 */
function setCursorPosition(elem, index) {
    var val = elem.value
    var len = val.length

    // 超过文本长度直接返回
    if (len < index) return
    setTimeout(function() {
        elem.focus()
        if (elem.setSelectionRange) { // 标准浏览器
            elem.setSelectionRange(index, index)
        } else { // IE9-
            var range = elem.createTextRange()
            range.moveStart("character", -len)
            range.moveEnd("character", -len)
            range.moveStart("character", index)
            range.moveEnd("character", 0)
            range.select()
        }
    }, 10)
}

/**
 * 将聊天内容添加到聊天列表中
 * @param message
 */
function addChatContentToChatList(message) {
    var chatList = $('#chat-list');
    var onlineTag = $('<div class="single-chat">\n' +
        '                    <span class="username">' +
        message.username +
        '</span><span> ：' + message.content    + '</span>' +
        '                </div>');
    chatList.append(onlineTag);
    // 聊天框滚轮自动滚动到最底部
    chatList.scrollTop(chatList[0].scrollHeight);
}

/**
 * 播放正确提示音
 */
function playBingoMusicAlert() {
    var audio = document.getElementById('bingo');
    // 重置声音资源
    audio.load();
    // 播放声音资源
    audio.play();
}

/**
 * 禁用输入框
 */
function disableTextarea() {
    $('#chat-content').attr("disabled", "disabled");
}

/**
 * 开启输入框
 */
function enableTextarea() {
    $('#chat-content').removeAttr("disabled");
}

/**
 * 更新每回合倒计时标签
 */
function updateRoundCountDownTag(time) {
    var countDownTag = $('#countdown');
    countDownTag.text(time);
}

/**
 * 重置每回合倒计时
 */
function resetRoundCountDownTag() {
    var countDownTag = $('#countdown');
    countDownTag.text("--");
}

/**
 * 更新每回合提示单词信息
 */
function updateWordPromptTag(length) {
    var wordLengthTag = $('#word-length');
    wordLengthTag.text(length);
}

function resetWordPromptTag() {
    var wordLengthTag = $('#word-length');
    wordLengthTag.text("-");
}
