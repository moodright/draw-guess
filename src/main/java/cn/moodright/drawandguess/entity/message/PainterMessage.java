package cn.moodright.drawandguess.entity.message;

/**
 * 判断绘画者消息
 * Created by moodright in 2021/5/18
 */
public class PainterMessage extends BaseMessage {

    /**
     * @param transferObjectName whoIsPainter
     * @param username 绘画者用户名
     */
    public PainterMessage(String transferObjectName, String username) {
        super(transferObjectName, username);
    }
}
