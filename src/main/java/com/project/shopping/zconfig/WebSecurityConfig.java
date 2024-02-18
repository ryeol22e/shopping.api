package com.project.shopping.zconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
import com.project.shopping.zconfig.filters.ApiFilter;
import com.project.shopping.zconfig.handler.LoginFailHandlers;
import com.project.shopping.zconfig.handler.LoginSuccessHandlers;
import com.project.shopping.zconfig.handler.LogoutSuccessHandlers;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	@Value("${spring.profiles.active}")
	private String profile;
	@Value("${client.url}")
	private String clientUrl;
	private final MemberService memberService;

	/**
	 * password encoder
	 * 
	 * @return
	 */
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * security filter chain
	 * 
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		final String[] IGNORE_PATH_ARRAY = {"/api/auth/check", "/api/common/**", "/api/display/**", "/api/product/**", "/api/cate/**"};

		http.httpBasic(basic -> basic.disable()).csrf(csrf -> csrf.disable())
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(login -> {
					login.loginProcessingUrl("/api/member/login").usernameParameter("memberId")
							.passwordParameter("memberPassword")
							.successHandler(new LoginSuccessHandlers()).permitAll()
							.failureHandler(new LoginFailHandlers()).permitAll();
				}).logout(logout -> {
					logout.logoutUrl("/api/member/logout")
							.logoutSuccessHandler(new LogoutSuccessHandlers());
				})
				.exceptionHandling(
						handling -> handling.authenticationEntryPoint(new AuthEntryPoint()))

				.authorizeHttpRequests(auth -> {
					auth.requestMatchers(IGNORE_PATH_ARRAY).permitAll();
					auth.requestMatchers("/api/member/**").authenticated();
					auth.requestMatchers("/api/auth/**").authenticated();
					auth.requestMatchers("/api/chat/**").authenticated();
					auth.requestMatchers("/api/admin/**").hasAnyAuthority(MemberEnum.ADMIN.getValue());
				})
				.addFilterBefore(new ApiFilter(memberService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}


	@Bean
	CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		config.setAllowCredentials(true);
		// config.setAllowedOrigins(ORIGIN_LIST);

		config.addAllowedOrigin(clientUrl);
		config.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
		config.addAllowedHeader(HttpHeaders.AUTHORIZATION);
		config.addAllowedMethod(HttpMethod.GET);
		config.addAllowedMethod(HttpMethod.POST);
		config.setMaxAge(3600L);

		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
