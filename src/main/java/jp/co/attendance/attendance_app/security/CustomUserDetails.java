package jp.co.attendance.attendance_app.security;

import jp.co.attendance.attendance_app.dto.EmployeeAuthDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails, Serializable {
    
    // セッション永続化のために必須
    private static final long serialVersionUID = 1L;

    private final EmployeeAuthDto employee;

    public CustomUserDetails(EmployeeAuthDto employee) {
        this.employee = employee;
    }

    public EmployeeAuthDto getEmployee() {
        return employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = employee.getRoleCode();
        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmployeeCode();
    }

    @Override
    public boolean isEnabled() {
        // 商用設計：DBの削除フラグや有効フラグと連動させる
        // 例: return !employee.isDeleted(); 
        return true; 
    }

    // 他のメソッド（isAccountNonExpired等）は現状のままでも許容されます
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
}