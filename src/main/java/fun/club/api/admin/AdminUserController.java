package fun.club.api.admin;

import fun.club.common.response.UserInfoByAdminResponse;
import fun.club.common.util.SuccessResponse;
import fun.club.core.admin.domain.AdminUser;
import fun.club.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PostMapping("/assign-admin/{userId}")
    @PreAuthorize("hasRole('ADMIN')") // ADMIN 만이 api url 접근 가능
    public ResponseEntity<?> assignAdminRole(@PathVariable Long userId) {
          userService.assignAdminRole(userId);
        SuccessResponse response = new SuccessResponse(true,"권한 부여 성공",userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
