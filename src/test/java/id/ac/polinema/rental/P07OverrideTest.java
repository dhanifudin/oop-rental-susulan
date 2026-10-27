package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Overriding: subclass mengganti perilaku method yang diwarisi. */
class P07OverrideTest {

    private static Object item() throws Throwable {
        return R.create("Equipment", "Generic", 2000);
    }

    private static Object projector() throws Throwable {
        return R.create("Projector", "Epson EB-X51", 2022, 3800);
    }

    private static Object camera() throws Throwable {
        return R.create("Camera", "Canon M50", 2020, 24);
    }

    @Test
    @DisplayName("rentalDays: Equipment 3, Laptop 7, Projector 1, Camera mewarisi 3")
    void rentalDays() throws Throwable {
        assertEquals(3, R.call(item(), "rentalDays"));
        assertEquals(7, R.call(new Laptop("1", "ThinkPad X1", 2023), "rentalDays"));
        assertEquals(1, R.call(projector(), "rentalDays"));
        assertEquals(3, R.call(camera(), "rentalDays"));
    }

    @Test
    @DisplayName("Laptop dan Projector meng-override rentalDays; Camera tidak")
    void whoOverrides() {
        assertTrue(R.declaresMethod(Laptop.class, "rentalDays"), "Laptop harus override rentalDays");
        assertTrue(R.declaresMethod(R.cls("Projector"), "rentalDays"), "Projector harus override rentalDays");
        assertFalse(R.declaresMethod(R.cls("Camera"), "rentalDays"),
                "Camera tidak perlu override rentalDays, ia memakai versi Equipment");
    }

    @Test
    @DisplayName("describe di Equipment: 'name (year)'")
    void itemDescribe() throws Throwable {
        assertEquals("Generic (2000)", R.call(item(), "describe"));
    }

    @Test
    @DisplayName("describe di Laptop memakai super.describe() lalu menambah ' by merek'")
    void laptopDescribe() throws Throwable {
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        assertEquals("ThinkPad X1 (2023) by Unknown", R.call(b, "describe"));
        R.call(b, "setBrand", new Brand("Lenovo", "China"));
        assertEquals("ThinkPad X1 (2023) by Lenovo", R.call(b, "describe"));
    }

    @Test
    @DisplayName("describe di Projector menambah kecerahan")
    void projectorDescribe() throws Throwable {
        assertEquals("Epson EB-X51 (2022) [3800 lumens]", R.call(projector(), "describe"));
    }

    @Test
    @DisplayName("describe di Camera memakai versi Equipment")
    void cameraDescribe() throws Throwable {
        assertEquals("Canon M50 (2020)", R.call(camera(), "describe"));
    }

    @Test
    @DisplayName("toString mengembalikan describe() untuk semua jenis item")
    void toStringUsesDescribe() throws Throwable {
        Object[] items = {item(), new Laptop("1", "ThinkPad X1", 2023), projector(), camera()};
        for (Object it : items) {
            assertEquals(R.call(it, "describe"), it.toString(),
                    "toString harus mengembalikan describe() pada " + it.getClass().getSimpleName());
        }
    }

    @Test
    @DisplayName("Polymorphism: objek yang menentukan versi method, bukan tipe variabel")
    void polymorphism() throws Throwable {
        Object[] items = {item(), new Laptop("1", "ThinkPad X1", 2023), projector(), camera()};
        int[] expected = {3, 7, 1, 3};
        for (int i = 0; i < items.length; i++) {
            assertEquals(expected[i], R.call(items[i], "rentalDays"));
        }
    }
}
