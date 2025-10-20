package poly.edu.ass.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.repository.DanhMucRepository;

import java.util.List;

@Controller
public class GlobalController {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @ModelAttribute("danhMucs")
    public List<DanhMuc> getDanhMucs() {
        return danhMucRepository.findAll();
    }
}
