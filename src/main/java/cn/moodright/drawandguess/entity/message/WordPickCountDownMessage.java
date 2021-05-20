package cn.moodright.drawandguess.entity.message;

/**
 * 选择单词倒计时消息
 * Created by moodright in 2021/5/20
 */
public class WordPickCountDownMessage extends BaseMessage {
    /**
     * 倒计时
     */
    private final int countDown;

    /**
     * @param transferObjectName 'wordPickCountDown'
     * @param username 'serverSide'
     * @param countDown 倒计时
     */
    public WordPickCountDownMessage(String transferObjectName, String username, int countDown) {
        super(transferObjectName, username);
        this.countDown = countDown;
    }

    public int getCountDown() {
        return countDown;
    }

    @Override
    public String toString() {
        return "WordPickCountDownMessage{" +
                "countDown=" + countDown +
                '}';
    }
}
