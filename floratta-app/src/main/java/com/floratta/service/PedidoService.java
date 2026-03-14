package com.floratta.service;

import com.floratta.model.dto.PedidoDTO;
import com.floratta.model.entity.*;
import com.floratta.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private EmailService emailService;

    @Transactional
    public Pedido crearPedido(PedidoDTO dto, String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setNombreCliente(dto.getNombreCliente());
        pedido.setTelefono(dto.getTelefono());
        pedido.setDireccion(dto.getDireccion());
        pedido.setNotas(dto.getNotas());

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        boolean tieneProducto = false;
        boolean tieneCurso = false;

        for (PedidoDTO.ItemDTO itemDto : dto.getItems()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setCantidad(itemDto.getCantidad());

            if (itemDto.getProductoId() != null) {
                Producto producto = productoRepository.findById(itemDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemDto.getProductoId()));
                if (Boolean.FALSE.equals(producto.getDisponible())) {
                    throw new RuntimeException("Producto no disponible: " + producto.getNombre());
                }
                detalle.setProducto(producto);
                detalle.setPrecioUnitario(producto.getPrecio());
                total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(itemDto.getCantidad())));
                tieneProducto = true;

            } else if (itemDto.getCursoId() != null) {
                Curso curso = cursoRepository.findById(itemDto.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado: " + itemDto.getCursoId()));
                if (curso.getModalidad() == ModalidadCurso.PRESENCIAL
                        && curso.getCupos() != null && curso.getCupos() <= 0) {
                    throw new RuntimeException("Sin cupos: " + curso.getNombre());
                }
                detalle.setCurso(curso);
                detalle.setPrecioUnitario(curso.getPrecio());
                total = total.add(curso.getPrecio());
                tieneCurso = true;
            }
            detalles.add(detalle);
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        if (tieneProducto && tieneCurso) pedido.setTipo(TipoPedido.MIXTO);
        else if (tieneCurso)             pedido.setTipo(TipoPedido.CURSO);
        else                             pedido.setTipo(TipoPedido.PRODUCTO);

        Pedido guardado = pedidoRepository.save(pedido);

        try {
            emailService.enviarConfirmacionPedido(usuario.getCorreo(), guardado);
        } catch (Exception e) {
            System.err.println("Error email: " + e.getMessage());
        }

        return guardado;
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado() == EstadoPedido.PENDIENTE
                && nuevoEstado != EstadoPedido.PENDIENTE
                && nuevoEstado != EstadoPedido.CANCELADO) {
            pedido.getDetalles().stream()
                .filter(d -> d.getCurso() != null)
                .forEach(d -> {
                    Curso curso = d.getCurso();
                    if (curso.getModalidad() == ModalidadCurso.PRESENCIAL
                            && curso.getCupos() != null && curso.getCupos() > 0) {
                        curso.setCupos(curso.getCupos() - 1);
                        cursoRepository.save(curso);
                    }
                });
        }

        pedido.setEstado(nuevoEstado);

        try {
            emailService.enviarCambioEstado(
                pedido.getUsuario().getCorreo(),
                pedido.getId(),
                nuevoEstado.name()
            );
        } catch (Exception e) {
            System.err.println("Error email estado: " + e.getMessage());
        }

        return pedidoRepository.save(pedido);
    }
}