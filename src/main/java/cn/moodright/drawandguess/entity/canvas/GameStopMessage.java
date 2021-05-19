package cn.moodright.drawandguess.entity.canvas;

/**
 * 游戏结束消息
 * Created by moodright in 2021/5/19
 */
public class GameStopMessage extends BaseMessage {
    /**
     * @param transferObjectName gameStop
     * @param username serverSide
     */
    public GameStopMessage(String transferObjectName, String username) {
        super(transferObjectName, username);
    }
}
