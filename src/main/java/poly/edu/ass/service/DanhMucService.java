package poly.edu.ass.service;

import poly.edu.ass.Entity.DanhMuc;

import java.util.List;

public interface DanhMucService {
    List<DanhMuc> getAll();
    DanhMuc getById(Integer id);
}
