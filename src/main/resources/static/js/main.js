/* =========================================================
 * CART (Bootstrap Offcanvas) – DB first, Local fallback
 * ========================================================= */
const USER_ID = window.USER_ID || 3; // TODO: gắn từ session khi đăng nhập
const API = {
    get:   (uid = USER_ID)              => fetch(`/api/cart?userId=${uid}`),
    add:   (pid, qty = 1, uid = USER_ID)=> fetch(`/api/cart/add?userId=${uid}&productId=${pid}&qty=${qty}`, { method: 'POST' }),
    qty:   (cartId, qty, uid = USER_ID) => fetch(`/api/cart/${cartId}/qty?userId=${uid}&qty=${qty}`, { method: 'PATCH' }),
    del:   (cartId, uid = USER_ID)      => fetch(`/api/cart/${cartId}?userId=${uid}`, { method: 'DELETE' }),
    clear: (uid = USER_ID)              => fetch(`/api/cart/clear?userId=${uid}`, { method: 'DELETE' }),
    checkout: (addrId = 1, uid = USER_ID) => fetch(`/api/cart/checkout?userId=${uid}&diaChiId=${addrId}`, { method: 'POST' })
};

const $  = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
const money = v => (Number(v) || 0).toLocaleString('vi-VN') + 'đ';

// State local fallback: [{id, name, price, qty}]
let cartLocal = [];

/* ============================
 * OFFCANVAS HELPERS
 * ============================ */
function openOffcanvas() {
    const el = $('#cartOffcanvas');
    if (!el) return;
    const oc = bootstrap.Offcanvas.getOrCreateInstance(el);
    oc.show();
}
function closeOffcanvas() {
    const el = $('#cartOffcanvas');
    if (!el) return;
    bootstrap.Offcanvas.getInstance(el)?.hide();
}

/* ============================
 * RENDER CART (SERVER or LOCAL)
 * ============================ */
function renderCart(payload, isLocal = false) {
    const list = isLocal ? (cartLocal || []) : (payload.items || []);
    const count = isLocal
        ? list.reduce((a, c) => a + (c.qty || 0), 0)
        : (payload.count ?? list.reduce((a, c) => a + (c.soLuong || 0), 0));
    const subtotal = isLocal
        ? list.reduce((a, c) => a + (Number(c.price) * Number(c.qty)), 0)
        : Number(payload.subtotal || 0);

    const body = $('#cartBody');
    const countEl = $('#cartCount');
    const subtotalEl = $('#cartSubtotal');

    if (countEl) countEl.textContent = count;
    if (subtotalEl) subtotalEl.textContent = money(subtotal);

    if (!body) return;

    if (list.length === 0) {
        body.innerHTML = `<p class="text-muted text-center my-4">Giỏ hàng trống</p>`;
        return;
    }

    if (!isLocal) {
        // Server items: {cartId,productId,tenSP,hinhAnh,donGia,soLuong,lineTotal}
        body.innerHTML = list.map(it => `
      <div class="d-flex align-items-start gap-3 py-3 border-bottom border-secondary cart-row" data-cart-id="${it.cartId}">
        <img src="/photos/${it.hinhAnh || 'noimg.jpg'}" class="rounded"
             style="width:72px;height:72px;object-fit:cover;border:1px solid rgba(255,255,255,.15)">
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between">
            <div class="me-2">
              <div class="fw-semibold">${it.tenSP}</div>
              <div class="text-secondary small">${money(it.donGia)} / sản phẩm</div>
            </div>
            <div class="fw-bold text-danger">${money(it.lineTotal)}</div>
          </div>

          <div class="d-flex align-items-center gap-2 mt-2">
            <button class="btn btn-outline-secondary btn-sm"
                    data-action="dec" data-cart-id="${it.cartId}">−</button>

            <input class="form-control form-control-sm text-bg-dark border-secondary text-center qty-input"
                   style="width:64px" value="${it.soLuong}"
                   data-cart-id="${it.cartId}" aria-label="Số lượng">

            <button class="btn btn-outline-secondary btn-sm"
                    data-action="inc" data-cart-id="${it.cartId}">+</button>

            <button class="btn btn-outline-danger btn-sm ms-2"
                    data-action="remove" data-cart-id="${it.cartId}" title="Xoá">
              <i class="bi bi-trash3"></i>
            </button>
          </div>
        </div>
      </div>
    `).join('');
    } else {
        // Local items: {id, name, price, qty}
        body.innerHTML = list.map(c => `
      <div class="d-flex align-items-start gap-3 py-3 border-bottom border-secondary cart-row" data-prod-id="${c.id}">
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between">
            <div class="me-2">
              <div class="fw-semibold">${c.name}</div>
              <div class="text-secondary small">${money(c.price)} / sản phẩm</div>
            </div>
            <div class="fw-bold text-danger">${money(c.price * c.qty)}</div>
          </div>

          <div class="d-flex align-items-center gap-2 mt-2">
            <button class="btn btn-outline-secondary btn-sm"
                    data-action="dec-local" data-prod-id="${c.id}">−</button>

            <input class="form-control form-control-sm text-bg-dark border-secondary text-center qty-input-local"
                   style="width:64px" value="${c.qty}"
                   data-prod-id="${c.id}" aria-label="Số lượng">

            <button class="btn btn-outline-secondary btn-sm"
                    data-action="inc-local" data-prod-id="${c.id}">+</button>

            <button class="btn btn-outline-danger btn-sm ms-2"
                    data-action="remove-local" data-prod-id="${c.id}" title="Xoá">
              <i class="bi bi-trash3"></i>
            </button>
          </div>
        </div>
      </div>
    `).join('');
    }

    // Gắn handler một lần cho container (event delegation)
    attachCartHandlers(isLocal);
}

/* ============================
 * EVENT HANDLERS (delegation)
 * ============================ */
function attachCartHandlers(isLocal) {
    const body = $('#cartBody');
    if (!body) return;

    body.onclick = async (e) => {
        const btn = e.target.closest('button');
        if (!btn) return;

        // SERVER
        if (btn.dataset.action === 'inc') {
            const id = +btn.dataset.cartId;
            const input = $(`input.qty-input[data-cart-id="${id}"]`);
            const cur = Math.max(1, parseInt(input?.value || '1', 10));
            await API.qty(id, cur + 1);
            refreshCart();
        }
        if (btn.dataset.action === 'dec') {
            const id = +btn.dataset.cartId;
            const input = $(`input.qty-input[data-cart-id="${id}"]`);
            const cur = Math.max(1, parseInt(input?.value || '1', 10));
            const next = Math.max(1, cur - 1);
            await API.qty(id, next);
            refreshCart();
        }
        if (btn.dataset.action === 'remove') {
            const id = +btn.dataset.cartId;
            await API.del(id);
            refreshCart();
        }

        // LOCAL
        if (btn.dataset.action === 'inc-local') {
            const id = +btn.dataset.prodId;
            const it = cartLocal.find(c => c.id === id);
            if (it) it.qty++;
            renderCart({}, true);
        }
        if (btn.dataset.action === 'dec-local') {
            const id = +btn.dataset.prodId;
            const it = cartLocal.find(c => c.id === id);
            if (it) {
                it.qty = Math.max(1, it.qty - 1);
                renderCart({}, true);
            }
        }
        if (btn.dataset.action === 'remove-local') {
            const id = +btn.dataset.prodId;
            cartLocal = cartLocal.filter(c => c.id !== id);
            renderCart({}, true);
        }
    };

    body.onchange = async (e) => {
        const input = e.target.closest('input.qty-input, input.qty-input-local');
        if (!input) return;

        const val = Math.max(1, parseInt(input.value || '1', 10));

        // SERVER
        if (input.classList.contains('qty-input')) {
            const id = +input.dataset.cartId;
            await API.qty(id, val);
            refreshCart();
        }
        // LOCAL
        if (input.classList.contains('qty-input-local')) {
            const id = +input.dataset.prodId;
            const it = cartLocal.find(c => c.id === id);
            if (it) it.qty = val;
            renderCart({}, true);
        }
    };
}

/* ============================
 * LOAD + SYNC
 * ============================ */
async function refreshCart() {
    try {
        const r = await API.get();
        if (!r.ok) throw new Error();
        const data = await r.json();
        renderCart(data, false);
    } catch (e) {
        // fallback local
        renderCart({}, true);
    }
}

/* ============================
 * PUBLIC ACTIONS
 * ============================ */
async function addToCart(productId, name, price) {
    try {
        await API.add(productId, 1);
        await refreshCart();
        openOffcanvas();
        showAddedToast();
    } catch {
        // fallback local
        const it = cartLocal.find(c => c.id === Number(productId));
        if (it) it.qty++;
        else cartLocal.push({ id: Number(productId), name, price: Number(price), qty: 1 });
        renderCart({}, true);
        openOffcanvas();
    }
}

async function clearCart() {
    try {
        const r = await fetch(`/api/cart/clear?userId=${USER_ID}`, { method: 'DELETE' });
        if (!r.ok) throw new Error('DELETE not allowed');
        await refreshCart();
    } catch {
        await fetch(`/api/cart/clear?userId=${USER_ID}`, { method: 'POST' });
        await refreshCart();
    }
}



// expose global để onclick="clearCart()" gọi được



async function checkout() {
    try {
        const res = await API.checkout(1);
        if (!res.ok) throw new Error(await res.text());
        const data = await res.json();
        alert(`Đặt hàng thành công! Mã: ${data.orderId} - Tổng: ${money(data.total)}`);
        await refreshCart();
        closeOffcanvas();
    } catch (e) {
        alert(typeof e === 'string' ? e : 'Thanh toán thất bại');
    }
}

/* ============================
 * SMALL UI BITS
 * ============================ */
function showAddedToast() {
    const a = $('#cartAlert');
    if (!a) return;
    a.classList.remove('d-none');
    setTimeout(() => a.classList.add('d-none'), 1500);
}

/* ============================
 * SEARCH & FILTER (giữ nguyên)
 * ============================ */
const searchInput = $('#searchInput');
if (searchInput) {
    searchInput.addEventListener('input', e => {
        const q = (e.target.value || '').toLowerCase();
        $$('#products .product-card').forEach(card => {
            const name = (card.querySelector('.card-title')?.textContent || '').toLowerCase();
            card.parentElement.style.display = name.includes(q) ? 'block' : 'none';
        });
    });
}

$$('[data-filter]').forEach(el => {
    el.addEventListener('click', e => {
        e.preventDefault();
        $$('[data-filter]').forEach(n => n.classList.remove('active'));
        el.classList.add('active');
        const filter = (el.dataset.filter || '').toLowerCase();
        $$('#products .product-card').forEach(card => {
            const name = (card.querySelector('.card-title')?.textContent || '').toLowerCase();
            card.parentElement.style.display = (filter === '*' || name.includes(filter)) ? 'block' : 'none';
        });
    });
});

/* ============================
 * ROUTING
 * ============================ */
window.onpopstate = null;
function navigateTo(url) { window.location.assign(url); }

/* ============================
 * INIT
 * ============================ */
document.addEventListener('DOMContentLoaded', () => {
    // auto refresh khi mở offcanvas
    $('#cartOffcanvas')?.addEventListener('shown.bs.offcanvas', refreshCart);
    refreshCart();
});
async function addToCartWithQty(productId) {
    const qty = Math.max(1, parseInt(document.getElementById('buyQty')?.value || '1', 10));
    try {
        const res = await API.add(productId, qty);
        if (!res.ok) throw new Error(await res.text());
        await refreshCart();
        // mở giỏ cho thấy đã thêm
        const el = document.getElementById('cartOffcanvas');
        if (el) bootstrap.Offcanvas.getOrCreateInstance(el).show();
        showAddedToast();
    } catch (e) {
        // fallback local nếu server lỗi
        const it = cartLocal.find(c => c.id === Number(productId));
        if (it) it.qty += qty; else cartLocal.push({ id: Number(productId), name: 'SP', price: 0, qty });
        renderCart({}, true);
        const el = document.getElementById('cartOffcanvas');
        if (el) bootstrap.Offcanvas.getOrCreateInstance(el).show();
        console.error('ADD failed:', e);
    }
}
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('cartOffcanvas')
        ?.addEventListener('shown.bs.offcanvas', refreshCart);
});
function goCheckout() {
    window.location.href = `/checkout?userId=${USER_ID}`;
}
window.goCheckout = goCheckout;

async function buyNow(productId, name, price) {
    // +1 vào giỏ rồi chuyển checkout
    try { await API.add(productId, 1); } catch {}
    goCheckout();
}

async function checkout() {
    try {
        const res = await API.checkout(1);
        if (!res.ok) throw new Error(await res.text());
        const data = await res.json();
        window.location.href = `/order-success?orderId=${encodeURIComponent(data.orderId)}`;
    } catch (e) {
        alert('Thanh toán thất bại');
    }
}


// Expose nếu cần gọi từ HTML
window.addToCart = addToCart;
window.clearCart = clearCart;
window.checkout = checkout;
window.buyNow = buyNow;
window.addToCartWithQty = addToCartWithQty; // expose global

