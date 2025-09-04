package lk.vihanganimsara.classsphere.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lk.vihanganimsara.classsphere.entity.HallType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallDto {

    private String hallId;
    private String hallName;
    private String hallType; // AC / Non-AC
    private Integer capacity;
}
