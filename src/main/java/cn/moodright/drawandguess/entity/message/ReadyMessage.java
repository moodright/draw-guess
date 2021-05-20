package cn.moodright.drawandguess.entity.message;

/**
 * 准备消息
 * Created by moodright in 2021/5/19
 */
public class ReadyMessage extends BaseMessage {
    private Boolean ready;

    /**
     * @param transferObjectName ready
     */
    public ReadyMessage(String transferObjectName, String username, Boolean ready) {
        super(transferObjectName, username);
        this.ready = ready;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "ReadyMessage{" +
                "ready=" + ready +
                '}';
    }
}
