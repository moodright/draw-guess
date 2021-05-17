package cn.moodright.drawandguess.logic;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.entity.game.Settings;
import cn.moodright.drawandguess.socket.LobbyWebSocketServer;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;

/**
 * 游戏过程
 * Created by moodright in 2021/5/17
 */
public class GameProcess {
    private static final Log log = LogFactory.get();
    // 定时器
    private static final Timer timer = new Timer();

    /**
     * 回合开始
     */
    public static void roundStart() throws IOException {
        // 发送该回合单词
        String word = sendWord();
        // 更新该回合单词
        Round.updateCurrentWord(word);
        // 当前回合定时器
        timer.schedule(new Round(), 0, 1000);
    }

    /**
     * 发送单词
     * @return 词典中取出的字符串
     */
    public static String sendWord() throws IOException {
        // 随机选择词典单词
        String word = Settings.WORD[new Random().nextInt(Settings.WORD.length)];
        // 广播发送该词
        LobbyWebSocketServer.broadcastMessage(JSON.toJSONString(word));
        return word;
    }

    /**
     * 游戏开始
     */
    public static void gameStart() throws IOException {
        log.info("游戏开始");
        roundStart();
    }
}
