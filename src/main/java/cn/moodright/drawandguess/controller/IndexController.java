package cn.moodright.drawandguess.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.moodright.drawandguess.config.LobbyInterceptor;
import cn.moodright.drawandguess.logic.GameProcess;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by moodright in 2021/5/16
 */

@Controller
public class IndexController {
    private static final Log log = LogFactory.get();

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 验证游戏状态
     * @return boolean
     */
    @PostMapping("/game/status/")
    @ResponseBody
    public boolean getGameStatus() {
        log.info("验证当前游戏状态:" + GameProcess.getGameStatus());
        return GameProcess.getGameStatus();
    }

    /**
     * 验证口令
     * @param key 9418
     * @return boolean
     */
    @PostMapping("/game/key/{key}/{username}")
    @ResponseBody
    public int keyCheck(@PathVariable("key")String key, @PathVariable("username")String username) {
        if(key.equals("9418")) {
            // 拦截器验证字段
            boolean repeat = LobbyInterceptor.usernameSet.add(username);
            if(!repeat) {
                // 用户名重复
                return 3;
            }
            // 口令正确
            return 2;
        }
        // 口令错误
        return 1;
    }
}
