package jp.co.attendance.attendance_app.service;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jp.co.attendance.attendance_app.dto.AttendanceCalculationResult;
import jp.co.attendance.attendance_app.dto.AttendanceDayDto;
import jp.co.attendance.attendance_app.entity.AttendanceRecord;
import jp.co.attendance.attendance_app.mapper.AttendanceMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceMapper attendanceMapper;
    private final AttendanceCalculationService calculationService;

    // 表示形式の定義 (HH:mm) [cite: 100, 130]
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public List<AttendanceDayDto> getMonthlyList(Long employeeId, YearMonth targetMonth) {
        // 月初・月末の算出 [cite: 94]
        var startDate = targetMonth.atDay(1);
        var endDate = targetMonth.atEndOfMonth();

        // 生データの取得 (R_010) [cite: 95]
        List<AttendanceRecord> records = attendanceMapper.findMonthlyAttendance(employeeId, startDate, endDate);

        return records.stream().map(record -> {
            // PL008の共通ロジックを呼び出して計算 [cite: 112]
            AttendanceCalculationResult calc = calculationService.calculate(
                record.getClockInTime(), 
                record.getClockOutTime(), 
                record.getBreaks()
            );

            // DTOの組み立て [cite: 1027]
            return AttendanceDayDto.builder()
                .date(record.getWorkDate())
                .dayOfWeek(record.getWorkDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE)) // "月", "火"... [cite: 1028]
                .checkInTime(formatTime(record.getClockInTime()))
                .checkOutTime(formatTime(record.getClockOutTime()))
                .breakMinutes(calc.getBreakDuration())
                .workingMinutes(calc.getWorkDuration())
                // status_id=3(退勤済み)ならPRESENT、それ以外は暫定でWORKING [cite: 289, 1030]
                .status(record.getStatusId() != null && record.getStatusId() == 3 ? "PRESENT" : "WORKING")
                .build();
        }).collect(Collectors.toList());
    }

    /**
     * OffsetDateTimeをHH:mm形式の文字列に変換する（Null安全） [cite: 100]
     */
    private String formatTime(OffsetDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(TIME_FORMATTER);
    }
}
