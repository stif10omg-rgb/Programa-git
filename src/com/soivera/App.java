package com.soivera;

import com.soivera.modelo.Alerta;
import com.soivera.modelo.Inventario;
import com.soivera.modelo.MovimientosStock;
import com.soivera.modelo.Producto;

import java.time.LocalDate;

/**
 * Simulación de la operación de inventario de SOIVERA.
 */
public class App {

    public static void main(String[] args) {
        Inventario inventario = new Inventario(1);

        Producto acetaminofen = new Producto(1, "Acetaminofén 500 mg", "Medicamentos",
                1200, 2000, 40, 10, LocalDate.now().plusDays(20));
        Producto martillo = new Producto(2, "Martillo de uña", "Herramientas",
                18000, 26000, 6, 5, null);

        inventario.agregarProducto(acetaminofen);
        inventario.agregarProducto(martillo);
        System.out.println("Productos registrados: " + inventario.listarProductos().size());

        inventario.agregarMovimiento(acetaminofen, 20, MovimientosStock.TIPO_ENTRADA, null);
        inventario.agregarMovimiento(martillo, 2, MovimientosStock.TIPO_VENTA, null);
        inventario.agregarMovimiento(acetaminofen, 3, MovimientosStock.TIPO_BAJA, "Empaque dañado");

        System.out.println("Stock acetaminofén: " + acetaminofen.getStockActual());
        System.out.println("Stock martillo: " + martillo.getStockActual());
        System.out.println("Movimientos registrados: " + inventario.getMovimientos().size());

        Producto productoBuscado = inventario.buscarPorNombre("martillo de uña");
        System.out.println("Búsqueda por nombre: " + productoBuscado.getNombre());
        System.out.println("Productos en Medicamentos: " + inventario.buscarPorCategoria("Medicamentos").size());
        System.out.println("¿Acetaminofén por vencer? " + acetaminofen.verificarVencimiento());

        Alerta alertaVencimiento = new Alerta(1, Alerta.TIPO_VENCIMIENTO,
                Producto.DIAS_ALERTA_VENCIMIENTO, acetaminofen);
        Alerta alertaStock = new Alerta(2, Alerta.TIPO_STOCK_MINIMO, 0, martillo);

        alertaVencimiento.generar();
        alertaStock.generar();
        alertaVencimiento.notificar();
        alertaStock.notificar();

        try {
            inventario.agregarMovimiento(martillo, 10, MovimientosStock.TIPO_VENTA, null);
        } catch (IllegalStateException error) {
            System.out.println("Venta rechazada: " + error.getMessage());
        }
    }
}
