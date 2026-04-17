package jp.co.attendance.attendance_app.mapper;

import jp.co.attendance.attendance_app.dto.EmployeeAuthDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeMapper {

	/**
	 * 【R_001】ログイン処理時、ユーザ名をパラメータとして受け取り従業員情報を返却する
	 * 
	 * @param username ログインID (employees_mst.employee_code と突き合わせ)
	 * @return EmployeeAuthDto 従業員認証用情報（存在しない場合はnull）
	 */
	EmployeeAuthDto findByUsername(@Param("username") String username);
}
