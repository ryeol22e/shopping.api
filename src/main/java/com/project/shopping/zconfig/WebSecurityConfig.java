package com.project.shopping.zconfig;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
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
import com.project.shopping.zconfig.authentications.AuthEntryPoint;
import com.project.shopping.zconfig.filters.ApiFilter;
import com.project.shopping.zconfig.handler.LoginFailHandlers;
import com.project.shopping.zconfig.handler.LoginSuccessHandlers;
import com.project.shopping.zconfig.handler.LogoutSuccessHandlers;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Value("${spring.profiles.active}")
	private String profile;

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
		http.httpBasic(basic -> basic.disable()).csrf(csrf -> csrf.disable())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.formLogin(login -> {
					login.loginProcessingUrl("/api/member/login")
						.usernameParameter("memberId")
						.passwordParameter("memberPassword")
						.successHandler(new LoginSuccessHandlers())
						.failureHandler(new LoginFailHandlers())
						.permitAll();
				})
				.logout(logout -> {
					logout.logoutUrl("/api/member/logout")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.logoutSuccessHandler(new LogoutSuccessHandlers());
				})
				.exceptionHandling(handling -> handling.authenticationEntryPoint(new AuthEntryPoint()))
				.authorizeHttpRequests()
					.requestMatchers("/api/member/**").authenticated()
					.requestMatchers("/api/auth/**").authenticated()
					.requestMatchers("/api/chat/**").authenticated()
					.requestMatchers("/api/admin/**").hasAnyAuthority(MemberEnum.ADMIN.getValue())
					.requestMatchers("/api/common/**").permitAll()
					.requestMatchers("/api/display/**").permitAll()
					.requestMatchers("/api/product/**").permitAll()
					.requestMatchers("/api/cate/**").permitAll()
				.and()
				.addFilterBefore(new ApiFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * security ignore.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() throws Exception {
		return (web) -> web.ignoring()
				.requestMatchers("/api/common/headers");
	}

	@Bean
	@Deprecated
	CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final List<String> WEB_URL_LIST = List.of(
				"https://".concat("prod".equalsIgnoreCase(profile) ? "www" : profile).concat(".shoppingmall.com:7800"),
				"http://localhost:7800");

		config.setAllowCredentials(true);
		config.setAllowedOrigins(WEB_URL_LIST);
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
