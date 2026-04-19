package jp.co.attendance.attendance_app.controller;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.attendance.attendance_app.dto.AttendanceDayDto;
import jp.co.attendance.attendance_app.security.CustomUserDetails;
import jp.co.attendance.attendance_app.service.AttendanceService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	
	@GetMapping("/attendance/list")
	public String showMonthlyList(
	        @RequestParam(required = false) String targetYearMonth,
	        @RequestParam(required = false) String monthShift,
	        @AuthenticationPrincipal CustomUserDetails loginUser,
	        Model model) {
		
		// loginUser が null の場合の安全策（通常はSecurityConfigでガードされますが、商用では念のためチェック）
        if (loginUser == null) {
            return "redirect:/login";
        }
	    
	    try {
	        // 1. 表示対象年月の決定 (PL007-No2)
	        YearMonth ym = (targetYearMonth == null) 
	            ? YearMonth.now() 
	            : YearMonth.parse(targetYearMonth);

	        // 2. 月切替操作の適用 (prev / next)
	        if ("prev".equals(monthShift)) ym = ym.minusMonths(1);
	        if ("next".equals(monthShift)) ym = ym.plusMonths(1);

	        // 3. サービス呼び出し
	        Long employeeId = loginUser.getEmployee().getId();
	        List<AttendanceDayDto> list = attendanceService.getMonthlyList(employeeId, ym);
	        
	        model.addAttribute("attendanceList", list);
	        model.addAttribute("targetYearMonth", ym.toString());
	        
	        if (list.isEmpty()) {
	            model.addAttribute("emptyMessage", "MSG015: 対象月の勤怠データはありません。"); // [cite: 160]
	        }
	        
	    } catch (DateTimeParseException e) {
	        model.addAttribute("errorMessage", "MSG014: 対象年月を確認してください。"); // [cite: 158]
	        return "attendance/list";
	    }
	    return "attendance/list";
	}

}
