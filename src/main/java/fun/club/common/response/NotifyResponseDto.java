package fun.club.common.response;

import fun.club.alarm.domain.Notification;
import lombok.*;

@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class NotifyResponseDto {
    private String id;
    private String name;
    private String content;
    private String type;
    private String createdAt;
    // String 으로 한 이유는 프론트엔드가 문자열 형태로 쉽게 다룰 수 있기 때문


    public static NotifyResponseDto toNotifyDto(Notification notification) {
        return NotifyResponseDto.builder()
                .id(notification.getId().toString())
                .name(notification.getReceiver().getUsername())
                .content(notification.getContent())
                .type(String.valueOf(notification.getNotificationType()))
                .createdAt(notification.getCreatedDate().toString())
                .build();
    }
}
