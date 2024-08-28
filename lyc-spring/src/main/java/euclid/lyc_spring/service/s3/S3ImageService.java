package euclid.lyc_spring.service.s3;

import euclid.lyc_spring.dto.s3.S3DTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3ImageService {

    S3DTO.ImageListDTO uploadImagesToBucket(List<MultipartFile> images);
    String upload(MultipartFile image);
    void deleteImageFromS3(String imageAddress);

}
