package com.project.shopping.zconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.member.service.MemberService;
import com.project.shopping.zconfig.authentications.AuthEntryPoint;
import com.project.shopping.zconfig.filters.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private String WEB_URL = "http://localhost:7800";
	private final MemberService memberService;

	/**
	 * password encoder
	 * @return
	 */
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * security filter chain
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic().disable().csrf().disable().formLogin().disable().logout().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.cors()
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(new AuthEntryPoint())
			.and()
				.authorizeHttpRequests()
				.requestMatchers("/api/auth/**").authenticated()
				.requestMatchers("/api/chat/**").authenticated()
				.requestMatchers("/api/admin/**").hasAnyAuthority(MemberEnum.ADMIN.getValue())
				.requestMatchers("/api/common/**").permitAll()
				.requestMatchers("/api/member/**").permitAll()
				.requestMatchers("/api/display/**").permitAll()
				.requestMatchers("/api/product/**").permitAll()
				.requestMatchers("/api/cate/**").permitAll()
			.and()
				.addFilterBefore(new JwtFilter(memberService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * security ignore.
	 * @return
	 * @throws Exception
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() throws Exception {
		return (web) -> web.ignoring()
			.requestMatchers("/api/common/headers");
	}

	@Bean
	CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		// config.setAllowedOrigins(List.of(webUrl));
		config.setAllowCredentials(true);
		config.addAllowedOrigin(WEB_URL);
		config.addAllowedHeader("Content-Type");
		config.addAllowedHeader("Authorization");
		config.addAllowedHeader("MemberId");
		config.addAllowedMethod(HttpMethod.GET);
		config.addAllowedMethod(HttpMethod.POST);
		config.setMaxAge(3600L);
		
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
