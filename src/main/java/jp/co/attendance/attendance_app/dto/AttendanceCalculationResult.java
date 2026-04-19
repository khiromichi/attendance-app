package jp.co.attendance.attendance_app.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AttendanceCalculationResult {
	String breakDuration; // HH:mm 形式
    String workDuration;  // HH:mm 形式
    long netMinutes;      // 分単位（内部計算・集計用）
}
