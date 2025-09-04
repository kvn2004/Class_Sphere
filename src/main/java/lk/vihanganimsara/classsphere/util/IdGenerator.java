package lk.vihanganimsara.classsphere.util;

import lk.vihanganimsara.classsphere.entity.Enrollment;
import lk.vihanganimsara.classsphere.entity.Hall;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.entity.Teacher;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;


public class IdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String prefix = " ";

        if (object instanceof Student) prefix = "STU";
        else if (object instanceof Teacher) prefix = "TEC";
        else  if (object instanceof Hall) prefix = "Hall";
        //else if (object instanceof ClassEntity) prefix = "CLS";

        return prefix + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

