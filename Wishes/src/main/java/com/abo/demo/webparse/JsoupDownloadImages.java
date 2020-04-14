package com.abo.demo.webparse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.abo.demo.lists.UsercontentRepo;

import java.io.IOException; 
import java.net.URL; 


@Component
public class JsoupDownloadImages  {		
	
	@Autowired
	ImagesRepo imgrepo;
	
		public void writeImageToRespose(int id, HttpServletResponse response) throws IOException {
	        //store image in browser cache
	        response.setContentType("image/jpeg, image/jpg, image/png");
	        response.setHeader("Cache-Control", "max-age=2628000");
	        Optional<Contentimages> c = imgrepo.findById(id);	       
	        try (OutputStream out = response.getOutputStream()) {
	        	if(c.get().getImg_data() != null)
	        		out.write(c.get().getImg_data());
	         
	        }
	}
	
	
	
		public ArrayList<String> ParseLink(String link) throws IOException {
	        String strURL = link;
	        Document document = null;
	        //connect to the website and get the document
	        ArrayList<String> res= new ArrayList<String>();
	       
	        try {
	        document = Jsoup
	                .connect(strURL)
	                .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
	                .timeout(30000)
	                .get();
	        }catch(IOException a) {
	        	try {
	    	        document = Jsoup
	    	                .connect(strURL)
	    	                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
	    	                .timeout(30000)
	    	                .get();
	    	      }catch(IOException q) {
	    	    	  
	    	      }
		      }

//	        //select all img tags

//	        ArrayList<String> imgLinks= new ArrayList<String>();
//	        //iterate over each image
	        if(document!=null) {
	        if(document.title().length()>50)
	        	res.add(document.title().substring(0, 50));
	        else
	        	res.add(document.title());}
	        /*activate in case server is overwhelmed changing the bottom if statement to 10 or more */
	        Elements imageElements = document.getElementsByTag("img");
	        for(Element imageElement : imageElements){
	            
	            //make sure to get the absolute URL using abs: prefix
	            String strImageURL = imageElement.attr("abs:src");
	            
	            //download image one by one
	            if(!strImageURL.isEmpty())
	            res.add(strImageURL);
	           
	            
	        }
	        if(res.size()<10) {
	        Process proc = Runtime.getRuntime().exec("java -jar com.abo.urlh-0.0.1-SNAPSHOT.jar " + "\"" +link + "\"");
		       //Process proc = Runtime.getRuntime().exec(new String[] { "bash", "-c", "echo hello > hello.txt" });
		        InputStream in = proc.getInputStream();
		        InputStream err = proc.getErrorStream();

		        String line;
		        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		        while ((line=buffer.readLine()) != null) {
		            res.add(line);
		        }
//			       String erro = new BufferedReader(new InputStreamReader(err))
//      		  .lines().collect(Collectors.joining("\n"));
//      		  System.out.print(erro);
	        }
	        
	        
	        
	        
	        
	        
	        
	        return res;
	    }
	    
	    public  void downloadImage(String strImageURL,String cid ,String imgT, String title, String link){
	    	if(strImageURL!="itemplaceholder.png") {

	        try {
	            
	            //open the stream from URL
	            URL urlImage = new URL(strImageURL);
	            URLConnection con = urlImage.openConnection();
	            		con.setRequestProperty("User-Agent", "image/jpeg, image/jpg, image/png");
	            		InputStream in = con.getInputStream();

	            ByteArrayOutputStream output = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int count;
	            while ((count = in.read(buffer)) != -1)
	                output.write(buffer, 0, count);
	            byte[] contents = output.toByteArray();

	            Contentimages conimg= new Contentimages();
	            conimg.setImg_data(contents);
	            conimg.setCid(cid);
	            conimg.setTitle(title);
	            conimg.setLink(link);
	            imgrepo.save(conimg);
	      
	            
	           
	            
	        } catch (IOException e) {
	        	Contentimages conimg= new Contentimages();
		        
		        conimg.setCid(cid);
	            conimg.setTitle(title);
	            conimg.setLink(link);
	            imgrepo.save(conimg);
	            
	        }
	    	}else {
	    	
	      Contentimages conimg= new Contentimages();
	        
	        conimg.setCid(cid);
            conimg.setTitle(title);
            conimg.setLink(link);
            imgrepo.save(conimg);
            
	    	}
	       
	    }
	    public  void getImage(MultipartFile file,String cid , String title, String link){

	        byte[] in = null;
	    	try {
	    		in = file.getBytes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Contentimages conimg= new Contentimages();
	      //  conimg.setImgpath(link.replaceAll("\\W+", "") + file.getOriginalFilename());
	    	conimg.setImg_data(in);
	        conimg.setCid(cid);
            conimg.setTitle(title);
            conimg.setLink(link);
            imgrepo.save(conimg);
	    	
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

