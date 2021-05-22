package cn.moodright.drawandguess.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by moodright in 2021/5/21
 */
@Configuration
public class LobbyConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new LobbyInterceptor());
        interceptorRegistration.addPathPatterns("/**");
        interceptorRegistration.excludePathPatterns("/index","/", "/game/status", "/game/key/**", "/**/*.html","/**/*.js","/**/*.css","/**/*.mp3","/**/*.png", "/**/*.otf", "/**/*.eot", "/**/*.svg", "/**/*.ttf", "/**/*.woff", "/**/*.woff2");
    }
}
