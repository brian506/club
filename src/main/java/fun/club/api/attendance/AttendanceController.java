package fun.club.api.attendance;

import fun.club.common.util.SuccessResponse;
import fun.club.service.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestParam Long userId){
        attendanceService.markAttendance(userId);
        SuccessResponse response = new SuccessResponse<>(true,"출석 체크 완료",userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
