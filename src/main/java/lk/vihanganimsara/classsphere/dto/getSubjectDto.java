package lk.vihanganimsara.classsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class getSubjectDto {
    private String subjectId;
    private String subjectName;
    private String gradeLevel;
}
