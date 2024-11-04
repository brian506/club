package fun.club.alarm.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository{

    private final Map<String,SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String,Object> eventCache = new ConcurrentHashMap<>();
    // 클라이언트의 ID 가 키값

    // emitterId 로 SseEmitter 객체를 저장하고, 저장된 SseEmitter 반환(클라이언트가 연결될 때 사용)
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String emitterId, Object event) {
        eventCache.put(emitterId, event);
    }

    // userId 로 모든 SseEmitter 를 찾아서 반환
    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllEmitterStartWithId(String userId) {
        emitters.forEach(
                (key,emitter) -> {
                    if (key.startsWith(userId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheStartWithId(String userId) {
        eventCache.forEach(
                (key,emitter) -> {
                    if (key.startsWith(userId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}

/**
 * concurrentHashmap
 * 스레드 안전한 맵
 * 동시성 문제 해결, 맵에 데이터 저장,조회 가능
 */