package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/** Asosiasi: Laptop mengenal Brand, tetapi Brand hidup sendiri. */
class P04AssociationTest {

    @Test
    @DisplayName("Laptop baru belum punya merek (null)")
    void newLaptopHasNoBrand() throws Throwable {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        assertNull(R.call(b, "getBrand"));
    }

    @Test
    @DisplayName("getBrand mengembalikan objek Brand yang sama (asosiasi)")
    void sameBrandObject() throws Throwable {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        Brand martin = new Brand("Lenovo", "China");
        R.call(b, "setBrand", martin);
        assertSame(martin, R.call(b, "getBrand"));
    }

    @Test
    @DisplayName("Dua laptop bisa berbagi satu Brand")
    void laptopsShareBrand() throws Throwable {
        Laptop a = new Laptop("1", "ThinkPad X1", 2023);
        Laptop b = new Laptop("2", "ThinkPad T14", 2022);
        Brand martin = new Brand("Lenovo", "China");
        R.call(a, "setBrand", martin);
        R.call(b, "setBrand", martin);
        assertSame(R.call(a, "getBrand"), R.call(b, "getBrand"));
        assertEquals("Lenovo", martin.getName());
    }

    @Test
    @DisplayName("setBrand(null) melepas merek")
    void clearBrand() throws Throwable {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        R.call(b, "setBrand", new Brand("Lenovo", "China"));
        R.call(b, "setBrand", (Object) null);
        assertNull(R.call(b, "getBrand"));
    }
}
