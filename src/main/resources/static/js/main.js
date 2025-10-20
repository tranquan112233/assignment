let cart = [];

// Cập nhật giỏ hàng
function addToCart(id, name, price) {
    const item = cart.find(c => c.id === id);
    if (item) item.qty++;
    else cart.push({ id, name, price, qty: 1 });
    updateCart();
}

function updateCartItem(id, delta) {
    const item = cart.find(c => c.id === id);
    if (item) {
        item.qty += delta;
        if (item.qty <= 0) {
            cart = cart.filter(c => c.id !== id);
        }
    }
    updateCart();
}

function removeCartItem(id) {
    cart = cart.filter(c => c.id !== id);
    updateCart();
}

function updateCart() {
    const totalItems = cart.reduce((a, c) => a + c.qty, 0);
    document.getElementById("cartCount").textContent = totalItems;

    const cartItems = document.getElementById("cartItems");
    cartItems.innerHTML = cart.length === 0
        ? '<p class="text-muted text-center">Giỏ hàng trống</p>'
        : cart.map(c => `
            <div class="cart-item d-flex justify-content-between align-items-center border-bottom py-3">
                <div class="d-flex flex-column">
                    <span class="fw-bold">${c.name}</span>
                    <span class="text-muted">${c.price.toLocaleString()}đ / sản phẩm</span>
                </div>
                <div class="d-flex align-items-center">
                    <button class="btn btn-outline-secondary btn-sm me-2" onclick="updateCartItem(${c.id}, -1)">-</button>
                    <span class="px-2">${c.qty}</span>
                    <button class="btn btn-outline-secondary btn-sm me-2" onclick="updateCartItem(${c.id}, 1)">+</button>
                    <button class="btn btn-danger btn-sm" onclick="removeCartItem(${c.id})"><i class="bi bi-trash"></i></button>
                </div>
            </div>
        `).join('');

    const totalPrice = cart.reduce((a, c) => a + c.price * c.qty, 0);
    document.getElementById("cartTotal").textContent = totalPrice.toLocaleString() + 'đ';
}

// Filter search client-side (còn products render từ Thymeleaf)
document.getElementById("searchInput").addEventListener("input", e => {
    const searchValue = e.target.value.toLowerCase();
    document.querySelectorAll("#products .product-card").forEach(card => {
        const name = card.querySelector(".card-title").textContent.toLowerCase();
        card.parentElement.style.display = name.includes(searchValue) ? "block" : "none";
    });
});

// Filter danh mục khi click
document.querySelectorAll("[data-filter]").forEach(el => {
    el.addEventListener("click", e => {
        e.preventDefault();
        document.querySelectorAll("[data-filter]").forEach(nav => nav.classList.remove("active"));
        el.classList.add("active");

        const filter = el.dataset.filter.toLowerCase();
        document.querySelectorAll("#products .product-card").forEach(card => {
            const name = card.querySelector(".card-title").textContent.toLowerCase();
            card.parentElement.style.display = (filter === "*" || name.includes(filter)) ? "block" : "none";
        });
    });
});
window.onpopstate = function(event) {
    // event.state có thể lưu route trước đó
    const path = window.location.pathname;
    // Gọi lại API tương ứng để load dữ liệu
    loadProductsForPath(path);
};

function navigateTo(url) {
    history.pushState({}, '', url);
    loadProductsForPath(url);
}
