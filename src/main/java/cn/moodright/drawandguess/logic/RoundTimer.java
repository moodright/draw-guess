package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.message.GameStopMessage;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.entity.message.ResetGameMessage;
import cn.moodright.drawandguess.socket.LobbyWebSocketServer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.TimerTask;

/**
 * 回合定时器
 * Created by moodright in 2021/5/17
 */
public class RoundTimer extends TimerTask {
    private static final Log log = LogFactory.get();
    // 回合剩余时间倒计时
    private int roundCountDown = Settings.ROUND_COUNT_DOWN;
    // 当前回合单词
    private static String currentWord = null;

    @Override
    public void run() {
        try {
            // 广播倒计时消息
            LobbyWebSocketServer.broadcastMessage(String.valueOf(roundCountDown--));
            log.info("绘画剩余时间：" + roundCountDown);
            if(roundCountDown == 0) {
                log.info("当前回合计时结束");
                cancel();
                // 游戏总回合数减一
                GameProcess.subRoundCount();
                if (GameProcess.getRoundCount() > 0) {
                    log.info("新的回合开始, 还剩余" + GameProcess.getRoundCount() + "回合！");
                    GameProcess.roundStart();
                }
                if (GameProcess.getRoundCount() == 0) {
                    log.info("游戏结束，重置游戏资源");
                    // 重置游戏资源
                    resetGameResource();
                    // 发送重置游戏消息
                    LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new ResetGameMessage("resetGame", "serverSide")));
                    // 发送游戏停止消息
                    LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new GameStopMessage("gameStop", "serverSide")));
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
        // 重置游戏单词
        currentWord = null;
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
