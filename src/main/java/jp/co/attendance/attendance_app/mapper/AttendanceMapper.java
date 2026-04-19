package jp.co.attendance.attendance_app.mapper;

import jp.co.attendance.attendance_app.entity.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

/**
 * 勤怠記録に関するリポジトリインターフェース
 */
@Mapper
public interface AttendanceMapper {

    /**
     * 指定された期間の勤怠情報を取得する（R_010対応）
     * 休憩記録(break_records)を外部結合した結果を取得します。
     *
     * @param employeeId 従業員ID [cite: 768]
     * @param startDate  対象期間開始日（月初） [cite: 769]
     * @param endDate    対象期間終了日（月末） [cite: 770]
     * @return 勤怠記録のリスト
     */
    List<AttendanceRecord> findMonthlyAttendance(
        @Param("employeeId") Long employeeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // 必要に応じて、PL002で使用する当日勤怠取得(R_008)などもここに追加します [cite: 728]
}