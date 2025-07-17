package library;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.ObjectCannedAclProvider;
import com.amazonaws.services.s3.transfer.Transfer.TransferState;

import automationEngine.ApplicationSetup;
import utilities.CommonUtilities;
import utilities.ConstantVariables;

import com.amazonaws.services.s3.transfer.TransferManager;



public class AmazonS3Client extends ApplicationSetup{

	private AmazonS3 s3client;
	String errorMessage = "Method Execution Failed";
	static String currentDir = System.getProperty("user.dir");
	String clientRegion = "*** Client region ***";
	String bucketName = "RSPL_Bucket_name";
	String keyName;
	String filePath = "*** Path to file to upload ***";
	String pSyncName ="RSPL_testReports/";
	private static final String SOURCE_EXTENT_FOLDER = currentDir + "/" +"ExtentReports";
	private static final String SOURCE_EXTENT_SCREENSHOT_FOLDER = currentDir + "/" + "ExtentReports/ScreenShots";
	public static String fileObjectUrl;
	boolean extentReportsFlag = Boolean.parseBoolean(SetObjectProperties.envProp.getPropertyValue(ConstantVariables.EXTENT_REPORT_FLAG));
	

	public AmazonS3Client() {
		
		s3client = AmazonS3ClientBuilder.standard()
				.withRegion(com.amazonaws.services.s3.model.Region.EU_Ireland.toString())
				.withCredentials(new AWSStaticCredentialsProvider(getAWSCredentialsProvider("INT"))).build();

	}

	@SuppressWarnings("deprecation")
	public void uploadLatestReportsFoldertoS3Bucket() {
		if(extentReportsFlag) {
			uploadLatestExtentReportFoldertoS3Bucket();
		}		
	}
	public static AWSCredentials getAWSCredentialsProvider(String environment) {
		AWSCredentials credential = null;
		String accessKey = System.getenv("AWSAccessKey");// NOSONAR
		String secretKey = System.getenv("AWSSecretKey");// NOSONAR

		if (!(accessKey != null && secretKey != null)) {
			accessKey = SetObjectProperties.envProp.getPropertyValue("AWSAccessKey");
			secretKey = SetObjectProperties.envProp.getPropertyValue("AWSSecretKey");
		}

		if (environment.equalsIgnoreCase("INT")) {
			credential = new BasicAWSCredentials(accessKey, secretKey);
			return credential;
		}
		return credential;
	}
	/**
	 * @author Abirami
	 * @purpose To upload AllocationSync Extent Reports/Screenshot folder on AWS S3 Bucket
	 */
	@SuppressWarnings("deprecation")
	public void uploadLatestExtentReportFoldertoS3Bucket() {
		try {
			File extentReportDirectory = new File(SOURCE_EXTENT_SCREENSHOT_FOLDER);
			TransferManager xfer_mgr = new TransferManager(s3client);
			TransferState xfer_state = null;
			try {
				ObjectCannedAclProvider cannedAclProvider = new ObjectCannedAclProvider() {
					public CannedAccessControlList provideObjectCannedAcl(File file) {
						return CannedAccessControlList.PublicRead;
					}
				};
				String tempName = pSyncName + "ExtentReports/ScreenShots";
				System.out.println(tempName);
				MultipleFileUpload xfer = xfer_mgr.uploadDirectory(bucketName, tempName,
						extentReportDirectory, true, null, null, cannedAclProvider);
				do {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) { 
						Thread.currentThread().interrupt();
						return;
					}
				} while (xfer.isDone() == false);
				xfer_state = xfer.getState();
				System.out.println("Uploading status : " + xfer_state);
			} catch (AmazonServiceException e) {
				System.err.println(e.getErrorMessage());
				System.exit(1);
			}
			if (!xfer_state.toString().equalsIgnoreCase("Failed") || !xfer_state.toString().contains("Failed")) {
				String finalReport = uploadExtentReporttoS3Bucket();
				System.out.println(finalReport);
				String indexFile = pSyncName  + extentReportDirectory.getName();
				System.out.println(indexFile + "\nfinalReport : "+indexFile);
				fileObjectUrl = s3client.getUrl(bucketName, indexFile).toString();
				
			} else {
				fileObjectUrl = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * @author Abirami
	 * @purpose To upload extent report to S3 Bucket
	 * @return
	 */
	public String uploadExtentReporttoS3Bucket() {
		try {
			String reportsFilePath = FileHandlingUtilities.lastFileModified(SOURCE_EXTENT_FOLDER);
			File file = new File(reportsFilePath);
			System.out.println(bucketName);
			fileObjectUrl = uploadFileToS3Bucket(bucketName, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileObjectUrl;
	}
	
	
	/**
	 * @purpose To upload file to S3 Bucket
	 * @param bucketName
	 * @param file
	 * @return
	 */
	private String uploadFileToS3Bucket(final String bucketName, final File file) {
		final String uniqueFileName = pSyncName + "/ExtentReports/" + file.getName();
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead);
		s3client.putObject(putObjectRequest);
		return s3client.getUrl(bucketName, uniqueFileName).toString();
	}
	
	
	/**
	 * @Author Abirami
	 * @Purpose To Download File from S3 Bucket
	 * @Note File will be downloaded to Download Folder
	 * @param bucketName
	 * @param fileName
	 * @return localFile
	 * @throws Throwable
	 */
	public File downloadFileFromS3(String bucketName, String fileName) {
		CommonUtilities.deletDownLoadFile();
		String downloadpath = currentDir + "/Download";
		File localFile = new File(downloadpath + "/" + fileName);
		s3client.getObject(new GetObjectRequest(bucketName, fileName), localFile);
		return localFile;
	}
	
}