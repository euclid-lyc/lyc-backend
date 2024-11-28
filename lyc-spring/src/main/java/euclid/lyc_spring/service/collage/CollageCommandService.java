package euclid.lyc_spring.service.collage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CollageCommandService {

    List<String> createBGRemovalImages(List<MultipartFile> multipartFiles);

    String createCollage(MultipartFile multipartFile);
}
