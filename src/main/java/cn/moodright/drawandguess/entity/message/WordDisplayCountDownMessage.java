package cn.moodright.drawandguess.entity.message;

/**
 * 单词展示倒计时消息
 * Created by moodright in 2021/5/21
 */
public class WordDisplayCountDownMessage extends BaseMessage {
    /**
     * 倒计时
     */
    private int countDown;

    /**
     * @param transferObjectName wordDisplayCountDown
     * @param username serverSide
     * @param countDown 倒计时
     */
    public WordDisplayCountDownMessage(String transferObjectName, String username, int countDown) {
        super(transferObjectName, username);
        this.countDown = countDown;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    @Override
    public String toString() {
        return "WordDisplayCountDownMessage{" +
                "countDown=" + countDown +
                '}';
    }
}
