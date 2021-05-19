package cn.moodright.drawandguess.entity.canvas;

/**
 * 在线消息
 * Created by moodright in 2021/5/19
 */
public class OnlineMessage extends BaseMessage {

    /**
     * @param transferObjectName playerOnline
     * @param username 客户端用户名
     */
    public OnlineMessage(String transferObjectName, String username) {
        super(transferObjectName, username);
    }

}
