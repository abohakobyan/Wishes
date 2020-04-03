package com.abo.demo.webparse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException; 
import java.net.URL; 


@Component
public class JsoupDownloadImages  {		
	
	@Autowired
	ImagesRepo ImgRepo;
	boolean alreadyExecuted=false;
	
//	JsoupDownloadImages(){
//// 		if(!alreadyExecuted) {
////		 URL.setURLStreamHandlerFactory( protocol ->
////      	protocol.equals( "http" ) ? new HttpHandler() :
////      	protocol.equals( "https" ) ? new HttpsHandler() : null );
////		 launch(  );
////		 alreadyExecuted=true;}
//	}
	
	
	    //private static String IMAGE_DESTINATION_FOLDER = "\\src\\main\\resources\\static\\photos";
	    //Parses The Website and pulls items
	    
		public ArrayList<String> ParseLink(String link) throws IOException {
	    
//	    	String[]as = new String[10];
//	    	as[0]=link;
//	    	NetworkMonitor.main(as);
//	    	Runtime rt = Runtime.getRuntime();
//	   
//	    	Process pr = rt.exec("java -cp net2.jar NetworkMonitor" + link);
//	    	try {
//				TimeUnit.SECONDS.sleep(5);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	    	ArrayList<String>a = new ArrayList<String>();
//	    	BufferedReader stdInput = new BufferedReader(new 
//	    		     InputStreamReader(pr.getInputStream()));
//	    	String s = null;
//	    	while ((s = stdInput.readLine()) != null) 
//	    			a.add(s);
	    	
	    	
	    	
	    
	    	
	        //replace it with your URL 
	        String strURL = link;
	        Document document;
	        //connect to the website and get the document
	        try {
	        document = Jsoup
	                .connect(strURL)
	                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
	                .timeout(30000)
	                .get();
	        }catch(IOException a) {
	        	try {
	    	        document = Jsoup
	    	                .connect(strURL)
	    	                .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
	    	                .timeout(30000)
	    	                .get();
	    	      }catch(IOException q) {
	    	    	  throw(q);
	    	      }
		      }
	      
	        
	        
	        
	        //select all img tags
	        Elements imageElements = document.getElementsByTag("img");
	        ArrayList<String> imgLinks= new ArrayList<String>();
	        //iterate over each image
	        if(document.title().length()>50)
	        	imgLinks.add(document.title().substring(0, 50));
	        else
	        	imgLinks.add(document.title());
	        for(Element imageElement : imageElements){
	            
	            //make sure to get the absolute URL using abs: prefix
	            String strImageURL = imageElement.attr("abs:src");
	            
	            //download image one by one
	            imgLinks.add(strImageURL);
	           
	            
	        }
	        return imgLinks;
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

