package cn.moodright.drawandguess.entity.canvas;

/**
 * 判断绘画者消息
 * Created by moodright in 2021/5/18
 */
public class PainterMessage extends BaseMessage {
    /**
     * 判断绘画者变量
     */
    private boolean painter;

    public PainterMessage(String transferObjectName, String username, boolean painter) {
        super(transferObjectName, username);
        this.painter = painter;
    }

    public void setPainter(boolean painter) {
        this.painter = painter;
    }

    public boolean getPainter() {
        return painter;
    }

    @Override
    public String toString() {
        return "PainterMessage{" +
                "isPainter=" + painter +
                '}';
    }
}
