package cn.moodright.drawandguess.entity.game;

/**
 * 游戏变量
 * Created by moodright in 2021/5/17
 */
public class Settings {
    /**
     * 每回合倒计时
     */
    public static final int ROUND_COUNT_DOWN = 5;
    /**
     * 词典
     */
    public static final String[] WORD = {"猫", "狗", "鸡"};
    /**
     * 游戏人数
     */
    public static final int PLAYER_COUNT = 2;
    /**
     * 游戏回合数
     */
    public static final int ROUND_COUNT = PLAYER_COUNT * 2;
}
