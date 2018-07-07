package com.buddz.auth.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class AppConfig {

	@Bean
	public DataSource dataSource() {
	    return DataSourceBuilder
	        .create()
	        .username("buddz")
	        .password("buddz")
	        .url("jdbc:postgresql://localhost:5432/buddz")
	        .build();
	}
	
}
