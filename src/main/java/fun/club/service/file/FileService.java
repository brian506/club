package fun.club.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final String userUploadDir = "/Users/brian/Documents/user_profile/"; // 파일이 저장되는 경로, 맨 뒤에 / 붙여야함
    private final String postUploadDir = "/Users/brian/Documents/post/";

    // 파일 저장
    public String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        // UUID 는 고유한 식별자를 나타내며 파일 이름이 중복되는 것을 방지
        Path path = Paths.get(userUploadDir + fileName);
        // 파일 전체 경로 생성
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        // 업로드된 파일을 로컬 파일 시스템에 복사하여 저장
        // 동일한 파일이 있을 경우 덮어 씀
        return fileName;
    }
    // 게시글 첨부파일 저장
    public String savePostFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(postUploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
    // 파일 삭제
    public void deleteFile(String fileName, boolean isUserFile) throws IOException {
        String directory = isUserFile ? userUploadDir : postUploadDir;
        Path path = Paths.get(directory + fileName);
        Files.deleteIfExists(path);
    }
    // 파일 경로
    public Path getFilePath(String fileName, boolean isUserFile) {
        String directory = isUserFile ? userUploadDir : postUploadDir;
        return Paths.get(directory + fileName);
    }
}
