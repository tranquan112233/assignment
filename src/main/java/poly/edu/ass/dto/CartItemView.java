package poly.edu.ass.dto;// CartItemView.java
import java.math.BigDecimal;

public record CartItemView(
    Integer cartId,
    Integer productId,
    String tenSP,
    String hinhAnh,
    BigDecimal donGia,
    Integer soLuong,
    BigDecimal lineTotal
) {}
