/**
 * 跳转到大厅
 */
function toLobby() {
    var username = $('#username').val();
    var key = $('#key').val();
    if(key === '') {
        alert("请输入口令");
        return false;
    }
    if(username === '') {
        alert("请输入用户名");
        return false;
    }
    $.ajax({
        url: '/game/status',
        method: 'post',
        async: true,
        success: function(gameStatus) {
            if(gameStatus === true) {
                alert("游戏正在进行中，请等待游戏结束再加入！");
                return false;
            }else {
                $.ajax({
                    url: '/game/key/' + key + "/" + $('#username').val(),
                    method: 'post',
                    async: true,
                    success: function(keyStatus) {
                        if(keyStatus === 2) {
                            location.href = '/lobby/' + username;
                        }else if(keyStatus === 3){
                            alert("该用户名已存在游戏中");
                            return false;
                        }else if(keyStatus === 1) {
                            alert("口令错误！");
                            return false;
                        }
                    }
                });
            }
        }
    });
}