package com.abo.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.abo.demo.lists.Usercontent;
import com.abo.demo.lists.UsercontentRepo;
import com.abo.demo.usersecure.SignUpForm;
import com.abo.demo.usersecure.UserPrinciple;
import com.abo.demo.usersecure.UserRepository;
import com.abo.demo.usersecure.users;
import com.abo.demo.webparse.Contentimages;
import com.abo.demo.webparse.ImagesRepo;
import com.abo.demo.webparse.JsoupDownloadImages;

import org.springframework.web.multipart.MultipartFile;;
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
			List<Usercontent> lists = contrepo.findByUid(uid);
			Map<String, Usercontent> modelLists = new HashMap<String,Usercontent>();
			for (Usercontent i : lists) modelLists.put(String.valueOf(i.getCid()),i);
			ModelAndView usC = new ModelAndView();
			usC.addObject("contentLists", modelLists);
			usC.setViewName("Home.html");
			return usC;
		}
		

		@GetMapping("/login")
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
	
		
		
		
		
		@PostMapping("/newlist")
		public String submitLink(@ModelAttribute("listName") String listName) {
				
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserPrinciple p = (UserPrinciple) authentication.getPrincipal();
			int uid= p.getID();
			
			Usercontent cont = new Usercontent();
			cont.setUid(uid);
			String uniqueID = UUID.randomUUID().toString();
			cont.setListtitle(listName);
			cont.setCid(uniqueID);
			contrepo.save(cont);
			return "redirect:/";
		}
		
		
		@GetMapping("/list")
		public ModelAndView list(ModelAndView modelAndView,@ModelAttribute("Cid") String listID) {
			List<Contentimages> imgs = imgrepo.findByCid(listID);
			Map<String, Contentimages> modelImg = new HashMap<String,Contentimages>();
			for (Contentimages i : imgs) modelImg.put(String.valueOf(i.getImg_id()),i);
			//ModelAndView usC = new ModelAndView();
			modelAndView.addObject("contentImages", modelImg);
			modelAndView.setViewName("list.html");
			modelAndView.addObject("listID", listID);
			
			return modelAndView;
			
		}
		@PostMapping("/newItem")
		public String submitItem(@ModelAttribute("listID") String listID,@ModelAttribute("Contentimages") Contentimages imageLink,
				@RequestParam("file") MultipartFile file) {
			
			String imgT =null;
			if(!imageLink.getImgpath().isEmpty()) {
				try {
				
					imgT = jsDownload.guessImgType(imageLink.getImgpath());
					} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				jsDownload.downloadImage(imageLink.getImgpath(),listID, imgT,imageLink.getTitle(),imageLink.getLink());	
			}else if(!file.isEmpty()){
				jsDownload.getImage(file,listID,imageLink.getTitle(),imageLink.getLink());	
			}else {
				jsDownload.downloadImage("itemplaceholder.png" ,listID, imgT,imageLink.getTitle(),imageLink.getLink());	
			}
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/list?Cid=" + listID;
		
		
		}
		@GetMapping("/deleteimg")
		public String removeImg(@ModelAttribute("img_id") int img_id,@ModelAttribute("listID") String listID) {
			imgrepo.deleteById(img_id);
			return "redirect:/list?Cid=" + listID;
		}
		@GetMapping("/deletelist")
		@Transactional
		public String removeList(@ModelAttribute("listID") String listID) {
			imgrepo.deleteByCid(listID);
			contrepo.deleteById(listID);
			return "redirect:/";
		}
		
		
		
		@RequestMapping("/logout-success")
		public String logoutPage() {
			return "logout.html";
		}
}
