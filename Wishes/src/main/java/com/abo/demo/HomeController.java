package com.abo.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		

		@RequestMapping(value = "/id", method = RequestMethod.GET)
	    @ResponseBody
	    public String currentUserName(Authentication authentication) {
	        return authentication.getName();
	    }
		@RequestMapping("/")
		public String index() {
			return "index.html";
		}
		public String gettingID() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserPrinciple p = null;
			OAuth2AuthenticationToken oauthToken;
			String uid = null;
			if(authentication.getPrincipal() instanceof UserPrinciple) {
				 p = (UserPrinciple) authentication.getPrincipal();
				 uid= p.getID();
			
			}else {
				oauthToken =(OAuth2AuthenticationToken) authentication;
				uid = oauthToken.getPrincipal().getAttribute("sub");
			}
			return uid;
		}
		
		
		@RequestMapping("/home")
		public ModelAndView home() {
			String uid = this.gettingID();
			
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
		@RequestMapping("/login-error.html")
		  public String loginError(Model model) {
		    model.addAttribute("loginError", true);
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
			return "signupsuccess.html";
			}
	
		@RequestMapping("/signups")
		public String signupSuccess() {
		return "signupsuccess.html";
		}
		
		//Takes the list name and makes a new list 
		@PostMapping("/newlist")
		public String submitLink(@ModelAttribute("listName") String listName, @ModelAttribute("privateorpublic") String priv) {
			
			
			String uid = this.gettingID();
			Usercontent cont = new Usercontent();
			if(priv.equals("false")) cont.setPriv(false);
			else cont.setPriv(true);
			cont.setUid(uid);
			String uniqueID = UUID.randomUUID().toString();
			cont.setListtitle(listName);
			cont.setCid(uniqueID);
			contrepo.save(cont);
			return "redirect:/home";
		}
		
		//Populates the list page based on listID
		@GetMapping("/list")
		public ModelAndView list(ModelAndView modelAndView,@ModelAttribute("Cid") String listID, @ModelAttribute("listtitle") String listtitle) {
		
			String uid= this.gettingID();
			Optional<Usercontent> valList = contrepo.findById(listID);
			if(valList.get().getUid().equals(uid) || !valList.get().isPriv()) {
			List<Contentimages> imgs = imgrepo.findByCid(listID);
			
			
			Map<String, Contentimages> modelImg = new HashMap<String,Contentimages>();
			for (Contentimages i : imgs) modelImg.put(String.valueOf(i.getImg_id()),i);
			//ModelAndView usC = new ModelAndView();
			modelAndView.addObject("listtitle", listtitle);
			modelAndView.addObject("contentImages", modelImg);
			modelAndView.setViewName("list.html");
			modelAndView.addObject("listID", listID);
			return modelAndView;
			}
			modelAndView.setViewName("redirect:/home");
			return modelAndView;
		}
		//Upload form 
		@PostMapping("/newItem")
		public String submitItem(@ModelAttribute("listID") String listID,@ModelAttribute("Contentimages") Contentimages imageLink,
				@RequestParam("file") MultipartFile file) {
			
			String imgT =null;
			//If no link to an image
			if(!imageLink.getImgpath().isEmpty()) {
				jsDownload.downloadImage(imageLink.getImgpath(),listID, imgT,imageLink.getTitle(),imageLink.getLink());	
			}
			//If no file uploaded
			else if(!file.isEmpty()){
				jsDownload.getImage(file,listID,imageLink.getTitle(),imageLink.getLink());		
			}
			else {
				jsDownload.downloadImage("itemplaceholder.png" ,listID, imgT,imageLink.getTitle(),imageLink.getLink());	
			}
			
			return "redirect:/list?Cid=" + listID;
		
		
		}
		//Java script edited form handles automatic image population of a selection page
		@PostMapping("/selectImg")
		public ModelAndView selectImg(@ModelAttribute("listID") String listID,@ModelAttribute("link") String link,ModelAndView modelAndView,@ModelAttribute("title") String title){
			ArrayList<String> l = new ArrayList<String>();
			try {
					l = jsDownload.ParseLink(link);
				} catch (IOException e) {
					Contentimages c = new Contentimages();
					c.setLink(link);
					c.setImgpath("itemplaceholder.png");
					String a = submitItem(listID,c);
					modelAndView.setViewName(a);
					return modelAndView;
				}
				if(title.isEmpty())
				modelAndView.addObject("title", l.get(0));
				else modelAndView.addObject("title", title);
				l.remove(0);
				//ModelAndView modelAndView = new ModelAndView();
				modelAndView.addObject("contLink", link);
				//ModelAndView usC = new ModelAndView();
				modelAndView.addObject("Images", l);
				modelAndView.setViewName("selectImg.html");
				modelAndView.addObject("listID", listID);
				
			return modelAndView;
		}
		
		public String submitItem(@ModelAttribute("listID") String listID,@ModelAttribute("Contentimages") Contentimages imageLink) {
			jsDownload.downloadImage("itemplaceholder.png" ,listID, null,imageLink.getTitle(),imageLink.getLink());
			return "redirect:/list?Cid=" + listID;
		}
		
		@GetMapping("/newItem")
		public String submitItemFromList(@ModelAttribute("listID") String listID,@ModelAttribute("Contentimages") Contentimages imageLink) {
			String imgT =null;
			jsDownload.downloadImage(imageLink.getImgpath(),listID, imgT,imageLink.getTitle(),imageLink.getLink());
			return "redirect:/list?Cid=" + listID;
		}
			
		@GetMapping("/deleteimg")
		public String removeImg(@ModelAttribute("img_id") int img_id, @ModelAttribute("listID") String listID) {
			imgrepo.deleteById(img_id);
			return "back2.html";
		}
		
		@GetMapping("/deletelist")
		@Transactional
		public String removeList(@ModelAttribute("listID") String listID) {
			imgrepo.deleteByCid(listID);
			contrepo.deleteById(listID);
			return "redirect:/home";
		}
		
		@GetMapping("/img/{id}")
		public void getImage(@PathVariable("id") Integer id, HttpServletResponse response) {
			try {
				jsDownload.writeImageToRespose(id, response);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		@RequestMapping("/logout-success")
		public String logoutPage() {
			return "signupsuccess.html";
		}
}
