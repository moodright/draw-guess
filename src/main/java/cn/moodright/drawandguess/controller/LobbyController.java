package cn.moodright.drawandguess.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by moodright in 2021/5/16
 */
@Controller
public class LobbyController {
    private static final Log log = LogFactory.get();

    /**
     * 大厅页面
     * @param username 用户名
     */
    @GetMapping("/lobby/{username}")
    public String lobby(@PathVariable("username")String username, Model model) {
        model.addAttribute("username", username);
        return "lobby";
    }

    @GetMapping("/newlobby")
    public String newLobby() {
        return "newlobby";
    }
}
