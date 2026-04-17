package jp.co.attendance.attendance_app.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * ログイン認証の成功・失敗を検知してアプリケーションログを出力するクラス
 */
@Component
public class LoginAuthenticationListener {

	private static final Logger log = LoggerFactory.getLogger(LoginAuthenticationListener.class);

	/**
	 * 認証成功時のイベントリスナー
	 */
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {
		Object principal = event.getAuthentication().getPrincipal();
		if (principal instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) principal;
			// メッセージ設計書 LOG002
			log.info("[LOG002] ログインに成功しました。 userId: {}", userDetails.getUsername());
		}
	}

	/**
	 * 認証失敗時のイベントリスナー (パスワード間違い・存在しないユーザー)
	 */
	@EventListener
	public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
		// 失敗時は認証前なので、入力された文字列がそのまま渡されます
		String username = (String) event.getAuthentication().getPrincipal();
		// メッセージ設計書 LOG003
		log.warn("[LOG003] ログインに失敗しました。認証情報が一致しません。 userId: {}", username);
	}
}