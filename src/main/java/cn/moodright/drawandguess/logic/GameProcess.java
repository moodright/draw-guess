package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.message.PainterMessage;
import cn.moodright.drawandguess.entity.message.WordMessage;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.entity.message.WordPromptMessage;
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
    // 玩家用户名列表，存储顺序为玩家建立连接时的先后顺序，用于确定绘画者
    private static List<String> playerList = null;
    // 玩家准备数量，用于判断游戏是否开始
    private static int playerReadyCount = 0;
    // 游戏总回合数
    private static int roundCount = 0;
    // 选择单词计时器
    private static WordPickTimer wordPickTimer = null;
    // 游戏状态
    private static boolean status = false;

    /**
     * 获取游戏状态
     * @return 游戏状态
     */
    public static synchronized boolean getGameStatus() {
        return status;
    }

    /**
     * 重置游戏状态
     */
    public static synchronized void resetGameStatus() {
        status = false;
    }

    public static synchronized void setGameStatus() {
        status = true;
    }

    /**
     * 获取单词计时器
     * @return 单词计时器
     */
    public static WordPickTimer getWordPickTimer() {
        return wordPickTimer;
    }

    /**
     * 获取游戏总回合数
     * @return 游戏总回合数
     */
    public static synchronized int getRoundCount() {
        return roundCount;
    }

    /**
     * 游戏总回合数自减
     */
    public static synchronized void subRoundCount() {
        roundCount--;
    }

    /**
     * 获取玩家准备数量
     * @return 玩家准备数量
     */
    public static synchronized int getPlayerReadyCount() {
        return playerReadyCount;
    }

    /**
     * 玩家准备数量自增
     */
    public static synchronized void addPlayerReadyCount() {
        playerReadyCount++;
    }

    /**
     * 玩家准备数量自减
     */
    public static synchronized void subPlayerReadyCount() {
        playerReadyCount--;
    }

    /**
     * 重置玩家准备数量
     */
    public static synchronized void resetPlayerReadyCount() {
        playerReadyCount = 0;
    }

    /**
     * 回合开始
     */
    public static void roundStart() throws IOException {
        // 确定该回合绘画者
        String painterUsername = GameProcess.whoIsPainter();
        // 向绘画者发送该回合单词
        String word = sendWordToPainter(painterUsername);
        // 初始化当前回合资源
        RoundTimer.initRoundResource(word, playerList.size());
        // 存储该引用用于接收到玩家确认单词消息时修改confirm变量
        wordPickTimer = new WordPickTimer(painterUsername);
        // 确认单词计时器开始计时
        timer.schedule(wordPickTimer, 0, 1000);
    }

    /**
     * 发送单词
     * @param painterUsername 绘画者用户名
     * @return 词典中取出的字符串
     */
    public static String sendWordToPainter(String painterUsername) throws IOException {
        // 随机选择词典单词
        String word = Settings.WORD_DICTIONARY.get(new Random().nextInt(Settings.WORD_DICTIONARY.size()));
        // 发送该单词至绘画者
        LobbyWebSocketServer.sendMessageToSpecifiedUser(
                JSON.toJSONString(new WordMessage("word", painterUsername, word)),
                painterUsername);
        // 发送单词提示
        LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new WordPromptMessage("wordPrompt", "serverSide", word.length())));
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
                LobbyWebSocketServer.broadcastMessage(
                        JSON.toJSONString(new PainterMessage("whoIsPainter", playerList.get(i))));
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
        // 修改游戏状态
        setGameStatus();
        // 初始化用户名列表
        playerList = LobbyWebSocketServer.getUsernameList();
        // 初始化绘画者Map
        playerMap = new HashMap<>();
        for (String username : playerList) {
            // 初始化所有玩家为非绘画者
            playerMap.put(username, false);
        }
        // 初始化游戏总回合数
        roundCount = getPlayerReadyCount() * 2;
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
