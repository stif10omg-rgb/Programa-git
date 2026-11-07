package com.soivera.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Notifica condiciones críticas de un producto: vencimiento próximo o stock mínimo (RF02).
 */
public class Alerta {

    public static final String TIPO_VENCIMIENTO = "VENCIMIENTO";
    public static final String TIPO_STOCK_MINIMO = "STOCK_MINIMO";
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_NOTIFICADA = "NOTIFICADA";

    private int idAlerta;
    private String tipo;
    private LocalDate fechaGeneracion;
    private String estado;
    private String indicadorVisual;
    private int diasAnticipacion;
    private Producto producto; // asociación con Producto

    public Alerta(int idAlerta, String tipo, int diasAnticipacion, Producto producto) {
        this.idAlerta = idAlerta;
        this.tipo = tipo;
        this.diasAnticipacion = diasAnticipacion;
        this.producto = producto;
    }

    /** Revisa si se cumple la condición crítica según el tipo de alerta. */
    public boolean verificarCondicion() {
        if (TIPO_VENCIMIENTO.equals(tipo)) {
            if (producto.getFechaVencimiento() == null) {
                return false;
            }
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), producto.getFechaVencimiento());
            return diasRestantes <= diasAnticipacion;
        }
        return producto.getStockActual() <= producto.getStockMinimo();
    }

    /** Genera la alerta solo si la condición se cumple. */
    public void generar() {
        if (verificarCondicion()) {
            fechaGeneracion = LocalDate.now();
            estado = ESTADO_PENDIENTE;
            indicadorVisual = TIPO_VENCIMIENTO.equals(tipo) ? "ROJO" : "AMARILLO";
        }
    }

    /** Muestra la alerta pendiente y la marca como notificada. */
    public void notificar() {
        if (ESTADO_PENDIENTE.equals(estado)) {
            System.out.println("[" + indicadorVisual + "] Alerta de " + tipo + ": " + producto.getNombre()
                    + " (stock: " + producto.getStockActual() + ")");
            estado = ESTADO_NOTIFICADA;
        }
    }

    public int getIdAlerta() { return idAlerta; }
    public void setIdAlerta(int idAlerta) { this.idAlerta = idAlerta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getFechaGeneracion() { return fechaGeneracion; }

    public String getEstado() { return estado; }

    public String getIndicadorVisual() { return indicadorVisual; }

    public int getDiasAnticipacion() { return diasAnticipacion; }
    public void setDiasAnticipacion(int diasAnticipacion) { this.diasAnticipacion = diasAnticipacion; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
