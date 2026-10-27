package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Overloading: nama method atau constructor sama, parameter berbeda. */
class P07OverloadTest {

    @Test
    @DisplayName("Equipment punya dua method extendRental dengan parameter berbeda")
    void twoExtendRental() {
        int count = 0;
        for (Method m : R.cls("Equipment").getDeclaredMethods()) {
            if (m.getName().equals("extendRental")) {
                count++;
            }
        }
        assertEquals(2, count, "Harus ada extendRental() dan extendRental(int days)");
    }

    @Test
    @DisplayName("extendRental() menambah 2 hari")
    void extendDefault() throws Throwable {
        Object projector = R.create("Projector", "Epson EB-X51", 2022, 3800);
        assertEquals(1, R.call(projector, "getTotalRentalDays"));
        R.call(projector, "extendRental");
        assertEquals(3, R.call(projector, "getTotalRentalDays"));
    }

    @Test
    @DisplayName("extendRental(int) menambah sebanyak hari yang diberikan dan bisa berulang")
    void extendDays() throws Throwable {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        assertEquals(7, R.call(b, "getTotalRentalDays"));
        R.call(b, "extendRental", 3);
        R.call(b, "extendRental", 2);
        assertEquals(12, R.call(b, "getTotalRentalDays"));
    }

    @Test
    @DisplayName("extendRental(int) menolak 0 dan negatif; total tidak berubah")
    void extendRejectsNonPositive() throws Throwable {
        Object m = R.create("Camera", "Canon M50", 2020, 24);
        assertThrows(IllegalArgumentException.class, () -> R.call(m, "extendRental", 0));
        assertThrows(IllegalArgumentException.class, () -> R.call(m, "extendRental", -4));
        assertEquals(3, R.call(m, "getTotalRentalDays"));
    }

    @Test
    @DisplayName("Constructor Laptop(serialNumber, name, year, brand) langsung mengisi merek")
    void laptopConstructorWithBrand() throws Throwable {
        Brand lenovo = new Brand("Lenovo", "China");
        Object b = R.create("Laptop", "1", "ThinkPad X1", 2023, lenovo);
        assertSame(lenovo, R.call(b, "getBrand"));
        assertEquals("ThinkPad X1", R.call(b, "getName"));
        assertEquals("1", R.call(b, "getSerialNumber"));
        assertEquals(true, R.call(b, "isAvailable"));
    }

    @Test
    @DisplayName("Constructor Camera(name, year) memberi 12 megapixel; versi 3 parameter tetap jalan")
    void cameraConstructors() throws Throwable {
        Object a = R.create("Camera", "Canon M50", 2020);
        assertEquals(12, R.call(a, "getMegapixels"));
        Object b = R.create("Camera", "Canon M50", 2020, 9);
        assertEquals(9, R.call(b, "getMegapixels"));
    }
}
