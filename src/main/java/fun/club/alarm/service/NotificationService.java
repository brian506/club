package fun.club.alarm.service;


import fun.club.alarm.domain.Notification;
import fun.club.alarm.domain.NotificationType;
import fun.club.alarm.repository.EmitterRepository;
import fun.club.alarm.repository.NotificationRepository;
import fun.club.common.response.NotifyResponseDto;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;


    /**
     * 알림 구독
     * 필요할때마다 해당 구독자가 생성한 SseEmitter 를 불러와 이벤트에 대한 응답을 전송하는 것
     *
     * 1. 클라이언트에게 더미 이벤트를 전송하여 연결 생성 알림
     * 2. 수신 못한 이벤트가 있는 경우, 해당 이벤트 전송
     * 3. 생성된 SseEmitter 를 반환하여 클라이언트와의 sse 통신 유지
     *
     * => 클라이언트와의 SSE 스트림 통신을 유지하면서 연결 생성
     */
    public SseEmitter subscribe(String username,String lastEventId){

        // makeTimeIncludeId 로 username 포함하여 sseemitter 식별하기 위한 고유 아이디 생성
        String emitterId = makeTimeIncludeId(username);
        // 새로운 sseemitter 객체 생성, emitterId 를 키로 사용해 리포지토리에 저장
        SseEmitter emitter = emitterRepository.save(emitterId,new SseEmitter(DEFAULT_TIMEOUT));

        // SseEmitter 가 완료되거나 타임아웃될 때 해당 SseEmitter 삭제
        emitter.onCompletion(()->emitterRepository.deleteById(emitterId));
        emitter.onTimeout(()->emitterRepository.deleteById(emitterId));

        // 503 에러 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(username);
        sendNotification(emitter,eventId,emitterId,"EventStream Created. [userEmail = " + username + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 event 유실 예방
        if (hasLostData(lastEventId)){
            sendLostData(lastEventId,username,emitterId,emitter);
        }
        return emitter;
    }

    // 데이터가 언제 보내졌는지,유실되었는지 알기 위한 메서드
    private String makeTimeIncludeId(String email){
        return email + "_" + System.currentTimeMillis();
    }

    // 클라이언트에게 연결 생성 알리는 메서드(클라이언트-서버 연결 유지)
    private void sendNotification(SseEmitter emitter,String eventId,String emitterId,Object data){
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        }catch (IOException e){
            emitterRepository.deleteById(emitterId);
            throw new RuntimeException("연결 오류");
        }
    }

    /**
     * lastEventId 가 비어있는 지의 유무를 확인하여 이전 이벤트 이후에 새로운 이벤트를 놓치지 않았는지 확인
     * 손실된 이벤트가 있으면 true
     * 손실된 이벤트가 없으면 false
     */
    private boolean hasLostData(String lastEventId){
        return !lastEventId.isEmpty();
    }

    // 수신자에게 전송되지 못한 이벤트 데이터를 캐시에서 가져와 클라이언트에게 전송하는 과정 수행
    private void sendLostData(String lastEventId,String email,String emitterId,SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(email));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0) // lastEventId < entry.getKey()
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    /**
     * 알림을 생성하고 지정된 수신자에게 알림을 전송하는 기능
     * 수신자,알림 유형,내용,URL 등의 정보를 인자로 받음
     * 모든 수신자에게 알림 전송 위해 모든 SseEmitter 를 가져와 알림 전송, 이벤트 캐시 저장
     *
     * => 알림을 생성하고 알림을 수신하는 모든 클라이언트에게 전송
     *
     * send() 는 특정 사용자에게 개별적으로 보낼 때 필요한 메서드
     */
    public void send(User receiver, NotificationType notificationType,String content,String url){
        Notification notification = notificationRepository.save(createNotification(receiver,notificationType,content,url));

        String receiverEmail = receiver.getEmail();
        String eventId = receiverEmail + "_" + System.currentTimeMillis();

        // 여러 클라이언트와 연결된 경우를 대비해 다중 연결을 하기 위해
        // 수신자에 연결된 모든 SseEmitter 객체를 emitters 변수에 가져옴
        Map<String,SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverEmail);
        emitters.forEach( // emitters 를 순환하며 각 SseEmitter 객체에 알림 전송
                (key,emitter)->{
                    emitterRepository.saveEventCache(key,notification);
                    sendNotification(emitter,eventId,key, NotifyResponseDto.toNotifyDto(notification));
                }
        );
    }

    /**
     *  모든 사용자에게 보내기 위한 메서드임
     */
    public void sendToAll(NotificationType notificationType,String content,String url){
        List<User> users = userRepository.findAll();
        users.forEach(user -> send(user,notificationType,content,url));
    }

    // 매개변수로 받은 정보들을 포함한 알림 객체를 모아둔 메서드
    private Notification createNotification(User receiver, NotificationType notificationType,String content,String url){
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }
}
// emitterId : SseEmitter 구분,관리하기 위한 식별자, 리포지토리에 저장되어 특정 클라이언트의 연결 관리하는데 사용
// eventId : 개별 알림 이벤트를 식별하기 위한 고유값, 클라이언트에게 전송될 때 이벤트 식별 위해 사용