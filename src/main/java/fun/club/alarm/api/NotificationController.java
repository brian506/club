package fun.club.alarm.api;

import fun.club.alarm.service.NotificationService;
import fun.club.common.util.SuccessResponse;
import fun.club.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 클라이언트와 서버간의 SSE 연결
     *
     * 로그인 시 자동으로 알림 구독이 이루어지도록 하는 것은 프론트 단이 처리해야 하는 부분(알림 전송 요청,읽음 처리 요청도 마찬가지)
     */
    @GetMapping(value = "/subscribe",produces = "text/event-stream")
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false,defaultValue = "") String lastEventId) {
        notificationService.subscribe(userDetails.getUsername(),lastEventId);
        SuccessResponse response = new SuccessResponse<>(true,"알림 구동 성공",lastEventId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /**
     * 알림 전송 요청
     */

    /**
     * 알림 읽음 처리 요청
     */
}
/**
 * 연결 요청 처리를 위해서 text/event-stream 형태로 받아야 한다.
 * Last-Event-ID 는 받지 못한 이벤트(연결에 대한 만료,종료)가 있을 경우, 마지막 이벤트 ID 값을 넘겨 이후의 데이터부터 받을 수 있게 할 수 있는 정보임(항상 전달 받는 정보는 아니므로 필수값은 아님)
 * 현재 누구로부터 온 알림 구독인지에 대한 부분은 @AuthenticationPrincipal 을 활용해 입력받는다.
 */