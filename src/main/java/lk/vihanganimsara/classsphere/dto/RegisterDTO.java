package lk.vihanganimsara.classsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {
    private String name;
    private String username;
    private String password;
    private String role;
}
