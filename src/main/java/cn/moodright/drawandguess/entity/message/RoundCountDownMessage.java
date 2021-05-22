package cn.moodright.drawandguess.entity.message;

/**
 * Created by moodright in 2021/5/21
 */
public class RoundCountDownMessage extends BaseMessage {
    /**
     * 倒计时
     */
    private int countDown;

    /**
     * @param transferObjectName roundCountDown
     * @param username serverSide
     * @param countDown 倒计时
     */
    public RoundCountDownMessage(String transferObjectName, String username, int countDown) {
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
        return "RoundCountDownMessage{" +
                "countDown=" + countDown +
                '}';
    }
}
