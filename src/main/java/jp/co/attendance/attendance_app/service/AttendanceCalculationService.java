package jp.co.attendance.attendance_app.service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.attendance.attendance_app.dto.AttendanceCalculationResult;
import jp.co.attendance.attendance_app.entity.BreakRecord;

@Service
public class AttendanceCalculationService {

    /**
     * 出勤・退勤時刻と休憩リストから勤務時間を算出する（PL008）
     */
    public AttendanceCalculationResult calculate(
            OffsetDateTime clockIn, 
            OffsetDateTime clockOut, 
            List<BreakRecord> breaks) {

        // 1. 出退勤のいずれかが欠けている場合は計算不可 [cite: 121]
        if (clockIn == null || clockOut == null) {
            return AttendanceCalculationResult.builder()
                    .breakDuration("").workDuration("").netMinutes(0L).build();
        }

        // 2. 出退勤の逆転チェック [cite: 125]
        if (clockOut.isBefore(clockIn)) {
            throw new IllegalArgumentException("MSG016: 退勤時刻が出勤時刻より前です");
        }

        // 3. 休憩時間の集計（終了済みのみ対象） [cite: 122, 124]
        long totalBreakMinutes = breaks.stream()
                .filter(b -> b.getStartTime() != null && b.getEndTime() != null)
                .mapToLong(b -> {
                    if (b.getEndTime().isBefore(b.getStartTime())) {
                        throw new IllegalArgumentException("MSG016: 休憩終了が開始より前です");
                    }
                    return Duration.between(b.getStartTime(), b.getEndTime()).toMinutes();
                })
                .sum();

        // 4. 実労働時間の算出 [cite: 125, 126]
        long grossMinutes = Duration.between(clockIn, clockOut).toMinutes();
        long netMinutes = grossMinutes - totalBreakMinutes;

        if (netMinutes < 0) {
            throw new IllegalArgumentException("MSG016: 休憩時間が勤務時間を超えています");
        }

        return AttendanceCalculationResult.builder()
                .breakDuration(formatMinutes(totalBreakMinutes))
                .workDuration(formatMinutes(netMinutes))
                .netMinutes(netMinutes)
                .build();
    }

    private String formatMinutes(long minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }
}