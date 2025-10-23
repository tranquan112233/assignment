package poly.edu.ass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import poly.edu.ass.Entity.NguoiDung;
import poly.edu.ass.Entity.Quyen;
import poly.edu.ass.repository.NguoiDungRepository;
import poly.edu.ass.repository.QuyenRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final NguoiDungRepository userRepo;
    private final QuyenRepository roleRepo;
    private final BCryptPasswordEncoder encoder;

    public String registerCustomer(NguoiDung u) {
        // Kiểm tra trùng email
        if (userRepo.existsByEmail(u.getEmail())) {
            return "Email đã được sử dụng!";
        }
        if (userRepo.existsByTenNguoiDung(u.getTenNguoiDung())) {
            return "Tên đã tồn tại!";
        }

        // Mã hóa mật khẩu
        u.setMatKhau(encoder.encode(u.getMatKhau()));

        // Gán quyền CUSTOMER (nếu chưa có thì tự tạo)
        Quyen role = roleRepo.findByTenQuyen("Admin").orElseGet(() -> {
            Quyen q = new Quyen();
            q.setTenQuyen("Admin");
            return roleRepo.save(q);
        });

        u.setQuyen(role);
        userRepo.save(u);

        return "success";
    }

    // helper to create admin/staff for demo
    public NguoiDung ensureRoleUser(String email, String rawPassword, String roleName, String name) {
        Optional<NguoiDung> existing = userRepo.findByEmail(email);
        if (existing.isPresent()) return existing.get();
        NguoiDung u = new NguoiDung();
        u.setEmail(email);
        u.setTenNguoiDung(name);
        u.setMatKhau(encoder.encode(rawPassword));
        Quyen r = roleRepo.findByTenQuyen(roleName).orElseGet(() -> {
            Quyen q = new Quyen();
            q.setTenQuyen(roleName);
            return roleRepo.save(q);
        });
        u.setQuyen(r);
        return userRepo.save(u);
    }

}
