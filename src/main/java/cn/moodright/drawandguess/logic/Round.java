package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.socket.LobbyWebSocketServer;

import java.io.IOException;
import java.util.TimerTask;

/**
 * 回合定时器
 * Created by moodright in 2021/5/17
 */
public class Round extends TimerTask {
    private static final Log log = LogFactory.get();
    // 回合剩余时间倒计时
    private int roundCountDown = Settings.ROUND_COUNT_DOWN;
    // 总回合数
    private static int roundCount = Settings.ROUND_COUNT;
    // 当前回合单词
    private static String currentWord = null;

    @Override
    public void run() {
        try {
            // 广播倒计时消息
            LobbyWebSocketServer.broadcastMessage(String.valueOf(roundCountDown--));
            if(roundCountDown == 0) {
                log.info("当前回合计时结束");
                cancel();
                // 游戏总回合数减一
                roundCount--;
                if (roundCount > 0) {
                    log.info("新的回合开始, 还剩余" + roundCount + "回合！");
                    GameProcess.roundStart();
                }
                if (roundCount == 0) {
                    log.info("游戏结束，重置游戏资源");
                    // 重置游戏资源
                    resetGameResource();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置游戏资源
     */
    public static void resetGameResource() {
        // 重置总回合数
        roundCount = Settings.ROUND_COUNT;
        // 重置游戏单词
        currentWord = null;
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
