package cn.moodright.drawandguess.entity.message;

/**
 * 单词提示消息
 * Created by moodright in 2021/5/21
 */
public class WordPromptMessage extends BaseMessage {
    /**
     * 单词长度
     */
    private int length;

    /**
     * @param transferObjectName wordPrompt
     * @param username serverSide
     * @param length 单词长度
     */
    public WordPromptMessage(String transferObjectName, String username, int length) {
        super(transferObjectName, username);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "WordPromptMessage{" +
                "length=" + length +
                '}';
    }
}
