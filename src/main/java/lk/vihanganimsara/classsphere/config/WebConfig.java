package lk.vihanganimsara.classsphere.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Photos
        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:E:/Advanced API Development/SpringBOOT/ClassSphere/photos/");

        // QR Codes
        registry.addResourceHandler("/qrcodes/**")
                .addResourceLocations("file:E:/Advanced API Development/SpringBOOT/ClassSphere/qrcodes/");
    }
}

