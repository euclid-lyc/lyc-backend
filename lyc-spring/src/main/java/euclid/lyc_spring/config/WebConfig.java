package euclid.lyc_spring.config;

import euclid.lyc_spring.converter.MultipartHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private MultipartHttpMessageConverter multipartHttpMessageConverter;

    @Autowired
    public WebConfig(MultipartHttpMessageConverter multipartHttpMessageConverter) {
        this.multipartHttpMessageConverter = multipartHttpMessageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(multipartHttpMessageConverter);
    }
}