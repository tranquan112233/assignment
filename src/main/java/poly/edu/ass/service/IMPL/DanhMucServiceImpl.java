package poly.edu.ass.service.IMPL;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.repository.DanhMucRepository;
import poly.edu.ass.service.DanhMucService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhMucServiceImpl implements DanhMucService {
    private final DanhMucRepository danhMucRepository;

    @Override
    public List<DanhMuc> getAll() {
        return danhMucRepository.findAll();
    }

    @Override
    public DanhMuc getById(Integer id) {
        return danhMucRepository.findById(id).orElse(null);
    }
}
