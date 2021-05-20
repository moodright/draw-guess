package cn.moodright.drawandguess.entity.message;

/**
 * 聊天内容消息
 * Created by moodright in 2021/5/21
 */
public class ChatMessage extends BaseMessage {
    /**
     * 内容
     */
    private String content;

    /**
     * @param transferObjectName chat
     * @param username 客户端用户名
     * @param content 内容
     */
    public ChatMessage(String transferObjectName, String username, String content) {
        super(transferObjectName, username);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "content='" + content + '\'' +
                '}';
    }
}
