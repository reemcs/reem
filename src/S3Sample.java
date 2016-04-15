import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import java.util.*;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class S3Sample {
	
    public static void main(String[] args) throws IOException, InterruptedException, FileNotFoundException {
    	   
    	AWSCredentials credentials = null;

        try {
        	// provide credentials to access Amazon S3 service
        	String accessKey="AKIAI7I2YNQSIZ5RRMMQ"; 
        	String secretKey="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";
            credentials = new BasicAWSCredentials(accessKey, secretKey);
          
        } catch (Exception e) {
            throw new AmazonClientException("Please Check Your Credentials"+e);
        }
       
        LinkedList link= new LinkedList();
       
        try {
        	// create Amazon S3 object and give it the proper parametrs 
        	  AmazonS3 s3 = new AmazonS3Client(credentials);
              Region usWest2 = Region.getRegion(Regions.US_WEST_2);
              s3.setRegion(usWest2);
              String bucketName = "wiki8206";

       
           System.out.println("Listing objects");
           //Inserting the files from S3 to LinkedList
           ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("v1/p"));
           for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
		          
        	   		if(objectSummary.getSize() > 0)
		        	   link.add(objectSummary.getKey());
            }
           
           int x=-1;
            for(int i=0; i<link.size();i++){
            	//get the smallest file locally 
        	  if (link.get(i).toString().contains("p000352652p000352689.7z")){
        	   x=i;
        	   String zipped=link.get(x).toString();
        	   System.out.println(zipped);//just to check the name
        	   
        	   GetObjectRequest request = new GetObjectRequest(bucketName, zipped);
        	   //Get Current Directory 
           	   String current = System.getProperty("user.dir");
               System.out.println("Current working directory in Java : " + current);
               
              
               //download  .7z file one at a time to current dir e 
               File file = new File(zipped.substring(3));
               s3.getObject(request, file);
               System.out.println(zipped.substring(3));
               //unzip 7z file
               InputStream unzip = new FileInputStream(zipped.substring(3));
               Process process2 =Runtime.getRuntime().exec("p7zip -d "+unzip);
               
               //processe 7z file  
               
               //delete files zipped and the unzipped 
               
        	   break;
        	   }
           }
           // } 
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
       
    }

}