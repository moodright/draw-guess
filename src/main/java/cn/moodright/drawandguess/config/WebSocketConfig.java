package cn.moodright.drawandguess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类
 * Created by moodright in 2021/5/16
 */
@Configuration
public class WebSocketConfig {

    /**
     * 开启WebSocket支持
     * 使用内置的Tomcat容器需要配置该Bean
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
