package cn.moodright.drawandguess.entity.game;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏变量
 * Created by moodright in 2021/5/17
 */
public class Settings {
    /**
     * 每回合倒计时
     */
    public static final int ROUND_COUNT_DOWN = 60;
    /**
     * 每回合单词选择的倒计时
     */
    public static final int WORD_PICK_COUNT_DOWN = 10;
    /**
     * 每回合单词展示倒计时
     */
    public static final int WORD_DISPLAY_COUNT_DOWN = 5;
    /**
     * 词典
     */
    public static final List<String> WORD_DICTIONARY = initWordDictionary();

    /**
     * 初始化词典
     */
    public static List<String> initWordDictionary(){
        ArrayList<String> dict = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("static/dictionary/dict.txt").getInputStream(), StandardCharsets.UTF_8));
            String lineInText;
            while((lineInText = br.readLine()) != null) {
                // 读取每行的词
                dict.add(lineInText.split(" ")[0]);
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return dict;
    }

}
