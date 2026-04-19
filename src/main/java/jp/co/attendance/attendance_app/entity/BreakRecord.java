package jp.co.attendance.attendance_app.entity;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreakRecord {
	private Long id;
	private Long attendanceId;
	private OffsetDateTime startTime; // break_start
	private OffsetDateTime endTime; // break_end
	private Integer version; // 排他制御用
}