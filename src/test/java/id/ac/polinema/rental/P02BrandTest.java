package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class P02BrandTest {

    @Test
    @DisplayName("Constructor menyimpan nama dan negara, getter mengembalikannya")
    void constructorAndGetters() {
        Brand a = new Brand("Lenovo", "China");
        assertEquals("Lenovo", a.getName());
        assertEquals("China", a.getCountry());
    }

    @Test
    @DisplayName("getInfo mengembalikan 'nama (negara)'")
    void getInfo() {
        Brand a = new Brand("Asus", "Taiwan");
        assertEquals("Asus (Taiwan)", a.getInfo());
    }

    @Test
    @DisplayName("Dua objek Brand punya data sendiri-sendiri")
    void objectsAreIndependent() {
        Brand a = new Brand("Lenovo", "China");
        Brand b = new Brand("Asus", "Taiwan");
        assertNotEquals(a.getName(), b.getName());
        assertEquals("Lenovo", a.getName());
        assertEquals("Taiwan", b.getCountry());
    }
}
