
const CARRITO_KEY = 'floratta_carrito';

function obtenerCarrito() {
  try {
    return JSON.parse(localStorage.getItem(CARRITO_KEY)) || [];
  } catch { return []; }
}

function guardarCarrito(items) {
  localStorage.setItem(CARRITO_KEY, JSON.stringify(items));
  actualizarContadorCarrito();
}

function agregarAlCarrito(item) {
  const token = localStorage.getItem('token');
  if (!token) {
    if (confirm('Debes iniciar sesión para comprar. ¿Ir al login?')) {
      window.location.href = '/login';
    }
    return;
  }

  const carrito = obtenerCarrito();
  const key = item.tipo + '_' + item.id;
  const existente = carrito.find(i => i.tipo + '_' + i.id === key);

  if (existente) {
    if (item.tipo === 'PRODUCTO') {
      existente.cantidad = (existente.cantidad || 1) + 1;
    }

  } else {
    carrito.push({ ...item, cantidad: item.tipo === 'CURSO' ? 1 : (item.cantidad || 1) });
  }

  guardarCarrito(carrito);
  mostrarToastCarrito(item.nombre);
}

function quitarDelCarrito(key) {
  const carrito = obtenerCarrito().filter(i => (i.tipo + '_' + i.id) !== key);
  guardarCarrito(carrito);
  renderizarCarritoModal();
}

function cambiarCantidad(key, delta) {
  const carrito = obtenerCarrito();
  const item = carrito.find(i => (i.tipo + '_' + i.id) === key);
  if (item && item.tipo === 'PRODUCTO') {
    item.cantidad = Math.max(1, (item.cantidad || 1) + delta);
    guardarCarrito(carrito);
    renderizarCarritoModal();
  }
}

function actualizarContadorCarrito() {
  const total = obtenerCarrito().reduce((s, i) => s + (i.tipo === 'PRODUCTO' ? (i.cantidad || 1) : 1), 0);
  document.querySelectorAll('.carrito-contador').forEach(el => {
    el.textContent = total;
    el.style.display = total > 0 ? 'inline-flex' : 'none';
  });
}

function calcularSubtotal() {
  return obtenerCarrito().reduce((s, i) => s + (parseFloat(i.precio) * (i.tipo === 'PRODUCTO' ? (i.cantidad || 1) : 1)), 0);
}

function formatearPrecio(valor) {
  return '$' + parseFloat(valor).toLocaleString('es-CO');
}


function abrirCarrito() {
  let modal = document.getElementById('modal-carrito');
  if (!modal) crearModalCarrito();
  renderizarCarritoModal();
  document.getElementById('modal-carrito').style.display = 'flex';
}

function cerrarCarrito() {
  const modal = document.getElementById('modal-carrito');
  if (modal) modal.style.display = 'none';
}

function crearModalCarrito() {
  const modal = document.createElement('div');
  modal.id = 'modal-carrito';
  modal.style.cssText = 'display:none;position:fixed;inset:0;background:rgba(0,0,0,0.5);z-index:9999;align-items:center;justify-content:center;';
  modal.innerHTML = `
    <div style="background:#fff;border-radius:12px;width:90%;max-width:560px;max-height:90vh;overflow-y:auto;padding:24px;position:relative;">
      <button onclick="cerrarCarrito()" style="position:absolute;top:16px;right:16px;background:none;border:none;font-size:20px;cursor:pointer;color:#666;">&times;</button>
      <h2 style="font-family:'Cormorant Garamond',serif;color:#2d4a3e;margin-bottom:16px;">Tu carrito</h2>
      <div id="carrito-items"></div>
      <div id="carrito-footer"></div>
    </div>`;
  document.body.appendChild(modal);
  modal.addEventListener('click', e => { if (e.target === modal) cerrarCarrito(); });
}

function renderizarCarritoModal() {
  const carrito = obtenerCarrito();
  const itemsEl = document.getElementById('carrito-items');
  const footerEl = document.getElementById('carrito-footer');
  if (!itemsEl) return;

  if (carrito.length === 0) {
    itemsEl.innerHTML = '<p style="color:#888;text-align:center;padding:24px 0;">El carrito está vacío</p>';
    footerEl.innerHTML = '';
    return;
  }

  itemsEl.innerHTML = carrito.map(item => {
    const key = item.tipo + '_' + item.id;
    const subtotalItem = parseFloat(item.precio) * (item.tipo === 'PRODUCTO' ? (item.cantidad || 1) : 1);
    return `
      <div style="display:flex;gap:12px;align-items:center;padding:12px 0;border-bottom:1px solid #eee;">
        <img src="${item.imagen || '/images/placeholder.jpg'}" style="width:56px;height:56px;object-fit:cover;border-radius:8px;">
        <div style="flex:1;">
          <div style="font-weight:500;color:#2d4a3e;">${item.nombre}</div>
          <div style="font-size:13px;color:#888;">${item.tipo === 'CURSO' ? 'Curso' : 'Producto'} · ${formatearPrecio(item.precio)}</div>
          ${item.tipo === 'PRODUCTO' ? `
            <div style="display:flex;align-items:center;gap:8px;margin-top:4px;">
              <button onclick="cambiarCantidad('${key}', -1)" style="width:24px;height:24px;border:1px solid #ccc;border-radius:4px;background:none;cursor:pointer;">−</button>
              <span>${item.cantidad || 1}</span>
              <button onclick="cambiarCantidad('${key}', 1)" style="width:24px;height:24px;border:1px solid #ccc;border-radius:4px;background:none;cursor:pointer;">+</button>
            </div>` : ''}
        </div>
        <div style="text-align:right;">
          <div style="font-weight:500;">${formatearPrecio(subtotalItem)}</div>
          <button onclick="quitarDelCarrito('${key}')" style="font-size:12px;color:#e74c3c;background:none;border:none;cursor:pointer;margin-top:4px;">Quitar</button>
        </div>
      </div>`;
  }).join('');

  const subtotal = calcularSubtotal();
  footerEl.innerHTML = `
    <div style="padding-top:16px;">
      <div style="display:flex;justify-content:space-between;font-size:18px;font-weight:500;color:#2d4a3e;margin-bottom:16px;">
        <span>Total</span><span>${formatearPrecio(subtotal)}</span>
      </div>
      <button onclick="abrirCheckout()" style="width:100%;padding:14px;background:#2d4a3e;color:#fff;border:none;border-radius:8px;font-size:16px;cursor:pointer;font-family:'Jost',sans-serif;">
        Proceder al pago
      </button>
    </div>`;
}

function abrirCheckout() {
  cerrarCarrito();
  let modal = document.getElementById('modal-checkout');
  if (!modal) crearModalCheckout();
  document.getElementById('modal-checkout').style.display = 'flex';
}

function cerrarCheckout() {
  const modal = document.getElementById('modal-checkout');
  if (modal) modal.style.display = 'none';
}

function crearModalCheckout() {
  const modal = document.createElement('div');
  modal.id = 'modal-checkout';
  modal.style.cssText = 'display:none;position:fixed;inset:0;background:rgba(0,0,0,0.5);z-index:9999;align-items:center;justify-content:center;';
  modal.innerHTML = `
    <div style="background:#fff;border-radius:12px;width:90%;max-width:480px;max-height:90vh;overflow-y:auto;padding:24px;position:relative;">
      <button onclick="cerrarCheckout()" style="position:absolute;top:16px;right:16px;background:none;border:none;font-size:20px;cursor:pointer;color:#666;">&times;</button>
      <h2 style="font-family:'Cormorant Garamond',serif;color:#2d4a3e;margin-bottom:20px;">Datos del pedido</h2>
      <div style="display:flex;flex-direction:column;gap:14px;">
        <div>
          <label style="font-size:13px;color:#555;display:block;margin-bottom:4px;">Nombre completo *</label>
          <input id="co-nombre" type="text" placeholder="Tu nombre" style="width:100%;padding:10px 12px;border:1px solid #ddd;border-radius:8px;font-size:15px;box-sizing:border-box;">
        </div>
        <div>
          <label style="font-size:13px;color:#555;display:block;margin-bottom:4px;">Teléfono *</label>
          <input id="co-telefono" type="tel" placeholder="Ej: 3194122610" style="width:100%;padding:10px 12px;border:1px solid #ddd;border-radius:8px;font-size:15px;box-sizing:border-box;">
        </div>
        <div>
          <label style="font-size:13px;color:#555;display:block;margin-bottom:4px;">Dirección de entrega *</label>
          <input id="co-direccion" type="text" placeholder="Calle, barrio, ciudad" style="width:100%;padding:10px 12px;border:1px solid #ddd;border-radius:8px;font-size:15px;box-sizing:border-box;">
        </div>
        <div>
          <label style="font-size:13px;color:#555;display:block;margin-bottom:4px;">Notas adicionales</label>
          <textarea id="co-notas" placeholder="Indicaciones especiales, alergias, etc." rows="3" style="width:100%;padding:10px 12px;border:1px solid #ddd;border-radius:8px;font-size:15px;box-sizing:border-box;resize:vertical;"></textarea>
        </div>
        <div style="background:#f8f9f0;border-radius:8px;padding:12px;font-size:14px;color:#2d4a3e;">
          <strong>Total a pagar: ${formatearPrecio(calcularSubtotal())}</strong>
        </div>
        <div id="co-error" style="color:#e74c3c;font-size:13px;display:none;"></div>
        <button onclick="enviarPedido()" id="btn-confirmar" style="padding:14px;background:#2d4a3e;color:#fff;border:none;border-radius:8px;font-size:16px;cursor:pointer;font-family:'Jost',sans-serif;">
          Confirmar pedido
        </button>
      </div>
    </div>`;
  document.body.appendChild(modal);
  modal.addEventListener('click', e => { if (e.target === modal) cerrarCheckout(); });
}

async function enviarPedido() {
  const nombre = document.getElementById('co-nombre').value.trim();
  const telefono = document.getElementById('co-telefono').value.trim();
  const direccion = document.getElementById('co-direccion').value.trim();
  const notas = document.getElementById('co-notas').value.trim();
  const errorEl = document.getElementById('co-error');
  const btnConfirmar = document.getElementById('btn-confirmar');

  if (!nombre || !telefono || !direccion) {
    errorEl.textContent = 'Por favor completa los campos obligatorios.';
    errorEl.style.display = 'block';
    return;
  }

  const carrito = obtenerCarrito();
  if (carrito.length === 0) {
    errorEl.textContent = 'El carrito está vacío.';
    errorEl.style.display = 'block';
    return;
  }

  const items = carrito.map(i => ({
    productoId: i.tipo === 'PRODUCTO' ? i.id : null,
    cursoId: i.tipo === 'CURSO' ? i.id : null,
    cantidad: i.cantidad || 1
  }));

  const token = localStorage.getItem('token');
  btnConfirmar.textContent = 'Enviando...';
  btnConfirmar.disabled = true;
  errorEl.style.display = 'none';

  try {
    const response = await fetch('/api/pedidos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify({ nombreCliente: nombre, telefono, direccion, notas, items })
    });

    const data = await response.json();

    if (response.ok) {
      guardarCarrito([]);
      cerrarCheckout();
      mostrarConfirmacion(data);
    } else {
      errorEl.textContent = data.error || 'Error al procesar el pedido.';
      errorEl.style.display = 'block';
      btnConfirmar.textContent = 'Confirmar pedido';
      btnConfirmar.disabled = false;
    }
  } catch (err) {
    errorEl.textContent = 'Error de conexión. Intenta de nuevo.';
    errorEl.style.display = 'block';
    btnConfirmar.textContent = 'Confirmar pedido';
    btnConfirmar.disabled = false;
  }
}

function mostrarConfirmacion(data) {
  const modal = document.createElement('div');
  modal.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.5);z-index:9999;display:flex;align-items:center;justify-content:center;';
  modal.innerHTML = `
    <div style="background:#fff;border-radius:12px;padding:32px;max-width:400px;text-align:center;">
      <div style="font-size:48px;margin-bottom:16px;">🎂</div>
      <h3 style="font-family:'Cormorant Garamond',serif;color:#2d4a3e;font-size:24px;margin-bottom:8px;">¡Pedido recibido!</h3>
      <p style="color:#555;margin-bottom:8px;">Tu pedido #${data.id} ha sido registrado.</p>
      <p style="color:#555;margin-bottom:16px;">Total: <strong>${formatearPrecio(data.total)}</strong></p>
      <p style="font-size:13px;color:#888;margin-bottom:24px;">Recibirás un correo de confirmación. Nos pondremos en contacto pronto.</p>
      <button onclick="this.closest('div[style]').remove()" style="padding:12px 24px;background:#2d4a3e;color:#fff;border:none;border-radius:8px;cursor:pointer;font-family:'Jost',sans-serif;">
        Cerrar
      </button>
    </div>`;
  document.body.appendChild(modal);
}

function mostrarToastCarrito(nombre) {
  const toast = document.createElement('div');
  toast.style.cssText = 'position:fixed;bottom:24px;right:24px;background:#2d4a3e;color:#fff;padding:12px 20px;border-radius:8px;z-index:9999;font-family:Jost,sans-serif;font-size:14px;opacity:0;transition:opacity 0.3s;';
  toast.textContent = '✓ ' + nombre + ' agregado al carrito';
  document.body.appendChild(toast);
  setTimeout(() => toast.style.opacity = '1', 10);
  setTimeout(() => { toast.style.opacity = '0'; setTimeout(() => toast.remove(), 300); }, 2500);
}


document.addEventListener('DOMContentLoaded', actualizarContadorCarrito);