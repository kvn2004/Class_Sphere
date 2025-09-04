package lk.vihanganimsara.classsphere.repository;

import lk.vihanganimsara.classsphere.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepo extends JpaRepository<Hall,String> {

    void deleteById(String id);
}
