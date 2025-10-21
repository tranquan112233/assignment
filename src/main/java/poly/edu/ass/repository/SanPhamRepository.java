package poly.edu.ass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.Entity.SanPham;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    // === Method bạn đang gọi trong ProductServiceImpl ===
    List<SanPham> findByDanhMuc(DanhMuc danhMuc);

    // Tuỳ chọn – dùng khi cần truy theo id danh mục
    List<SanPham> findByDanhMuc_MaDanhMuc(Integer maDanhMuc);

    // Tuỳ chọn – lọc sản phẩm thanh lý theo mô tả
    List<SanPham> findByMoTaContainingIgnoreCaseOrMoTaContainingIgnoreCase(String kw1, String kw2);

    @Query("SELECT s FROM SanPham s " +
            "WHERE LOWER(s.tenSP) NOT LIKE %:kw1% " +
            "AND   LOWER(s.tenSP) NOT LIKE %:kw2%")
    List<SanPham> findAllExcludeUsed(@Param("kw1") String kw1,
                                     @Param("kw2") String kw2);

    // Hàng thanh lý: chỉ lấy SP đã sử dụng
    @Query("SELECT s FROM SanPham s " +
            "WHERE LOWER(s.tenSP) LIKE %:kw1% " +
            "   OR LOWER(s.tenSP) LIKE %:kw2%")
    List<SanPham> findAllUsed(@Param("kw1") String kw1,
                              @Param("kw2") String kw2);




        // ===== NEW (chưa qua sử dụng): tên KHÔNG chứa "đã sử dụng"
        @Query("SELECT s FROM SanPham s " +
                "WHERE LOWER(s.tenSP) NOT LIKE %:token% " +
                "ORDER BY s.maSP DESC")
        List<SanPham> findAllNew(@Param("token") String token);

        default List<SanPham> findAllNew() {
            return findAllNew("đã sử dụng");
        }

        // NEW theo danh mục
        @Query("SELECT s FROM SanPham s " +
                "WHERE s.danhMuc.maDanhMuc = :catId " +
                "AND LOWER(s.tenSP) NOT LIKE %:token% " +
                "ORDER BY s.maSP DESC")
        List<SanPham> findNewByDanhMuc(@Param("catId") Integer catId,
                                       @Param("token") String token);

        default List<SanPham> findNewByDanhMuc(Integer catId) {
            return findNewByDanhMuc(catId, "đã sử dụng");
        }

        // ===== CLEARANCE (đã qua sử dụng): tên CÓ chứa "đã sử dụng"
        @Query("SELECT s FROM SanPham s " +
                "WHERE LOWER(s.tenSP) LIKE %:token% " +
                "ORDER BY s.maSP DESC")
        List<SanPham> findAllClearance(@Param("token") String token);

        default List<SanPham> findAllClearance() {
            return findAllClearance("đã sử dụng");
        }

        // CLEARANCE theo danh mục
        @Query("SELECT s FROM SanPham s " +
                "WHERE s.danhMuc.maDanhMuc = :catId " +
                "AND LOWER(s.tenSP) LIKE %:token% " +
                "ORDER BY s.maSP DESC")
        List<SanPham> findClearanceByDanhMuc(@Param("catId") Integer catId,
                                             @Param("token") String token);

        default List<SanPham> findClearanceByDanhMuc(Integer catId) {
            return findClearanceByDanhMuc(catId, "đã sử dụng");
        }
    }



