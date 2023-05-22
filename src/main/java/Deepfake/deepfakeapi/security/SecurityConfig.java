package Deepfake.deepfakeapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception
	{
		return (web) -> web.ignoring().antMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**");
	}

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		return http
				.csrf().disable()
				// HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
				.authorizeRequests()
				.antMatchers("/test").authenticated()
				.antMatchers("/admin/**").hasRole("ADMIN") // admin 으로 시작하는 URL은 ADMIN만 허용
				.antMatchers("/**").permitAll()

				// 세션은 사용하지 않음
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// jwt 인증 필터 적용
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
				
				//초기 로그인 화면 비활성화
				.formLogin().disable()

				.build();
	}
}