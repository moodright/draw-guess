package cn.moodright.drawandguess.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 大厅拦截器
 * Created by moodright in 2021/5/21
 */
public class LobbyInterceptor implements HandlerInterceptor {
    /**
     * 拦截器验证map
     */
    public static Set<String> usernameSet = new HashSet<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/lobby/")) {
            String username = requestURI.replace("/lobby/", "");
            if (usernameSet.contains(username)) {
                return true;
            }
        }
        // 重定向到默认页
        response.sendRedirect(request.getContextPath() + "/");
        return false;
    }
}
