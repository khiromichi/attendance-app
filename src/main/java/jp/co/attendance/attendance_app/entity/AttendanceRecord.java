package jp.co.attendance.attendance_app.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AttendanceRecord {
    private Long id;
    private Long employeeId;
    private LocalDate workDate;
    private OffsetDateTime clockInTime;
    private OffsetDateTime clockOutTime;
    private Long statusId;
    
    // PL007/R_010 で取得する子要素
    private List<BreakRecord> breaks; 
}