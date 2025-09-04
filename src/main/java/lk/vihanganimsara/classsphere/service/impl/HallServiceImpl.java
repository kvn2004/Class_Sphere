package lk.vihanganimsara.classsphere.service.impl;

import lk.vihanganimsara.classsphere.dto.HallDto;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.entity.Hall;
import lk.vihanganimsara.classsphere.entity.HallType;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.repository.HallRepo;
import lk.vihanganimsara.classsphere.service.HallService;
import lk.vihanganimsara.classsphere.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {
    private final ModelMapper modelMapper;
    private final HallRepo hallRepo;

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
        Hall hall = new Hall();
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
