package cn.moodright.drawandguess.entity.canvas;

/**
 * 传输消息基础实体类
 * Created by moodright in 2021/5/16
 */
public class Message {
    /**
     * 传输对象名
     */
    private String transferObjectName;
    /**
     * 客户端用户名
     */
    private String username;

    public void setTransferObjectName(String transferObjectName) {
        this.transferObjectName = transferObjectName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransferObjectName() {
        return transferObjectName;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "BaseDTO{" +
                "transferObjectName='" + transferObjectName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
