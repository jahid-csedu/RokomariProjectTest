package com.example.demo.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.jwt.JwtUsernamePasswordAuthenticationFilter;
import com.example.demo.jwt.JwtVerifier;
import com.example.demo.service.UserDetailsServiceImplementation;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ApplicationSecurityConfiguration {
	
	@Autowired
	UserDetailsServiceImplementation userDetailsService;
	
	@Configuration
	@Order(1)
	public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter{
		private final JwtConfig jwtConfig;
		private final SecretKey secretKey;
	
		public JwtSecurityConfiguration(JwtConfig jwtConfig, SecretKey secretKey) {
			super();
			this.jwtConfig = jwtConfig;
			this.secretKey = secretKey;
		}
	
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider());
		}
	
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.requestMatchers(matchers -> matchers
		                  .antMatchers("/api/**")
		              )
		              .authorizeRequests(authz -> authz
		                  .anyRequest().authenticated()
		              )
				.addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
				.addFilterAfter(new JwtVerifier(jwtConfig, secretKey), JwtUsernamePasswordAuthenticationFilter.class);
	//		
	//		http.httpBasic();
		}
	}
	
	@Configuration
	public class FormBasedSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider());
		}
	
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/webjars/**")
					.permitAll()
				.antMatchers("/add-contact")
					.hasRole("ADMIN")
				.antMatchers("/contacts")
					.hasAnyRole("USER", "ADMIN")
				.anyRequest()
					.authenticated()
				.and()
				.exceptionHandling()
					.accessDeniedPage("/access-denied")
				.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/home", true)
					.permitAll()
				.and()
				.logout()
					.logoutUrl("/logout")
					.clearAuthentication(true)
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/login")
					.permitAll();
	//		
	//		http.httpBasic();
		}
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	

}
