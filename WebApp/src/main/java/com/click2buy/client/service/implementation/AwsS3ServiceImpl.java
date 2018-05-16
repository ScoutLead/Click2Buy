package com.click2buy.client.service.implementation;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.click2buy.client.service.AwsS3Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service("awsService")
public class AwsS3ServiceImpl implements AwsS3Service {

  private static final String CONTENT_TYPE = "Content-Type: {}";

  private String s3Bucket;

  private String accessKey;

  private String secretKey;

  private String region;

  private final Logger log = LoggerFactory.getLogger(this.getClass());


  private AmazonS3 amazonS3;

  @Autowired
  public AwsS3ServiceImpl(Environment env) {
    accessKey = env.getProperty("aws.access.key");
    secretKey = env.getProperty("aws.secret.key");
    s3Bucket = env.getProperty("aws.s3.bucket");
    region = env.getProperty("aws.secret.region");

    if ((accessKey == null) || (secretKey == null)) {
      log.error("AWS credentials are not initialized");
    }
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    amazonS3 = AmazonS3ClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
      .withRegion(Regions.fromName(region))
      .build();

    if (!amazonS3.doesBucketExistV2(s3Bucket)) {
      amazonS3.createBucket(s3Bucket);
    }

  }

  @Override
  public boolean upload(String key, InputStream inputStream) {
    try {

      byte[] content = IOUtils.toByteArray(inputStream);
      byte[] md5 = DigestUtils.md5(content);
      String streamMD5 = new String(Base64.encodeBase64(md5));

      ObjectMetadata metaData = new ObjectMetadata();
      metaData.setContentMD5(streamMD5);
      metaData.setContentLength(content.length);

      PutObjectRequest putObjectRequest = new PutObjectRequest(s3Bucket, key,
        inputStream, metaData);
      putObjectRequest.withCannedAcl(CannedAccessControlList.Private); // private
     // putObjectRequest.withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(kmsKeyId));

      amazonS3.putObject(putObjectRequest); // upload file
      log.info("upload object with key {} successfully ", key);
      return true;
    } catch (AmazonServiceException ase) {
      logAWSServiceException(ase);
    } catch (AmazonClientException ace) {
      log.error(ace.getMessage(), ace);
    } catch (IOException e) {
      log.error("IOException happened during uploading data to AWS, reason:", e);
    }
    return false;

  }

  @Override
  public Optional<byte[]> download(String key) {
    log.info("Retrieving object with key: {} ", key);
    S3Object s3object = amazonS3.getObject(new GetObjectRequest(
      s3Bucket, key));
    log.debug(CONTENT_TYPE,
      s3object.getObjectMetadata().getContentType());
    try {
      return Optional.of(retrieveObject(s3object.getObjectContent()));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  private byte[] retrieveObject(S3ObjectInputStream objectContent) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int nRead;
      byte[] data = new byte[16384];
      while ((nRead = objectContent.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return buffer.toByteArray();
  }

  private void logAWSServiceException(AmazonServiceException ase) {
    log.error("Caught an AmazonServiceException, which" +
      " means your request made it " +
      "to Amazon S3, but was rejected with an error response" +
      " for some reason.", ase);
    log.error(String.format("Error Message:    %s", ase.getMessage()));
    log.error(String.format("HTTP Status Code: %d", ase.getStatusCode()));
    log.error(String.format("AWS Error Code:   %s", ase.getErrorCode()));
    log.error(String.format("Error Type:       %s", ase.getErrorType()));
    log.error(String.format("Request ID:       %s", ase.getRequestId()));
  }


}
