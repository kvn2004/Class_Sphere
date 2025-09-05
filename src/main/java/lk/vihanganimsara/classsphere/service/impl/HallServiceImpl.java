package lk.vihanganimsara.classsphere.service.impl;

import jakarta.transaction.Transactional;
import lk.vihanganimsara.classsphere.dto.HallDto;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.entity.Hall;
import lk.vihanganimsara.classsphere.entity.HallType;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.repository.HallRepo;
import lk.vihanganimsara.classsphere.service.AuditLogsService;
import lk.vihanganimsara.classsphere.service.HallService;
import lk.vihanganimsara.classsphere.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HallServiceImpl implements HallService {
    private final ModelMapper modelMapper;
    private final HallRepo hallRepo;
    private final AuditLogsService auditLogsService;

    @Override
    public void save(HallDto hallDto) {
        Hall hall = new Hall();
        hall.setHallName(hallDto.getHallName());
        hall.setCapacity(hallDto.getCapacity());
        hall.setHallType(HallType.valueOf(hallDto.getHallType()));

        hallRepo.save(hall);
    }

    @Override
    public void update(HallDto hallDto) {
        Optional<Hall> byId = hallRepo.findById(hallDto.getHallId());
        if(!byId.isPresent()) {
            log.warn("Hall with id {} not found", hallDto.getHallId());
            throw new RuntimeException("Hall not found");
        }
        Hall hall = byId.get();
        hall.setHallName(hallDto.getHallName());
        hall.setCapacity(hallDto.getCapacity());
        hall.setHallType(HallType.valueOf(hallDto.getHallType()));

        hallRepo.save(hall);
    }

    @Override
    public List<HallDto> getAll() {
        List<Hall> all = hallRepo.findAll();
        return modelMapper.map(all, new TypeToken<List<HallDto>>() {
        }.getType());
    }

    @Override
    public void deleteById(String id) {

        hallRepo.deleteById(id);
    }
}
