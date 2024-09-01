package euclid.lyc_spring.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.S3Handler;
import euclid.lyc_spring.dto.s3.S3DTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Component
public class S3ImageServiceImpl implements S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public List<String> uploadImagesToBucket(List<MultipartFile> images) {

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageUrl = this.upload(image);
            imageUrls.add(imageUrl);
        }
        if (imageUrls.isEmpty()) {
            throw new S3Handler(ErrorStatus._PUT_OBJECT_EXCEPTION);
        }

        return imageUrls;
    }

    @Override
    public String upload(MultipartFile image) {
        // image가 비어있으면 오류
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new S3Handler(ErrorStatus._FILE_IS_NULL);
        }
        return this.uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        // image가 비어있으면 오류
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new S3Handler(ErrorStatus._FILE_IS_NULL);
        }

        this.validateImageFileExtention(image.getOriginalFilename());

        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new S3Handler(ErrorStatus._IO_EXCEPTION);
        }
    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Handler(ErrorStatus._BAD_FILE_EXTENSION);
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new S3Handler(ErrorStatus._BAD_FILE_EXTENSION);
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            extention = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); //확장자 명
        }

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        }catch (Exception e){
            throw new S3Handler(ErrorStatus._PUT_OBJECT_EXCEPTION);
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    @Override
    public void deleteImageFromS3(String imageAddress) {
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