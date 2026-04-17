package jp.co.attendance.attendance_app.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * ログイン認証時にDBから取得したユーザー情報を保持するDTO
 */

@Data
public class EmployeeAuthDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String employeeCode;
	private String password;
	private String displayName;
	private String roleCode;
}