package ru.eljke.tournamentsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("").setViewName("redirect:/admin/members");
        registry.addViewController("/").setViewName("redirect:/admin/members");
        registry.addViewController("/sw.js").setViewName("redirect:/admin/members");
    }
}
