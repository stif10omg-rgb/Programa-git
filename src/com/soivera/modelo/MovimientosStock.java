package com.soivera.modelo;

import java.time.LocalDate;

/**
 * Registra las entradas y salidas de existencias de un producto.
 * Cubre la actualización de stock (RF03) y las bajas con motivo (RF04).
 */
public class MovimientosStock {

    public static final String TIPO_ENTRADA = "ENTRADA";
    public static final String TIPO_VENTA = "VENTA";
    public static final String TIPO_BAJA = "BAJA";

    private int idMovimiento;
    private LocalDate fecha;
    private int cantidad;
    private String tipoMovimiento;
    private String justificacion;
    private Producto producto; // asociación con Producto

    public MovimientosStock(int idMovimiento, LocalDate fecha, int cantidad,
                            String tipoMovimiento, String justificacion, Producto producto) {
        this.idMovimiento = idMovimiento;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.justificacion = justificacion;
        this.producto = producto;
    }

    /** Clasifica el efecto del movimiento: ENTRADA suma stock, SALIDA lo resta. */
    public String determinarTipo() {
        return TIPO_ENTRADA.equals(tipoMovimiento) ? "ENTRADA" : "SALIDA";
    }

    /** Impide que una salida supere el stock disponible. */
    public boolean validarStock() {
        if (determinarTipo().equals("ENTRADA")) {
            return true;
        }
        return cantidad <= producto.getStockActual();
    }

    /** Aplica el movimiento al stock del producto asociado. */
    public void registrar() {
        if (cantidad <= 0) {
            throw new IllegalStateException("La cantidad debe ser mayor a cero");
        }
        if (TIPO_BAJA.equals(tipoMovimiento) && (justificacion == null || justificacion.isBlank())) {
            throw new IllegalStateException("Toda baja debe registrar una justificación");
        }
        if (!validarStock()) {
            throw new IllegalStateException("Stock insuficiente para el movimiento");
        }
        int nuevoStock = determinarTipo().equals("ENTRADA")
                ? producto.getStockActual() + cantidad
                : producto.getStockActual() - cantidad;
        producto.setStockActual(nuevoStock);
    }

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
