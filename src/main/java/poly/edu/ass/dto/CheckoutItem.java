package poly.edu.ass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.ass.Entity.SanPham;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class
CheckoutItem {
    private SanPham sanPham;
    private int soLuong;
    private BigDecimal donGia;
}
