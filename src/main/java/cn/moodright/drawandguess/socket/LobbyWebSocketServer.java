package cn.moodright.drawandguess.socket;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.logic.GameProcess;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by moodright in 2021/5/16
 */
@ServerEndpoint("/lobby/{username}")
@Component
public class LobbyWebSocketServer {

    private static final Log log = LogFactory.get();
    // 在线人数
    private static int onlineCount = 0;
    // 线程安全HashMap，用来存放每个每个客户端对应的WebSocket对象
    private static ConcurrentHashMap<String, LobbyWebSocketServer> lobbyWebSocketMap = new ConcurrentHashMap<>();
    // 客户端连接时的会话，通过它来给客户端发送数据
    private Session session;
    // 客户端连接时的用户名
    private String username;

    /**
     * 建立连接
     * @param session 建立连接的客户端会话
     * @param username 建立连接的客户端用户名
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username")String username) throws IOException {
        this.session = session;
        this.username = username;
        // 判断是否已建立同名的连接
        if(lobbyWebSocketMap.containsKey(username)) {
            lobbyWebSocketMap.remove(username);
            lobbyWebSocketMap.put(username, this);
        } else {
            lobbyWebSocketMap.put(username, this);
            // 连接数加一
            addOnlineCount();
        }
        log.info("用户：" + username + " 连接, 当前在线人数为：" + getOnlineCount());
        if(getOnlineCount() == 2) {
            // 游戏开始
            GameProcess.gameStart();
        }
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        if(lobbyWebSocketMap.containsKey(username)) {
            lobbyWebSocketMap.remove(username);
            // 连接数减一
            subOnlineCount();
        }
        log.info("用户：" + username + " 退出, 当前在线人数为：" + getOnlineCount());
    }

    /**
     * 接收到客户端消息调用方法
     * @param session 客户端会话
     * @param message 传输的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        log.info("用户：" + username + " 向服务器发送了信息：" + message);
        broadcastMessageExceptMyself(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户：" + username + ", 原因：" + error.getMessage());
    }

    /**
     * 实现服务器主动推送消息
     * @param message 消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 服务器广播消息（绘画者除外）
     * @param message 消息
     */
    public void broadcastMessageExceptMyself(String message) throws IOException {
        for (Map.Entry<String, LobbyWebSocketServer> entry : lobbyWebSocketMap.entrySet()) {
            if(!entry.getKey().equals(username)) {
                entry.getValue().sendMessage(message);
            }
        }
    }

    /**
     * 服务器广播消息
     * @param message 消息
     */
    public static void broadcastMessage(String message) throws IOException {
        for (Map.Entry<String, LobbyWebSocketServer> entry : lobbyWebSocketMap.entrySet()) {
            entry.getValue().sendMessage(message);
        }
    }







    // ---------------------------------------------------  static method start

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized  void subOnlineCount() {
        onlineCount--;
    }
}
