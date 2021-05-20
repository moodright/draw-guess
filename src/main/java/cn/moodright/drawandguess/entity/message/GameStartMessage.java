package cn.moodright.drawandguess.entity.message;

/**
 * 游戏开始消息
 * Created by moodright in 2021/5/19
 */
public class GameStartMessage extends BaseMessage {

    /**
     * @param transferObjectName gameStart
     * @param username serverSide
     */
    public GameStartMessage(String transferObjectName, String username) {
        super(transferObjectName, username);
    }
}
