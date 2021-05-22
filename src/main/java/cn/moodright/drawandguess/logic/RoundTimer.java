package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.message.*;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.socket.LobbyWebSocketServer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 回合定时器
 * Created by moodright in 2021/5/17
 */
public class RoundTimer extends TimerTask {
    private static final Log log = LogFactory.get();
    // 定时器
    private static final Timer timer = new Timer();
    // 回合剩余时间倒计时
    private int roundCountDown = Settings.ROUND_COUNT_DOWN;
    // 当前回合单词
    private static String currentWord = null;
    // 当前回合猜词正确人数，用于判断该回合是否提前结束
    private static int falseCount = 0;

    /**
     * 猜词正确人数自增
     */
    public static synchronized void subFalseCount() {
        falseCount--;
    }

    /**
     * 获取猜词正确人数
     * @return 猜词正确人数
     */
    public static synchronized int getFalseCount() {
        return falseCount;
    }

    /**
     * 更新猜词正确人数
     */
    public static synchronized void updateFalseCount(int playerCount) {
        falseCount = playerCount;
    }

    /**
     * 初始化当前回合资源
     * @param word 当前回合单词
     * @param playerCount 游玩人数
     */
    public static void initRoundResource(String word, int playerCount) {
        // 初始化当前回合单词
        updateCurrentWord(word);
        // 初始化游玩人数
        updateFalseCount(playerCount);
    }

    @Override
    public void run() {
        try {
            roundCountDown--;
            // 广播倒计时消息
            LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new RoundCountDownMessage("roundCountDown", "serverSide", roundCountDown)));
            log.info("绘画剩余时间：" + roundCountDown);
            if(getFalseCount() == 1 && roundCountDown > 0 || roundCountDown == 0) {
                log.info("当前回合计时结束");
                cancel();
                // 游戏总回合数减一
                GameProcess.subRoundCount();
                // 发送展示的单词
                LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new WordMessage( "wordDisplay", "serverSide", currentWord)));
                // 触发展示单词定时器
                timer.schedule(new WordDisplayTimer(), 0, 1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置游戏资源
     */
    public static void resetGameResource() {
        // 重置游戏单词
        currentWord = null;
        // 重置游戏状态
        GameProcess.resetGameStatus();
        // 重置玩家准备数量
        GameProcess.resetPlayerReadyCount();
    }

    /**
     * 更新当前回合单词
     * @param word 当前回合单词
     */
    public static void updateCurrentWord(String word) {
        currentWord = word;
    }

    /**
     * 获得当前回合单词
     */
    public static String getCurrentWord() {
        return currentWord;
    }



}
