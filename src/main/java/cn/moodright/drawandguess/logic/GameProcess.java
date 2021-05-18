package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.canvas.PainterMessage;
import cn.moodright.drawandguess.entity.canvas.WordMessage;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.socket.LobbyWebSocketServer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.*;

/**
 * 游戏过程
 * Created by moodright in 2021/5/17
 */
public class GameProcess {
    private static final Log log = LogFactory.get();
    // 定时器
    private static final Timer timer = new Timer();
    // 玩家用户名Map，用于确定绘画者
    private static Map<String, Boolean> playerMap = null;
    // 玩家用户名列表，存储顺序为玩家建立连接时的先后顺序
    private static List<String> playerList = null;

    /**
     * 回合开始
     */
    public static void roundStart() throws IOException {
        // 确定该回合绘画者
        String username = GameProcess.whoIsPainter();
        // 向绘画者发送该回合单词
        String word = sendWordToPainter(username);
        // 更新该回合单词
        Round.updateCurrentWord(word);
        // 当前回合定时器
        timer.schedule(new Round(), 0, 1000);
    }

    /**
     * 发送单词
     * @param painterUsername 绘画者用户名
     * @return 词典中取出的字符串
     */
    public static String sendWordToPainter(String painterUsername) throws IOException {
        // 随机选择词典单词
        String word = Settings.WORD[new Random().nextInt(Settings.WORD.length)];
        // 发送该单词至绘画者
        LobbyWebSocketServer.sendMessageToSpecifiedUser(
                JSON.toJSONString(new WordMessage("word", painterUsername, word)),
                painterUsername);
        return word;
    }

    /**
     * 确定绘画者
     * @return 绘画者用户名
     */
    public static String whoIsPainter() throws IOException {
        // 顺序选择绘画者
        for (int i = 0; i < playerList.size(); i++) {
            // 若该用户为非绘画者，则命中，修改判断字段
            if (!playerMap.get(playerList.get(i))) {
                playerMap.put(playerList.get(i), true);
                log.info("当前回合绘画者为：" + playerList.get(i));
                // 发送确定绘画者消息
                LobbyWebSocketServer.sendMessageToSpecifiedUser(
                        JSON.toJSONString(new PainterMessage("whoIsPainter", playerList.get(i), true)),
                        playerList.get(i));
                // 发送确定猜词者消息
                for (String username : playerList) {
                    if(!username.equals(playerList.get(i))) {
                        LobbyWebSocketServer.sendMessageToSpecifiedUser(
                                JSON.toJSONString(new PainterMessage("whoIsPainter", username, false)),
                                username);
                    }
                }
                if (i == playerList.size() - 1) {
                    log.info("此轮游戏结束，重置绘画者选取规则");
                    // 重置绘画者Map值
                    playerMap.replaceAll((k, v) -> false);
                }
                // 停止选择
                return playerList.get(i);
            }
        }
        log.info("异常情况发生，没有选中合适的绘画者！");
        return "";
    }

    /**
     * 初始化游戏资源
     */
    public static void initGameResource() {
        log.info("初始化游戏资源");
        // 初始化用户名列表
        playerList = LobbyWebSocketServer.getUsernameList();
        // 初始化绘画者Map
        playerMap = new HashMap<>();
        for (String username : playerList) {
            // 初始化所有玩家为非绘画者
            playerMap.put(username, false);
        }
    }


    /**
     * 游戏开始
     */
    public static void gameStart() throws IOException {
        log.info("游戏开始");
        // 游戏资源初始化
        initGameResource();
        // 第一回合开始
        roundStart();
    }
}
