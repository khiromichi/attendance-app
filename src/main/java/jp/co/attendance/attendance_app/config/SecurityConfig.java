package jp.co.attendance.attendance_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jp.co.attendance.attendance_app.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {
		

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/css/**", "/js/**", "/h2-console/**")
				.permitAll().anyRequest().authenticated()
			)
			.formLogin(form -> form
                .loginPage("/login") // SCR001: ログイン画面
                .loginProcessingUrl("/login") // POST /login
                .defaultSuccessUrl("/home", true) // SCR002: ホーム画面へ
                .failureUrl("/login?error") // 認証失敗
                .permitAll()
            )
				// 1. セッション管理の強化
			.sessionManagement(session -> session
				// セッション固定攻撃対策: 認証後に新しいセッションを作成
				.sessionFixation().migrateSession()
				// 同時ログイン制御: 1ユーザーにつき1セッションのみ許可
				.maximumSessions(1)
				// 2つ目のログインが試みられた場合、古い方を無効化
				.expiredUrl("/login?expired")
			)
			// 2. Remember-Me の追加（API仕様書 F001 に準拠）
			.rememberMe(me -> me.key("uniqueAndSecretAttendanceKey") // 署名用の秘密鍵
				.tokenValiditySeconds(60 * 60 * 24 * 5) // 5日間有効
				.rememberMeParameter("rememberMe") // 画面のチェックボックス名
				.userDetailsService(customUserDetailsService) // カスタムUserDetailsを使用
			)
			.logout(logout -> logout
				// [解決] 警告の出ない MvcRequestMatcher を使用
				.logoutUrl("/logout") // PL009: POST /logout
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true) // セッション破棄 
                .deleteCookies("JSESSIONID", "remember-me")
             )
		     .csrf(csrf -> csrf
                // 特定パスの除外も文字列で記述
                .ignoringRequestMatchers("/h2-console/**")
             )
                .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
             );

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}