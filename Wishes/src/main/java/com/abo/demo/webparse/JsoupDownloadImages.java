package com.abo.demo.webparse;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abo.demo.usersecure.UserRepository;


@Component
public class JsoupDownloadImages {		
	
	@Autowired
	ImagesRepo ImgRepo;
	
	
	
	    //private static String IMAGE_DESTINATION_FOLDER = "\\src\\main\\resources\\static\\photos";
	    
	    public String ParseLink(String link, int uid, String imgT) throws IOException {
	        
	        //replace it with your URL 
	        String strURL = link;
	        
	        //connect to the website and get the document
	        Document document = Jsoup
	                .connect(strURL)
	                .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
	                .timeout(10 * 1000)
	                .get();
	        
	        //select all img tags
	        Elements imageElements = document.select("img");
	        
	        //iterate over each image
	        for(Element imageElement : imageElements){
	            
	            //make sure to get the absolute URL using abs: prefix
	            String strImageURL = imageElement.attr("abs:src");
	            
	            //download image one by one
	            
	            return(downloadImage(strImageURL,uid, imgT));
	            
	        }
	        return "notsaved";
	    }
	    
	    public  String downloadImage(String strImageURL,int uid ,String imgT){
	        Path absolutePath=Paths.get(".");
	        Path path = Paths.get(absolutePath+ "/src/main/resources/static/photos/");
	        //get file name from image path
	        String strImageName = 
	                strImageURL.substring( strImageURL.lastIndexOf("\\") + 1 );
	        
	        System.out.println("Saving: " + strImageName + ", from: " + strImageURL);
	        
	        
	        
	        
	        try {
	            
	            //open the stream from URL
	            URL urlImage = new URL(strImageURL);
	            InputStream in = urlImage.openStream();
	            strImageName = strImageName.replaceAll("\\W+", "");
	            byte[] buffer = new byte[4096];
	            int n = -1;
	            String fname = path + "\\"+ strImageName+ "." + imgT;
	            OutputStream os = 
	                new FileOutputStream( fname );
	            
	            //write bytes to the output stream
	            while ( (n = in.read(buffer)) != -1 ){
	                os.write(buffer, 0, n);
	            }
	            Contentimages conimg= new Contentimages();
	            conimg.setImgpath(strImageName+ "." +imgT);
	            conimg.setUid(uid);
	            ImgRepo.save(conimg);
	            //close the stream
	            os.close();
	            
	            return strImageName + "." + imgT;
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	       return "notsaved";
	    }
	    public String guessImgType(String imgUrl) throws IOException {
	    	// URLConnection.guessContentTypeFromStream only needs the first 12 bytes, but
	    	// just to be safe from future java api enhancements, we'll use a larger number
	    	int pushbackLimit = 100;
	    	URL url = new URL(imgUrl);
	    	InputStream urlStream = url.openStream();
	    	PushbackInputStream pushUrlStream = new PushbackInputStream(urlStream, pushbackLimit);
	    	byte [] firstBytes = new byte[pushbackLimit];
	    	// download the first initial bytes into a byte array, which we will later pass to 
	    	// URLConnection.guessContentTypeFromStream  
	    	pushUrlStream.read(firstBytes);
	    	// push the bytes back onto the PushbackInputStream so that the stream can be read 
	    	// by ImageIO reader in its entirety
	    	pushUrlStream.unread(firstBytes);

	    	String imageType = null;
	    	// Pass the initial bytes to URLConnection.guessContentTypeFromStream in the form of a
	    	// ByteArrayInputStream, which is mark supported.
	    	ByteArrayInputStream bais = new ByteArrayInputStream(firstBytes);
	    	String mimeType = URLConnection.guessContentTypeFromStream(bais);
	    	if (mimeType.startsWith("image/"))
	    	    imageType = mimeType.substring("image/".length());
	    	else
	    		throw new IOException();
	    	// else handle failure here

	    	// read in image
	    	BufferedImage inputImage = ImageIO.read(pushUrlStream);
	    	return imageType;
	    }
	    
	    
	    
	    
	    
	    
	}

