package com.buddz.auth.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private ClientDetailsService clientDetailsService;
	
	//@Autowired
	//PasswordEncoder passwordEncoder;
	
	 @Override
	    public void configure(WebSecurity web) throws Exception {
	        web.debug(true);
	}
	 
	@Bean
	public UserDetailsManager userDetailsManager() throws Exception {
		
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		jdbcUserDetailsManager.setAuthenticationManager(authenticationManagerBean());
		return jdbcUserDetailsManager; 
	}
	
	@Bean
	public GroupManager groupManager() throws Exception {
		
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		jdbcUserDetailsManager.setAuthenticationManager(authenticationManagerBean());
		return jdbcUserDetailsManager; 
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth
	//	.userDetailsService(userDetailsService())
		
	//	.and()
	//	.authenticationProvider(authenticationProvider())
		.jdbcAuthentication().dataSource(dataSource)
		.passwordEncoder(passwordEncoder())
		;
	}
	
	@Bean(name="userDetailsService")
	public UserDetailsService userDetailsService() {
		JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
		jdbcImpl.setDataSource(dataSource);
		jdbcImpl.setUsersByUsernameQuery("select username, password, enabled from users where username=?");
		jdbcImpl.setGroupAuthoritiesByUsernameQuery("select g.id, g.group_name, ga.authority " + 
		                                            "from  groups g, group_members gm, group_authorities ga " + 
				                                    "where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id");
		
		return jdbcImpl;
	}
	
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
   
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
	  
	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable().antMatcher("/").authorizeRequests()
      .antMatchers("/**", "/signup", ".login").permitAll()
      .anyRequest()
      .authenticated()
//      .and()
//      .formLogin()
      ;
    }

//	 @Bean
//	    @Autowired
//	    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
//	        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
//	        handler.setTokenStore(tokenStore);
//	        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
//	        handler.setClientDetailsService(clientDetailsService);
//	        return handler;
//	    }
//	 
//	 @Bean
//	    @Autowired
//	    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
//	        TokenApprovalStore store = new TokenApprovalStore();
//	        store.setTokenStore(tokenStore);
//	        return store;
//	    }
	 
	@Bean
	public PasswordEncoder passwordEncoder() {
		String idForEncode = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(idForEncode, new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("sha256", new StandardPasswordEncoder());
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);	
		passwordEncoder.setDefaultPasswordEncoderForMatches((PasswordEncoder) encoders.get(idForEncode));
		return passwordEncoder;
  }
	
	@Bean
    public FilterRegistrationBean myCorsFilter() {
         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         CorsConfiguration config = new CorsConfiguration();
         config.setAllowCredentials(true);
         config.addAllowedOrigin("http://localhost:8080");
         config.addAllowedHeader("*");
         config.addAllowedMethod("*");
         source.registerCorsConfiguration("/**", config);
         FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
         bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
         return bean;
    }
}
