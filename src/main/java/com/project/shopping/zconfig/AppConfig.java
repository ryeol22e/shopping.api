package com.project.shopping.zconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

@Configuration
public class AppConfig {

    @Bean
    public ServletContextListener servletContextListener(DispatcherServlet dispatcherServlet) {
        return new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                dispatcherServlet.setThreadContextInheritable(true);
            }
        };
    }
}
