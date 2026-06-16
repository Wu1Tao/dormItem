package com.wutao.dormItem.ui.web.cofig;

import com.wutao.dormItem.ui.web.auth.WebAuthInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class WebMvcConfig implements WebMvcConfigurer {

    private final WebAuthInterceptor webAuthInterceptor;

    public WebMvcConfig(WebAuthInterceptor webAuthInterceptor) {
        this.webAuthInterceptor = webAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webAuthInterceptor)
                .addPathPatterns("/**");
    }
}