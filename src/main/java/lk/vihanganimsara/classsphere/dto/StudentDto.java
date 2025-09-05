package lk.vihanganimsara.classsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String id;
    private String name;
    private String email;
    private String telephone;
    private String address;
    private String guardianName;
    private String guardianTelephone;
    private String guardianEmail;
//    private MultipartFile photo;
}
