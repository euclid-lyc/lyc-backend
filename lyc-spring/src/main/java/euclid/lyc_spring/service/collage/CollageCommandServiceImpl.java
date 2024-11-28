package euclid.lyc_spring.service.collage;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.GeneralException;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.service.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CollageCommandServiceImpl implements CollageCommandService {

    @Value("${collage.api.key}")
    private String apiKey;

    @Value("${collage.api.uri}")
    private String uri;

    private final S3ImageService s3ImageService;

    @Override
    public List<String> createBGRemovalImages(List<MultipartFile> multipartFiles) {

        // Authorization
        SecurityUtils.checkTempAuthorization();

        return multipartFiles.stream()
                .map(multipartFile -> {
                        // 배경 제거 요청
                        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                            HttpPost postRequest = new HttpPost(uri);
                            postRequest.addHeader("X-Api-Key", apiKey);

                            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                                    .addBinaryBody("image_file", multipartFile.getInputStream())
                                    .addTextBody("size", "auto");

                            postRequest.setEntity(entityBuilder.build());

                            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                                if (response.getStatusLine().getStatusCode() == 200) {
                                    // 응답 저장
                                    HttpEntity entity = response.getEntity();
                                    try {
                                        InputStream inputStream = entity.getContent();
                                        String filename = UUID.randomUUID().toString();
                                        return s3ImageService.upload(inputStream, "png", filename);
                                    } catch (IOException e) {
                                        throw new GeneralException(ErrorStatus._IO_EXCEPTION);
                                    }
                                } else {
                                    throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
                                }
                            }
                        } catch (IOException e) {
                            throw new GeneralException(ErrorStatus._IO_EXCEPTION);
                        }
                    })
                .toList();
    }
}
