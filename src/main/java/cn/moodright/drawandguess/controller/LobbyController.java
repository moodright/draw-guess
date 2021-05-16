package cn.moodright.drawandguess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by moodright in 2021/5/16
 */
@Controller
public class LobbyController {

    /**
     * 大厅页面
     */
    @GetMapping("/lobby")
    public String lobby() {
        return "lobby";
    }
}
