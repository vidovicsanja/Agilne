package com.agilne.apigateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;

import com.agilne.apigateway.dto.CustomUserDto;

import org.springframework.http.HttpMethod;

@Configuration
@EnableWebFluxSecurity
public class ApiGatewayAuthentication {

    @Bean
	public MapReactiveUserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {

		List<UserDetails> users = new ArrayList<>();
		List<CustomUserDto> usersFromDatabase;

		ResponseEntity<CustomUserDto[]> response = 
		    new RestTemplate().getForEntity("http://localhost:8770/users-service/users", CustomUserDto[].class);

		usersFromDatabase = Arrays.asList(response.getBody());

		for (CustomUserDto cud: usersFromDatabase) {
			users.add(User.withUsername(cud.getEmail())
					.password(encoder.encode(cud.getPassword()))
					.roles(cud.getRole())
					.build());
		}

		return new MapReactiveUserDetailsService(users);
	}

	@Bean
	public BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeExchange()
		.pathMatchers(HttpMethod.GET, "/users-service/**").permitAll() //jedino owner moze da brise
		.pathMatchers(HttpMethod.DELETE, "/users-service/**").hasRole("OWNER") //jedino owner moze da brise
		.pathMatchers(HttpMethod.PUT, "/users-service/**").hasAnyRole("OWNER","ADMIN") //owner i admin mogu da rade update, ali admin moze samo usera
		.pathMatchers(HttpMethod.POST, "/users-service/**").hasAnyRole("OWNER","ADMIN") //owner i admin mogu da dodaju, ali admin moze samo usera
		.pathMatchers("/currency-exchange/**").permitAll()
		.pathMatchers("/currency-conversion/**").hasRole("USER")
		.pathMatchers("/bank-account/**").hasRole("ADMIN")
		.pathMatchers("/crypto-wallet/**").hasRole("ADMIN")
		.pathMatchers("/crypto-exchange/**").permitAll()
		.pathMatchers("/crypto-conversion/**").hasRole("USER")		
		.pathMatchers("/trade-service/**").hasRole("USER")
		.pathMatchers("/transfer-service/**").hasRole("USER")
		.and()
		.httpBasic();

		return http.build();
	}
    
}

