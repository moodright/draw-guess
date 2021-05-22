package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.entity.message.GameStopMessage;
import cn.moodright.drawandguess.entity.message.ResetGameMessage;
import cn.moodright.drawandguess.entity.message.ResetRoundMessage;
import cn.moodright.drawandguess.entity.message.WordDisplayCountDownMessage;
import cn.moodright.drawandguess.socket.LobbyWebSocketServer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 单词展示定时器
 * Created by moodright in 2021/5/21
 */
public class WordDisplayTimer extends TimerTask {
    private static final Log log = LogFactory.get();
    // 定时器
    private static final Timer timer = new Timer();
    // 单词展示倒计时
    private int wordDisplayCountDown = Settings.WORD_DISPLAY_COUNT_DOWN;

    @Override
    public void run() {
        wordDisplayCountDown--;
        log.info("展示单词剩余时间" + wordDisplayCountDown);
        try {
            // 发送展示单词倒计时
            LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new WordDisplayCountDownMessage("wordDisplayCountDown", "serverSide", wordDisplayCountDown)));
            if (wordDisplayCountDown == 0) {
                cancel();
                if (GameProcess.getRoundCount() > 0) {
                    log.info("新的回合开始, 还剩余" + GameProcess.getRoundCount() + "回合！");
                    // 发送重置回合消息
                    LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new ResetRoundMessage("resetRound", "serverSide")));
                    // 新的回合开始
                    GameProcess.roundStart();
                }
                if (GameProcess.getRoundCount() == 0) {
                    log.info("游戏结束，重置游戏资源");
                    // 重置游戏资源
                    RoundTimer.resetGameResource();
                    // 发送重置游戏消息
                    LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new ResetGameMessage("resetGame", "serverSide")));
                    // 发送游戏停止消息
                    LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(new GameStopMessage("gameStop", "serverSide")));
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
