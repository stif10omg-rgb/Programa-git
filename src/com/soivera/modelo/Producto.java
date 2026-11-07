package com.soivera.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Representa un artículo de cualquiera de los establecimientos de A TIME FOR ME.
 * Cubre el registro de productos (RF01) y el control de vencimiento (RF02).
 */
public class Producto {

    public static final int DIAS_ALERTA_VENCIMIENTO = 30;

    private int idProducto;
    private String nombre;
    private String categoria;
    private double costo;
    private double precioVenta;
    private int stockActual;
    private int stockMinimo;
    private LocalDate fechaVencimiento;

    public Producto(int idProducto, String nombre, String categoria, double costo,
                    double precioVenta, int stockActual, int stockMinimo,
                    LocalDate fechaVencimiento) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.costo = costo;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
    }

    /** Valida los datos obligatorios antes de registrar el producto (RF01). */
    public void registrar() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalStateException("El nombre del producto es obligatorio");
        }
        if (costo <= 0) {
            throw new IllegalStateException("El costo debe ser mayor a cero");
        }
        if (precioVenta < costo) {
            throw new IllegalStateException("El precio de venta no puede ser menor al costo");
        }
    }

    /** Indica si el producto vence dentro de los próximos 30 días (RF02). */
    public boolean verificarVencimiento() {
        if (fechaVencimiento == null) {
            return false; // productos sin vencimiento (ferretería, papelería)
        }
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
        return diasRestantes <= DIAS_ALERTA_VENCIMIENTO;
    }

    /** Indica si hay existencias disponibles para la venta. */
    public boolean tieneStock() {
        return stockActual > 0;
    }

    /** Compara nombre y categoría con los productos ya registrados. */
    public boolean esDuplicado(List<Producto> productosRegistrados) {
        for (Producto productoRegistrado : productosRegistrados) {
            boolean mismoNombre = productoRegistrado.getNombre().equalsIgnoreCase(nombre);
            boolean mismaCategoria = productoRegistrado.getCategoria().equalsIgnoreCase(categoria);
            if (productoRegistrado != this && mismoNombre && mismaCategoria) {
                return true;
            }
        }
        return false;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
