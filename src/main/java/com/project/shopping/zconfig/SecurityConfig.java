package com.project.shopping.zconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.member.service.MemberService;
import com.project.shopping.utils.UtilsStringEscape;
import com.project.shopping.zconfig.authentications.AuthEntryPoint;
import com.project.shopping.zconfig.filters.ApiFilter;
import com.project.shopping.zconfig.filters.ApiXssFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	@Value("${spring.profiles.active}")
	private String profile;
	@Value("${client.url}")
	private String clientUrl;
	private final MemberService memberService;
	private final ObjectMapper objectMapper;

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
		final String[] ignorePathArray = {"/api/member/auth", "/api/member/login", "/api/common/**", "/api/display/**", "/api/product/**", "/api/cate/**"};

		http.httpBasic(basic -> basic.disable()).csrf(csrf -> csrf.disable()).formLogin(login -> login.disable()).logout(logout -> logout.disable())
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(
						handling -> handling.authenticationEntryPoint(new AuthEntryPoint()))

				.authorizeHttpRequests(auth -> {
					auth.requestMatchers(ignorePathArray).permitAll();
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

	@Bean
	FilterRegistrationBean<ApiXssFilter> filterRegistrationBean() {
		FilterRegistrationBean<ApiXssFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new ApiXssFilter());
		filterRegistration.setOrder(1);
		filterRegistration.addUrlPatterns("/*");

		return filterRegistration;
	}

	@Bean
	MappingJackson2HttpMessageConverter jsonEscapeConverter() {
		ObjectMapper copy = objectMapper.copy();

		copy.getFactory().setCharacterEscapes(new UtilsStringEscape());

		return new MappingJackson2HttpMessageConverter(copy);
	}
}
