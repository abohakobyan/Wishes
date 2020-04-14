package com.abo.demo.usersecure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



public class CustomUserDetailsService implements UserDetailsService  {
	@Autowired
	UserRepository userrepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 users domainUser = userrepo.findByUsername(username);

		 	UserPrinciple customUserDetail=new UserPrinciple(domainUser);
	     
	        return customUserDetail;
		
	}

}
