package com.example.digitalBanking.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableWebSecurity
public class SecurityConifg {
	
	@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		PasswordEncoder passwordEncoder=passwordEncoder();
		return new InMemoryUserDetailsManager(
				User.withUsername("user1").password(passwordEncoder.encode("12345")).authorities("USER").build(),
				User.withUsername("admin").password(passwordEncoder.encode("12345")).authorities("USER","ADMIN").build()
				);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(ar->ar.anyRequest().authenticated())
				.csrf(csrf->csrf.disable())
				//.httpBasic(Customizer.withDefaults())
				.oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
				.build();
		
	}
	
	@Bean
	JwtEncoder jwtEncoder() {
		String secretKey="1AZSXC4VWS3EDFR45HYJI765OLMPBV2D2E5TGVBHJUX0586HFGVN54KIRNCY4375HY";
		return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
	}
	
	@Bean 
	JwtDecoder jwtDecoder() {
		String secretKey="1AZSXC4VWS3EDFR45HYJI765OLMPBV2D2E5TGVBHJUX0586HFGVN54KIRNCY4375HY";
		SecretKeySpec secretKeySpec= new SecretKeySpec( secretKey.getBytes(),"RSA");
		return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
	}

}
