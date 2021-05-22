package cn.moodright.drawandguess.entity.message;

/**
 * 重置回合消息
 * Created by moodright in 2021/5/21
 */
public class ResetRoundMessage extends BaseMessage {
    /**
     * @param transferObjectName resetRound
     * @param username serverSide
     */
    public ResetRoundMessage(String transferObjectName, String username) {
        super(transferObjectName, username);
    }
}
