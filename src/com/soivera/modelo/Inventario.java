package com.soivera.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Organiza y consulta el estado general de los productos.
 * Agrega productos (existen por sí mismos) y compone movimientos (solo existen dentro del inventario).
 */
public class Inventario {

    private int idInventario;
    private LocalDate fechaActualizacion;
    private List<Producto> productos;            // agregación
    private List<MovimientosStock> movimientos;  // composición

    public Inventario(int idInventario) {
        this.idInventario = idInventario;
        this.fechaActualizacion = LocalDate.now();
        this.productos = new ArrayList<>();
        this.movimientos = new ArrayList<>();
    }

    /** Agregación: recibe un producto creado fuera del inventario. */
    public void agregarProducto(Producto producto) {
        producto.registrar();
        if (producto.esDuplicado(productos)) {
            throw new IllegalStateException("El producto ya está registrado: " + producto.getNombre());
        }
        productos.add(producto);
        actualizarEnTiempoReal();
    }

    /** Composición: el movimiento se crea dentro del inventario y depende de él. */
    public MovimientosStock agregarMovimiento(Producto producto, int cantidad,
                                              String tipoMovimiento, String justificacion) {
        int idMovimiento = movimientos.size() + 1;
        MovimientosStock movimiento = new MovimientosStock(idMovimiento, LocalDate.now(),
                cantidad, tipoMovimiento, justificacion, producto);
        movimiento.registrar();
        movimientos.add(movimiento);
        actualizarEnTiempoReal();
        return movimiento;
    }

    public List<Producto> listarProductos() {
        return Collections.unmodifiableList(productos);
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        List<Producto> productosEncontrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getCategoria().equalsIgnoreCase(categoria)) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    /** Actualiza la fecha de la última modificación del inventario. */
    public void actualizarEnTiempoReal() {
        fechaActualizacion = LocalDate.now();
    }

    public int getIdInventario() { return idInventario; }
    public void setIdInventario(int idInventario) { this.idInventario = idInventario; }

    public LocalDate getFechaActualizacion() { return fechaActualizacion; }

    public List<MovimientosStock> getMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }
}
