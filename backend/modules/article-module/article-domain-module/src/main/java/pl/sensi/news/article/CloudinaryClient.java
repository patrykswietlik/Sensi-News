package pl.sensi.news.article;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryClient {

    @Value("${cloudinary.api.key}")
    private String url;

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(url);
        cloudinary.config.secure = true;
        return cloudinary;
    }
}
