package cn.moodright.drawandguess.entity.message;

/**
 * 单词确认消息
 * Created by moodright in 2021/5/20
 */
public class WordConfirmMessage extends BaseMessage {
    /**
     * 确认单词判断字段
     */
    private boolean confirm;

    /**
     * @param transferObjectName confirmWord
     * @param username 客户端用户名
     */
    public WordConfirmMessage(String transferObjectName, String username, boolean confirm) {
        super(transferObjectName, username);
        this.confirm = confirm;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "WordConfirmMessage{" +
                "confirm=" + confirm +
                '}';
    }
}
