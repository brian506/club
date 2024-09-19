package fun.club.service.attendance;


import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final UserRepository userRepository;

    private Set<Long> presentUsers = new HashSet<>(); // 중복된 ID 가 출석하지 않게 Set 으로 유저를 받아온다.

    public void markAttendance(Long userId) { // api 에서 출첵한 유저를 presentUsers 에 넣어준다. (출첵한 유저 확인)
        presentUsers.add(userId);
    }
    public void applyPenalty(){ // 모든 유저 정보를 가져와서 presentUsers(출책한) 와 비교해서 없으면 벌점을 매긴다
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!presentUsers.contains(user.getId())) {
                user.setAbsencePoint(user.getAbsencePoint() + 1);
                userRepository.save(user);
            }
        }
        clear();
    }
    private void clear(){ // 다음 출첵을 위해서 초기화한다.
        presentUsers.clear();
    }

    }

