package jp.co.attendance.attendance_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800) // 30分でタイムアウト
public class SessionConfig {
    // 特殊なカスタマイズ（テーブル名の変更など）がない限り、中身は空でOK
}