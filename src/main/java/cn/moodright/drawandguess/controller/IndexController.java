package cn.moodright.drawandguess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by moodright in 2021/5/16
 */

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
