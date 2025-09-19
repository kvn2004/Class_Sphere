package lk.vihanganimsara.classsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnrollmentDto {
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enrolledOn;
    private Boolean active;
}
