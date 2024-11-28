package euclid.lyc_spring.service.s3;

import euclid.lyc_spring.dto.s3.S3DTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface S3ImageService {

    String upload(MultipartFile image);
    String upload(MultipartFile image, String filename);
    String upload(InputStream inputStream, String extension, String filename);
    void deleteImageFromS3(String imageAddress);

}
