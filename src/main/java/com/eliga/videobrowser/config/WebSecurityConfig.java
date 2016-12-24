package com.eliga.videobrowser.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.eliga.videobrowser.types.ROLE;

@Configuration
@ComponentScan("com.eliga")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Autowired
    private AuthenticationProvider authenticationProvider;

	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
		logger.info("configuring httpsecurity request");
    	httpSecurity.csrf().disable().authorizeRequests().
  		antMatchers("/admin/**").hasAuthority(ROLE.admin.toString()).
 		anyRequest().authenticated().
		and().formLogin().loginPage("/login.html").defaultSuccessUrl("/authHome.html").
		failureUrl("/loginfailure.html").loginProcessingUrl("/perform_login");
    }

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers("/css/*.css").antMatchers("/anonymousMenu").antMatchers("/mnu.html").
		antMatchers("/register.html").antMatchers("/reg").antMatchers("/js/*.js").antMatchers("/login.html").
		antMatchers("/home.html").antMatchers("/homeContent.html").
		antMatchers("/anonMenu.html").antMatchers("/loginfailure.html");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("configuring authentication manager {}",authenticationProvider);
        auth.authenticationProvider(authenticationProvider);
    }
}
