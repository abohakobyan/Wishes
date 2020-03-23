package com.abo.demo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.abo.demo.usersecure.*;
import com.abo.demo.webparse.Contentimages;
import com.abo.demo.webparse.ImagesRepo;
import com.abo.demo.webparse.JsoupDownloadImages;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


@Controller
public class HomeController implements WebMvcConfigurer {

	@Autowired
	UsercontentRepo contrepo;
	@Autowired
	UserRepository userrepo;
	@Autowired
	ImagesRepo imgrepo;
	@Autowired
	JsoupDownloadImages jsDownload;
	
//	@Autowired
//	@Qualifier("newUserValidator")
//	private Validator validator;
//		
//	@InitBinder
//	   private void initBinder(WebDataBinder binder) {
//	      binder.setValidator(validator);;
//	   }
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/results").setViewName("results");
//	}
//	
		@RequestMapping(value = "/id", method = RequestMethod.GET)
	    @ResponseBody
	    public String currentUserName(Authentication authentication) {
	        return authentication.getName();
	    }
	
	
		@RequestMapping("/")
		public ModelAndView home() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserPrinciple p = (UserPrinciple) authentication.getPrincipal();
			int uid= p.getID();
			List<Contentimages> imgs = imgrepo.findByUid(uid);
			Map<String, Contentimages> modelImg = new HashMap<String,Contentimages>();
			for (Contentimages i : imgs) modelImg.put(String.valueOf(i.getCid()),i);
			ModelAndView usC = new ModelAndView();
			usC.addObject("contentImages", modelImg);
			usC.setViewName("Home.html");
			return usC;
		}
		
		@RequestMapping("/login")
		public String loginPage() {
			return "login.html";
		}
		

		@GetMapping(value = "/signup")
		public String addNuser(SignUpForm nuser) { 
		      return "form.html";
		}
		
		@ModelAttribute("signupform")
		   public SignUpForm createStudentModel() {	
		      return new SignUpForm();
		}
		
		@PostMapping(value ="/signup")
		public String signupNuser(@ModelAttribute("signUpForm")@Validated SignUpForm nuser, BindingResult bindingResult, ModelMap model) {
			if (bindingResult.hasErrors()) {
				return "form.html";
			}
			BCryptPasswordEncoder cript = new BCryptPasswordEncoder();
			users u = new users();
			u.setPass(cript.encode(nuser.getPassword1()));
			u.setUsername(nuser.getUsername());
			userrepo.save(u);
			return "form.html";
			}
	
		@PostMapping("/link")
		public ModelAndView submitLink(@ModelAttribute("link") String link) {
					
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date d = new Date();
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserPrinciple p = (UserPrinciple) authentication.getPrincipal();
			int uid= p.getID();
			
			Usercontent cont = new Usercontent();
			cont.setUid(uid);
			cont.setCid(dateFormat.format(d).toString()+uid);
			String imgT =null;
			try {
				 imgT = jsDownload.guessImgType(link);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cont.setLink(jsDownload.downloadImage(link,uid, imgT));		
			
			contrepo.save(cont);
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			List<Contentimages> imgs = imgrepo.findByUid(uid);
			Map<String, Contentimages> modelImg = new HashMap<String,Contentimages>();
			for (Contentimages i : imgs) modelImg.put(String.valueOf(i.getCid()),i);
			ModelAndView usC = new ModelAndView();
			usC.addObject("contentImages", modelImg);
			usC.setViewName("Home.html");

				
			return usC;
		}
		
		
		
		
		
		
		@RequestMapping("/logout-success")
		public String logoutPage() {
			return "logout.html";
		}
}
