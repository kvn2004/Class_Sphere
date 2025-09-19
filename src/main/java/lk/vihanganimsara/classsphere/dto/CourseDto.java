package lk.vihanganimsara.classsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String courseId;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startMonth;
    private Boolean active;

    // only IDs or simple names (avoid heavy object nesting)
    private String subjectId;
    private String subjectName;

    private String teacherId;
    private String teacherName;

    private String defaultHallId;
    private String defaultHallName;
    // NEW fields for fee

    private BigDecimal monthlyFee;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;


}
