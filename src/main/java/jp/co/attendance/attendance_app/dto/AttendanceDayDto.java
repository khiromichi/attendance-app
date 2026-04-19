package jp.co.attendance.attendance_app.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 月次勤怠一覧の1日分を表現するDTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDayDto {
    private LocalDate date;        // 日付 (yyyy-MM-dd)
    private String dayOfWeek;      // 曜日 (月, 火...)
    private String checkInTime;    // 出勤時刻 (HH:mm)
    private String checkOutTime;   // 退勤時刻 (HH:mm)
    private String breakMinutes;   // 休憩時間合計 (HH:mm)
    private String workingMinutes; // 実労働時間合計 (HH:mm)
    private String status;         // 勤務状態 (PRESENT, WORKING, ABSENT等) 
    private String note;           // 備考
}
