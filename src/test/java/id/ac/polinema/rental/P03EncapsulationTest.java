package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class P03EncapsulationTest {

    @Test
    @DisplayName("Semua field Laptop private")
    void laptopFieldsPrivate() {
        R.assertAllFieldsPrivate(Laptop.class);
    }

    @Test
    @DisplayName("Semua field Brand private")
    void brandFieldsPrivate() {
        R.assertAllFieldsPrivate(Brand.class);
    }

    @Test
    @DisplayName("serialNumber read-only: Laptop tidak punya setSerialNumber, dan status tidak bisa di-set langsung")
    void readOnlyFields() {
        assertFalse(R.hasPublicMethod(Laptop.class, "setSerialNumber"), "serialNumber tidak boleh punya setter");
        assertFalse(R.hasPublicMethod(Laptop.class, "setAvailable"),
                "status tersedia hanya boleh berubah lewat rent dan giveBack");
    }

    @Test
    @DisplayName("setName menolak null, kosong, dan hanya spasi; nama lama tetap")
    void setNameRejectsBlank() {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        assertThrows(IllegalArgumentException.class, () -> b.setName(null));
        assertThrows(IllegalArgumentException.class, () -> b.setName(""));
        assertThrows(IllegalArgumentException.class, () -> b.setName("   "));
        assertEquals("ThinkPad X1", b.getName());
    }

    @Test
    @DisplayName("setName menerima nama yang valid")
    void setNameAcceptsValid() {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        b.setName("ThinkPad T14");
        assertEquals("ThinkPad T14", b.getName());
    }

    @Test
    @DisplayName("setYear menolak 0 dan negatif; tahun lama tetap")
    void setYearRejectsNonPositive() {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        assertThrows(IllegalArgumentException.class, () -> b.setYear(0));
        assertThrows(IllegalArgumentException.class, () -> b.setYear(-5));
        assertEquals(2023, b.getYear());
        b.setYear(2022);
        assertEquals(2022, b.getYear());
    }

    @Test
    @DisplayName("Constructor Laptop memvalidasi serialNumber, name, dan year")
    void constructorValidates() {
        assertThrows(IllegalArgumentException.class, () -> new Laptop("", "ThinkPad X1", 2023));
        assertThrows(IllegalArgumentException.class, () -> new Laptop(null, "ThinkPad X1", 2023));
        assertThrows(IllegalArgumentException.class, () -> new Laptop("1", " ", 2023));
        assertThrows(IllegalArgumentException.class, () -> new Laptop("1", "ThinkPad X1", 0));
    }
}
