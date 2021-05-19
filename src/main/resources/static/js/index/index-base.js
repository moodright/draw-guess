/**
 * 跳转到大厅
 */
function toLobby() {
    var username = $('#username').val();
    if(username === '') {
        alert("请输入用户名");
        return false;
    }else {
        // 页面跳转
        location.href="/lobby/" + username;
    }
}