package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.entity.message.WordConfirmMessage;
import cn.moodright.drawandguess.entity.message.WordPickCountDownMessage;
import cn.moodright.drawandguess.socket.LobbyWebSocketServer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 选择单词计时器
 * Created by moodright in 2021/5/20
 */
public class WordPickTimer extends TimerTask {
    private static final Log log = LogFactory.get();
    // 定时器
    private static final Timer timer = new Timer();
    // 每回合单词确认剩余时间倒计时
    private int wordPickCountDown = Settings.WORD_PICK_COUNT_DOWN;
    // 确认开始绘画字段
    private boolean confirm = false;
    // 绘画者用户名
    private final String painterUsername;

    public WordPickTimer(String painterUsername) {
        this.painterUsername = painterUsername;
    }

    @Override
    public void run() {
        // 计时器自减
        wordPickCountDown--;
        log.info("选择单词剩余时间：" + wordPickCountDown);
        try {
            LobbyWebSocketServer.sendMessageToSpecifiedUser(
                    JSON.toJSONString(new WordPickCountDownMessage("wordPickCountDown", "serverSide", wordPickCountDown)),
                    painterUsername
            );
        } catch (IOException e) {
            log.error("服务器向该玩家发送选择单词倒计时消息出现异常", e);
            e.printStackTrace();
        }
        // 计时结束
        if( wordPickCountDown == 0 || confirm) {
            log.info("玩家确认该单词，请开始绘画！");
            try {
                LobbyWebSocketServer.sendMessageToSpecifiedUser(
                        JSON.toJSONString(new WordConfirmMessage("confirmWord", "serverSide", true)),
                        painterUsername
                );
            } catch (IOException e) {
                log.error("服务器向该玩家发送确认单词消息出现异常", e);
                e.printStackTrace();
            }
            cancel();
            // 开始本回合计时
            timer.schedule(new RoundTimer(), 0, 1000);
        }
    }

    /**
     * 玩家确认单词
     */
    public void painterConfirm() {
        confirm = true;
    }
}
