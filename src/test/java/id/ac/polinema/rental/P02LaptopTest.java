package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class P02LaptopTest {

    @Test
    @DisplayName("Constructor menyimpan serialNumber, name, dan year")
    void constructorAndGetters() {
        Laptop b = new Laptop("SN-0132350884", "ThinkPad X1", 2023);
        assertEquals("SN-0132350884", b.getSerialNumber());
        assertEquals("ThinkPad X1", b.getName());
        assertEquals(2023, b.getYear());
    }

    @Test
    @DisplayName("Laptop baru langsung tersedia")
    void newLaptopIsAvailable() {
        assertTrue(new Laptop("1", "ThinkPad X1", 2023).isAvailable());
    }

    @Test
    @DisplayName("rent: berhasil sekali, lalu gagal karena sudah disewa")
    void rent() {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        assertTrue(b.rent());
        assertFalse(b.isAvailable());
        assertFalse(b.rent());
    }

    @Test
    @DisplayName("giveBack membuat laptop tersedia lagi")
    void giveBack() {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        b.rent();
        b.giveBack();
        assertTrue(b.isAvailable());
        assertTrue(b.rent());
    }

    @Test
    @DisplayName("Meminjam satu laptop tidak mengubah laptop lain")
    void objectsAreIndependent() {
        Laptop a = new Laptop("1", "ThinkPad X1", 2023);
        Laptop b = new Laptop("2", "MacBook Air", 2021);
        a.rent();
        assertFalse(a.isAvailable());
        assertTrue(b.isAvailable());
        assertEquals("MacBook Air", b.getName());
    }
}
