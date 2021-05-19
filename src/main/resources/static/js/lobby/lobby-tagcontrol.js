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
            '                            <div class="painter-icon">\n' +
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
