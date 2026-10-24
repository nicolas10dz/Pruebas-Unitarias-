package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


/*
* ENTREGA DEL TALLER
* Nombre completo:
* 1. Bug encontrado (método y línea de CarritoCompras.java):
* 2. Prueba que lo detectó (nombre del método):
* 3. Sugerencia de la IA que rechacé o corregí, y por qué
 */


class CarritoComprasTest {
    private CarritoCompras carrito;
    @BeforeEach
    void setUp() { carrito = new CarritoCompras(); }
    @Test
    @DisplayName("Subtotal = precio × cantidad de cada producto")
    void calcularSubtotal_variosProductos_sumaCorrecta() {
// Arrange
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);
// Act
        double subtotal = carrito.calcularSubtotal();
// Assert
        assertEquals(34_000, subtotal, 0.001);
    }

    @Test
    @DisplayName("R2: Carrito vacío retorna subtotal 0 y total 0")
    void calcularSubtotalYTotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularSubtotal());
        assertEquals(0, carrito.calcularTotal());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("R1: Rechaza nombre vacío o en blanco")
    void agregarProducto_nombreVacio_lanzaExcepcion(String nombreInvalido) {
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(nombreInvalido, 1000, 1));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -100})
    @DisplayName("R1: Rechaza precio 0 o negativo")
    void agregarProducto_precioInvalido_lanzaExcepcion(double precio) {
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto("Lapiz", precio, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("R1: Rechaza cantidad 0 o negativa (AQUÍ SALTA EL BUG)")
    void agregarProducto_cantidadInvalida_lanzaExcepcion(int cantidad) {
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto("Lapiz", 1000, cantidad));
    }

    @Test
    @DisplayName("R3: Cupón SENA10 aplica 10% de descuento")
    void aplicarCupon_sena10_aplicaDescuento() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(50_000, carrito.calcularDescuento(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({"50000, 5000", "49999, 0"})
    @DisplayName("R4: Cupón BIENVENIDO en fronteras 50.000 y 49.999")
    void aplicarCupon_bienvenido_fronteras(double precio, double descuentoEsperado) {
        carrito.agregarProducto("Articulo", precio, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(descuentoEsperado, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("R5: Cupón inválido lanza excepción")
    void aplicarCupon_invalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> carrito.aplicarCupon("FALSO"));
    }

    @Test
    @DisplayName("R6: Total con envío pagado")
    void calcularTotal_envioPagado() {
        carrito.agregarProducto("Libro", 100_000, 1);
        assertEquals(131_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("R6: Total con envío gratis y cupón SENA10")
    void calcularTotal_envioGratisConCupon() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(535_500, carrito.calcularTotal(), 0.001);
    }

}