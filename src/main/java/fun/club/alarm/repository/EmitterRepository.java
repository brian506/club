package fun.club.alarm.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Repository
public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String emitterId,Object event);
    Map<String,SseEmitter> findAllEmitterStartWithByMemberId(String userId); // 브라우저당 여러개 연결이 가능하기 때문에 여러 Emitter 가 존재할 수 있다.
    Map<String, Object> findAllEventCacheStartWithByMemberId(String userId);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String userId);
    void deleteAllEventCacheStartWithId(String userId);

}
/**
 * 메모리 내의 맵을 이용하여 데이터를 직접 관리하고 있기 때문에 직접 CRUD 기능을 구현
 *
 * Map 형식으로 저장하는 이유?
 * 1. 수신자별로 여러 개의 SseEmitter 객체를 관리하는데, 각 수신자마다 다양한 클라이언트에서의 연결을 지원하기 위해 Map 형식으로 저장
 * 2. 특정 수신자에 대한 식별자를 통해 쉽게 검색하고 접근 가능
 * => 수신자별로 다중 연결과 동시성 문제를 해결하고, 식별자 기반의 검색과 제거를 효율적으로 수행하기 위한 목적
 */


/**
 * eventCache : 클라이언트가 연결을 잃어도 이벤트 유실을 방지하기 위해 임시로 저장되는 데이터
 *
 * emitters : 클라이언트가 구독을 요청하면 해당 사용자의 식별자를 키로 사용하여 맵에 저장되고, 알림을 전송할 때마다 해당 사용자의 SseEmitter 를 조회하기 위해 사용
 *
 * concurrentHashMap : 동시에 여러 스레드가 접근하더라도 안전하게 데이터를 조작할 수 있도록 해줌
 */