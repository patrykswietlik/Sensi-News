package pl.sensi.news.article;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sensi.news.article.api.ImageStorage;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ImageStorageImpl implements ImageStorage {

    private final Cloudinary cloudinary;

    @Override
    public String storeImage(byte[] image) {
        try {
            var response = cloudinary.uploader().upload(image, Map.of());

            if (Objects.isNull(response) || !response.containsKey("secure_url")) {
                throw new RuntimeException();
            }

            return (String) response.get("secure_url");

        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

}
