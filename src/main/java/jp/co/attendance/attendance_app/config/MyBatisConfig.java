package jp.co.attendance.attendance_app.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("jp.co.attendance.attendance_app.mapper")
public class MyBatisConfig {
}
