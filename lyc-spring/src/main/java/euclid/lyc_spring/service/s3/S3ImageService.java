package euclid.lyc_spring.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.S3Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.default-profile}")
    private String defaultProfile;

    public String uploadImage(String dir, MultipartFile image) {
        // image가 비어있으면 오류
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new S3Handler(ErrorStatus._FILE_IS_NULL);
        }
        return uploadImageWithPath(dir, image);
    }

    public String uploadImageByStream(InputStream is, String extension, String s3FileName) {
        uploadStreamToS3(is, extension, s3FileName);
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    private String uploadImageWithPath(String dir, MultipartFile image) {
        // image가 비어있으면 오류
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new S3Handler(ErrorStatus._FILE_IS_NULL);
        }
        String originalFilename = image.getOriginalFilename(); // 원본 파일명
        String extension = validateImageFileExtention(originalFilename);

        try {
            String s3FileName = dir + UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명
            InputStream is = image.getInputStream();
            uploadStreamToS3(is, extension, s3FileName);
            return amazonS3.getUrl(bucketName, s3FileName).toString();
        } catch (IOException e) {
            throw new S3Handler(ErrorStatus._IO_EXCEPTION);
        }
    }

    private static String validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Handler(ErrorStatus._BAD_FILE_EXTENSION);
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extension)) {
            throw new S3Handler(ErrorStatus._BAD_FILE_EXTENSION);
        }
        return extension;
    }

    public void uploadStreamToS3(InputStream is, String extension, String s3FileName) {
        try {
            byte[] bytes = IOUtils.toByteArray(is);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            try {
                PutObjectRequest putObjectRequest =
                        new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3.putObject(putObjectRequest);
            } catch (Exception e) {
                throw new S3Handler(ErrorStatus._PUT_OBJECT_EXCEPTION);
            } finally {
                byteArrayInputStream.close();
                is.close();
            }
        } catch (IOException e) {
            throw new S3Handler(ErrorStatus._IO_EXCEPTION);
        }
    }

    public void deleteImageFromS3(String imageAddress) {
        if (imageAddress == null || imageAddress.isEmpty()) {
            return ;
        }
        if (imageAddress.equals(defaultProfile)) {
            return ;
        }
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new S3Handler(ErrorStatus._IO_EXCEPTION);
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new S3Handler(ErrorStatus._IO_EXCEPTION);
        }
    }

}
