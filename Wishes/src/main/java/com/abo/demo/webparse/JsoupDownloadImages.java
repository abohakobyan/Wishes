package com.abo.demo.webparse;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class JsoupDownloadImages {		
	
	@Autowired
	ImagesRepo ImgRepo;
	
	
	
	    //private static String IMAGE_DESTINATION_FOLDER = "\\src\\main\\resources\\static\\photos";
	    //Parses The Website and pulls items
	    public String ParseLink(String link, String Cid, String imgT,String title, String mmlink) throws IOException {
	        
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
	            
	            return(downloadImage(strImageURL,Cid, imgT,title,mmlink));
	            
	        }
	        return "notsaved";
	    }
	    
	    public  String downloadImage(String strImageURL,String cid ,String imgT, String title, String link){
	    	if(strImageURL!="itemplaceholder.png") {
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
	            
	          
	          //saving image name to database
	            Contentimages conimg= new Contentimages();
	            conimg.setImgpath(strImageName+ "." +imgT);
	            conimg.setCid(cid);
	            conimg.setTitle(title);
	            conimg.setLink(link);
	            ImgRepo.save(conimg);
	            //close the stream
	            os.close();
	            
	            return strImageName + "." + imgT;
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    	}else {
	    	
	        Contentimages conimg= new Contentimages();
	        conimg.setImgpath(strImageURL);
	        conimg.setCid(cid);
            conimg.setTitle(title);
            conimg.setLink(link);
            ImgRepo.save(conimg);
            return strImageURL;
	    	}
	       return "notsaved";
	    }
	    public  void getImage(MultipartFile file,String cid , String title, String link){
	    	Path absolutePath=Paths.get(".");
	        Path path = Paths.get(absolutePath+ "/src/main/resources/static/photos/");
	    	try {
				byte[] bytes = file.getBytes();
				String fname = path +  "\\"+ link.replaceAll("\\W+", "") +file.getOriginalFilename();
				path = Paths.get(fname);
				   Files.write(path,bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Contentimages conimg= new Contentimages();
	        conimg.setImgpath(link.replaceAll("\\W+", "") + file.getOriginalFilename());
	        conimg.setCid(cid);
            conimg.setTitle(title);
            conimg.setLink(link);
            ImgRepo.save(conimg);
	    	
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
	    	//BufferedImage inputImage = ImageIO.read(pushUrlStream);
	    	return imageType;
	    }
	    
	    
	    
	    
	    
	    
	}

