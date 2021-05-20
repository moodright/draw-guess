package cn.moodright.drawandguess.entity.message;

/**
 * 单词消息
 * Created by moodright in 2021/5/18
 */
public class WordMessage extends BaseMessage {
    /**
     * 单词
     */
    private String word;

    /**
     * @param transferObjectName word
     */
    public WordMessage(String transferObjectName, String username, String word) {
        super(transferObjectName, username);
        this.word = word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "WordMessage{" +
                "word='" + word + '\'' +
                '}';
    }
}
