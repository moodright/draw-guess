package cn.moodright.drawandguess.entity.message;

/**
 * 重置游戏消息
 * Created by moodright in 2021/5/20
 */
public class ResetGameMessage extends BaseMessage {

    /**
     * @param transferObjectName resetGame
     * @param username serverSide
     */
    public ResetGameMessage(String transferObjectName, String username) {
        super(transferObjectName, username);
    }
}
