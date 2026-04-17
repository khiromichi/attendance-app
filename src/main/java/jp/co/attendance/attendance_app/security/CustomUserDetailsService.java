package jp.co.attendance.attendance_app.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.attendance.attendance_app.dto.EmployeeAuthDto;
import jp.co.attendance.attendance_app.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final EmployeeMapper employeeMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EmployeeAuthDto employee = employeeMapper.findByUsername(username);
		if (employee == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}

		return new CustomUserDetails(employee); // 内部IDや表示名を持たせたカスタムクラス
	}
}