package cn.moodright.drawandguess.socket;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.config.LobbyInterceptor;
import cn.moodright.drawandguess.entity.message.*;
import cn.moodright.drawandguess.logic.GameProcess;
import cn.moodright.drawandguess.logic.RoundTimer;
import cn.moodright.drawandguess.logic.WordPickTimer;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    // 客户端连接时的顺序
    private int order;

    /**
     * 建立连接
     * @param session 建立连接的客户端会话
     * @param username 建立连接的客户端用户名
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username")String username) throws IOException {
        this.session = session;
        this.username = username;
        this.order = onlineCount;
        // 判断是否已建立同名的连接
        if(lobbyWebSocketMap.containsKey(username)) {
            lobbyWebSocketMap.remove(username);
            lobbyWebSocketMap.put(username, this);
        } else {
            lobbyWebSocketMap.put(username, this);
            // 连接数加一
            addOnlineCount();
        }
        // 服务器向建立连接的客户端发送已在线用户的同步信息
        // 按照用户加入顺序排序
        List<LobbyWebSocketServer> collect = lobbyWebSocketMap.values().stream().sorted(Comparator.comparing(LobbyWebSocketServer::getOrder)).collect(Collectors.toList());
        for( LobbyWebSocketServer lobbyWebSocketServer : collect) {
            sendMessageToSpecifiedUser(JSON.toJSONString(
                    new OnlineMessage("playerOnline", lobbyWebSocketServer.username)),
                    this.username);
        }

        log.info("用户：" + username + " 连接, 当前在线人数为：" + getOnlineCount());

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
            // 拦截器验证list
            log.info("拦截器验证list中用户被删除：" + username);
            LobbyInterceptor.usernameSet.remove(username);
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
        // 将message字段转换为基础消息对象
        BaseMessage baseMessage = JSON.parseObject(message, BaseMessage.class);
        // 接收玩家准备消息
        if(baseMessage.getTransferObjectName().equals("ready")) {
            ReadyMessage readyMessage = JSON.parseObject(message, ReadyMessage.class);
            if (readyMessage.getReady().equals(true)) {
                log.info("用户：" + username + " 已准备！");
                // 玩家准备数量加一
                GameProcess.addPlayerReadyCount();
                // 游戏开始判断：准备人数大于1 且 准备人数与游戏在线人数相等 -> 游戏开始
                if(GameProcess.getPlayerReadyCount() == getOnlineCount() && GameProcess.getPlayerReadyCount() > 1) {
                    // 广播游戏开始消息
                    broadcastMessage(JSON.toJSONString(new GameStartMessage("gameStart", "serverSide")));
                    // 游戏开始
                    GameProcess.gameStart();
                    return;
                }
            }else {
                log.info("用户：" + username + " 取消准备！");
                // 玩家准备数量减一
                GameProcess.subPlayerReadyCount();
            }
        }
        // 接收绘画者确认单词消息
        if(baseMessage.getTransferObjectName().equals("confirmWord")) {
            WordConfirmMessage wordConfirmMessage = JSON.parseObject(message, WordConfirmMessage.class);
            if(GameProcess.getWordPickTimer() != null) {
                GameProcess.getWordPickTimer().painterConfirm();
                log.info("用户：" + wordConfirmMessage.getUsername() + " 确认该单词");
            }
        }
        // 接收聊天内容消息
        if(baseMessage.getTransferObjectName().equals("chat")) {
            ChatMessage chatMessage = JSON.parseObject(message, ChatMessage.class);
            // 消息命中单词处理
            if (chatMessage.getContent().equals(RoundTimer.getCurrentWord())) {
                chatMessage.setContent("Bingo!");
                message = JSON.toJSONString(chatMessage);
                // 错误人数自减
                RoundTimer.subFalseCount();
            }
            // 广播消息内容
            broadcastMessage(message);
            return;
        }
        // 消息转发
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
        synchronized (this.session) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 服务器广播消息（绘画者除外）
     * @param message 消息
     */
    public void broadcastMessageExceptMyself(String message) throws IOException {
        for (Map.Entry<String, LobbyWebSocketServer> entry : lobbyWebSocketMap.entrySet()) {
            // 往服务端发送消息的客户端不需要再次接收此消息
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

    /**
     * 服务器向指定客户端发送消息
     * @param message 消息
     * @param username 客户端用户名
     */
    public static void sendMessageToSpecifiedUser(String message, String username) throws IOException {
        if(lobbyWebSocketMap.containsKey(username)) {
            lobbyWebSocketMap.get(username).sendMessage(message);
            log.info("向用户：" + username + " 发送消息:" + message);
        }else {
            log.info("未找到该用户，发送消息失败！");
        }
    }

    /**
     * order默认set方法，用于lobbyWebSocketMap排序
     */
    public int getOrder() {
        return order;
    }

    /**
     * 获取客户端用户名列表
     * @return 客户端用户名列表
     */
    public static List<String> getUsernameList() {
        List<String> usernameSequenceList = new ArrayList<>();
        // 根据order字段对map进行排序
        List<LobbyWebSocketServer> collect = lobbyWebSocketMap.values().stream().sorted(Comparator.comparing(LobbyWebSocketServer::getOrder)).collect(Collectors.toList());
        for( LobbyWebSocketServer lobbyWebSocketServer : collect) {
            usernameSequenceList.add(lobbyWebSocketServer.username);
        }
        return usernameSequenceList;
    }

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
