package com.abo.demo.usersecure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class AppSecure extends WebSecurityConfigurerAdapter
{
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
		
	}
		
	@Autowired
	UserRepository userrepo;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
        .antMatchers( "/favicon.ico").permitAll()
        .and()
		.authorizeRequests().antMatchers("/signup").permitAll()
		.and()
		.authorizeRequests().antMatchers("/").permitAll()
		.and()
		.authorizeRequests().antMatchers("/index").permitAll()
		.and()
		.authorizeRequests().antMatchers("/signups").permitAll()
		.and()
		.authorizeRequests().antMatchers("/images/**").permitAll()
		.and()
		.authorizeRequests().antMatchers("/CSS/**").permitAll()
		.and()
		.authorizeRequests().antMatchers("/login").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login").permitAll()
		.failureUrl("/login-error.html")
		.defaultSuccessUrl("/home", true)
		.and()
		.oauth2Login()
		.authorizationEndpoint()
		.and()
		.defaultSuccessUrl("/home", true).loginPage("/login").and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/logout-success").permitAll();
		
	}
	
	
	
//		@Bean
//		@Override
//		protected UserDetailsService userDetailsService() {
//			List<UserDetails> users = new ArrayList<>();
//			users.add(User.withDefaultPasswordEncoder().username("Albert").password("1234").roles("USER").build());
//			return new InMemoryUserDetailsManager(users);
//		}
}
