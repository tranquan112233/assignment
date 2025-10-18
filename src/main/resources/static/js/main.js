// const products = [
//     { id: 1, name: "Intel Core i9", price: 12000000, category: "cpu intel" },
//     { id: 2, name: "AMD Ryzen 9", price: 11000000, category: "cpu amd" },
//     { id: 3, name: "NVIDIA RTX 4080", price: 35000000, category: "gpu nvidia" },
//     { id: 4, name: "AMD RX 7900 XT", price: 28000000, category: "gpu amd-gpu" },
//     { id: 5, name: "Corsair Vengeance 16GB DDR4", price: 2500000, category: "ram ddr4" },
//     { id: 6, name: "G.Skill Trident Z 32GB DDR5", price: 5500000, category: "ram ddr5" },
//     { id: 7, name: "Samsung 980 PRO 1TB NVMe", price: 3500000, category: "ssd nvme" },
//     { id: 8, name: "Seagate Barracuda 2TB HDD", price: 1800000, category: "ssd hdd" },
//     { id: 9, name: "Intel Core i7", price: 9000000, category: "cpu intel" },
//     { id: 10, name: "AMD Ryzen 7", price: 8500000, category: "cpu amd" },
//     { id: 11, name: "NVIDIA RTX 4070", price: 25000000, category: "gpu nvidia" },
//     { id: 12, name: "AMD RX 7800 XT", price: 22000000, category: "gpu amd-gpu" },
//     { id: 13, name: "Kingston Fury 16GB DDR4", price: 2400000, category: "ram ddr4" },
//     { id: 14, name: "TeamGroup T-Force 32GB DDR5", price: 5300000, category: "ram ddr5" },
//     { id: 15, name: "WD Black SN850X 1TB NVMe", price: 3700000, category: "ssd nvme" },
//     { id: 16, name: "Toshiba 2TB HDD", price: 1700000, category: "ssd hdd" },
//     { id: 17, name: "Intel Core i5", price: 6000000, category: "cpu intel" },
//     { id: 18, name: "AMD Ryzen 5", price: 5800000, category: "cpu amd" },
//     { id: 19, name: "NVIDIA RTX 4060", price: 18000000, category: "gpu nvidia" },
//     { id: 20, name: "AMD RX 7600", price: 16000000, category: "gpu amd-gpu" },
// ];

const categories = ["cpu intel", "cpu amd", "gpu nvidia", "gpu amd-gpu", "ram ddr4", "ram ddr5", "ssd nvme", "ssd hdd"];
const products = Array.from({ length: 1000 }, (_, i) => {
    const id = i + 1;
    const category = categories[i % categories.length];
    const name = `${category.toUpperCase()} Model ${id}`;
    const price = Math.floor(Math.random() * 30000000) + 1000000;
    return { id, name, price, category };
});
let cart = [];
let currentPage = 1;
const itemsPerPage = 25;

function renderProducts(filter = "*", search = "") {
    const container = document.getElementById("products");
    container.innerHTML = "";

    const filtered = products.filter(p =>
        (filter === "*" || p.category.includes(filter)) &&
        p.name.toLowerCase().includes(search.toLowerCase())
    );

    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const paginated = filtered.slice(start, end);

    paginated.forEach(p => {
        const col = document.createElement("div");
        col.className = "col-sm-6 col-md-4 col-lg-3";
        col.innerHTML = `
      <div class="card product-card h-100">
        <div class="card-body text-center d-flex flex-column">
          <h5 class="card-title">${p.name}</h5>
          <p class="card-text fw-bold">${p.price.toLocaleString()}đ</p>
          <button class="btn btn-dark mt-auto" onclick="addToCart(${p.id})">Thêm vào giỏ</button>
        </div>
      </div>`;
        container.appendChild(col);
    });

    renderPagination(filtered.length);
}

function renderPagination(totalItems) {
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const container = document.getElementById("products");
    const pagination = document.createElement("div");
    pagination.className = "d-flex justify-content-center mt-4";

    pagination.innerHTML = `
    <nav>
      <ul class="pagination">
        ${Array.from({ length: totalPages }, (_, i) => `
          <li class="page-item ${currentPage === i + 1 ? "active" : ""}">
            <a class="page-link" href="#" onclick="goToPage(${i + 1})">${i + 1}</a>
          </li>`).join('')}
      </ul>
    </nav>
  `;

    container.appendChild(pagination);
}

function goToPage(page) {
    currentPage = page;
    const activeFilter = document.querySelector("[data-filter].active")?.dataset.filter || "*";
    const search = document.getElementById("searchInput").value;
    renderProducts(activeFilter, search);
}

function addToCart(id) {
    const product = products.find(p => p.id === id);
    const item = cart.find(c => c.id === id);
    if (item) item.qty++;
    else cart.push({ ...product, qty: 1 });
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
      </div>`).join('');

    const totalPrice = cart.reduce((a, c) => a + c.price * c.qty, 0);
    document.getElementById("cartTotal").textContent = totalPrice.toLocaleString() + 'đ';
}

document.querySelectorAll("[data-filter]").forEach(el => {
    el.addEventListener("click", e => {
        e.preventDefault();
        document.querySelectorAll("[data-filter]").forEach(nav => nav.classList.remove("active"));
        el.classList.add("active");
        currentPage = 1;
        renderProducts(el.dataset.filter, document.getElementById("searchInput").value);
    });
});

document.getElementById("searchInput").addEventListener("input", e => {
    currentPage = 1;
    const activeFilter = document.querySelector("[data-filter].active")?.dataset.filter || "*";
    renderProducts(activeFilter, e.target.value);
});

renderProducts();