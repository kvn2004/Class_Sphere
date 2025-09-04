package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Hall")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hall {
    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "lk.vihanganimsara.classsphere.util.IdGenerator")
    private String hallId;

    private String hallName;

    @Enumerated(EnumType.STRING)
    private HallType hallType; // AC / Non-AC

    private Integer capacity;

    @OneToMany(mappedBy = "hall")
    private List<CourseSession> sessions = new ArrayList<>();
}


