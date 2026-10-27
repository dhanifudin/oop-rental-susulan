package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Agregasi: Rack menyimpan Laptop, tetapi Laptop ada di luar Rack juga. */
class P04RackAggregationTest {

    private static Object rack() throws Throwable {
        return R.create("Rack", "A1");
    }

    @Test
    @DisplayName("Rack ada, semua field-nya private, dan menyimpan kode rak")
    void basics() throws Throwable {
        R.assertAllFieldsPrivate(R.cls("Rack"));
        Object s = rack();
        assertEquals("A1", R.call(s, "getCode"));
        assertEquals(0, R.call(s, "getLaptopCount"));
    }

    @Test
    @DisplayName("addLaptop menambah laptop dan getLaptopCount ikut naik")
    void addLaptop() throws Throwable {
        Object s = rack();
        assertEquals(true, R.call(s, "addLaptop", new Laptop("1", "ThinkPad X1", 2023)));
        assertEquals(true, R.call(s, "addLaptop", new Laptop("2", "MacBook Air", 2021)));
        assertEquals(2, R.call(s, "getLaptopCount"));
    }

    @Test
    @DisplayName("Kapasitas rak 4 laptop; laptop ke-5 ditolak")
    void capacity() throws Throwable {
        Object s = rack();
        for (int i = 1; i <= 4; i++) {
            assertEquals(true, R.call(s, "addLaptop", new Laptop("" + i, "Laptop " + i, 2000)));
        }
        assertEquals(false, R.call(s, "addLaptop", new Laptop("5", "Laptop 5", 2000)));
        assertEquals(4, R.call(s, "getLaptopCount"));
    }

    @Test
    @DisplayName("addLaptop(null) ditolak")
    void rejectsNull() throws Throwable {
        Object s = rack();
        assertEquals(false, R.call(s, "addLaptop", (Object) null));
        assertEquals(0, R.call(s, "getLaptopCount"));
    }

    @Test
    @DisplayName("findBySerial mengembalikan objek Laptop yang sama (agregasi), atau null")
    void findBySerial() throws Throwable {
        Object s = rack();
        Laptop thinkpad = new Laptop("111", "ThinkPad X1", 2023);
        Laptop macbook = new Laptop("222", "MacBook Air", 2021);
        R.call(s, "addLaptop", thinkpad);
        R.call(s, "addLaptop", macbook);
        assertSame(macbook, R.call(s, "findBySerial", "222"));
        assertSame(thinkpad, R.call(s, "findBySerial", "111"));
        assertNull(R.call(s, "findBySerial", "999"));
    }

    @Test
    @DisplayName("countAvailable mengikuti status laptop yang disewa lewat referensi luar")
    void countAvailable() throws Throwable {
        Object s = rack();
        Laptop a = new Laptop("1", "ThinkPad X1", 2023);
        Laptop b = new Laptop("2", "MacBook Air", 2021);
        R.call(s, "addLaptop", a);
        R.call(s, "addLaptop", b);
        assertEquals(2, R.call(s, "countAvailable"));
        assertTrue(a.rent());
        assertEquals(1, R.call(s, "countAvailable"));
        a.giveBack();
        assertEquals(2, R.call(s, "countAvailable"));
        assertTrue(b.isAvailable());
    }
}
