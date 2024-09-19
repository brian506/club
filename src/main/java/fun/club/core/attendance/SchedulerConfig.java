package fun.club.core.attendance;

import fun.club.service.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final AttendanceService attendanceService;

    @Scheduled(cron = "0 55 18 * * WED") // 매주 수요일 18시 55분에 시작
    public void startAttendanceCheck(){
        System.out.println("회의가 시작되었습니다.");
    }
    @Scheduled(cron = "0 10 19 * * WED")
    public void endAttendanceCheck(){
        attendanceService.applyPenalty();
        System.out.println("회의가 종료되었습니다.");
    }
}
