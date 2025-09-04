package lk.vihanganimsara.classsphere.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class EnrollmentId implements Serializable {
    private String studentId;
    private Integer courseId;
}
