package opentalent.configuracion;

import org.springframework.context.annotation.Configuration;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	   
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }
    
    @Bean
    HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        return converter;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }
}
