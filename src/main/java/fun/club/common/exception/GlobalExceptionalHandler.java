package fun.club.common.exception;

import fun.club.common.util.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
// spring mvc에서 전역 예외처리, 전역 데이터 바인딩,전역 모델 객체 정의를 도와주는 어노테이션
public class GlobalExceptionalHandler {
    // 전역에서 발생하는 예외를 처리하는 핸들러로 사용한다.

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<SuccessResponse> handleGenericException(Exception e) {
        SuccessResponse response = new SuccessResponse(false,"오류 발생 : " +e.getMessage(),null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
