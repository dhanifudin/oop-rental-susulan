package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Inheritance: Laptop, Projector, dan Camera adalah Equipment (IS-A). */
class P06InheritanceTest {

    private static Object projector() throws Throwable {
        return R.create("Projector", "Epson EB-X51", 2022, 148);
    }

    private static Object camera() throws Throwable {
        return R.create("Camera", "Canon M50", 2020, 7);
    }

    @Test
    @DisplayName("Laptop, Projector, dan Camera extends Equipment")
    void superclass() {
        Class<?> item = R.cls("Equipment");
        assertEquals(item, Laptop.class.getSuperclass(), "Laptop harus extends Equipment");
        assertEquals(item, R.cls("Projector").getSuperclass(), "Projector harus extends Equipment");
        assertEquals(item, R.cls("Camera").getSuperclass(), "Camera harus extends Equipment");
    }

    @Test
    @DisplayName("Equipment menyimpan name dan year sebagai protected, dan tidak ada field public")
    void itemFields() {
        Class<?> item = R.cls("Equipment");
        for (Field f : item.getDeclaredFields()) {
            if (f.isSynthetic()) {
                continue;
            }
            assertFalse(Modifier.isPublic(f.getModifiers()), "Field " + f.getName() + " tidak boleh public");
        }
        assertTrue(Modifier.isProtected(field(item, "name").getModifiers()), "name harus protected");
        assertTrue(Modifier.isProtected(field(item, "year").getModifiers()), "year harus protected");
    }

    private static Field field(Class<?> c, String name) {
        try {
            return c.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return org.junit.jupiter.api.Assertions.fail("Field " + name + " tidak ada di " + c.getSimpleName());
        }
    }

    @Test
    @DisplayName("Laptop tidak menyimpan ulang name, year, dan available (sudah ada di Equipment)")
    void laptopDoesNotRepeatFields() {
        for (Field f : Laptop.class.getDeclaredFields()) {
            String n = f.getName();
            assertFalse(n.equals("name") || n.equals("year") || n.equals("available"),
                    "Field " + n + " sudah ada di Equipment. Hapus dari Laptop.");
        }
        R.assertAllFieldsPrivate(Laptop.class);
    }

    @Test
    @DisplayName("Method umum dipindah ke Equipment, bukan ditulis ulang di subclass")
    void methodsMovedUp() {
        for (String name : new String[] {"getName", "getYear", "isAvailable", "rent", "giveBack"}) {
            assertFalse(R.declaresMethod(Laptop.class, name), "Laptop tidak perlu mendeklarasikan " + name);
            assertFalse(R.declaresMethod(R.cls("Projector"), name), "Projector tidak perlu mendeklarasikan " + name);
            assertTrue(R.declaresMethod(R.cls("Equipment"), name), "Equipment harus punya " + name);
        }
    }

    @Test
    @DisplayName("Equipment(name, year) menyimpan data dan memvalidasinya")
    void itemConstructor() throws Throwable {
        Object item = R.create("Equipment", "Generic", 2000);
        assertEquals("Generic", R.call(item, "getName"));
        assertEquals(2000, R.call(item, "getYear"));
        assertEquals(true, R.call(item, "isAvailable"));
        assertThrows(IllegalArgumentException.class, () -> R.create("Equipment", " ", 2000));
        assertThrows(IllegalArgumentException.class, () -> R.create("Equipment", "Generic", 0));
    }

    @Test
    @DisplayName("Projector menyimpan kecerahan dan memakai getter turunan dari Equipment")
    void projectorKeepsDuration() throws Throwable {
        Object d = projector();
        assertEquals("Epson EB-X51", R.call(d, "getName"));
        assertEquals(2022, R.call(d, "getYear"));
        assertEquals(148, R.call(d, "getLumens"));
        R.assertAllFieldsPrivate(R.cls("Projector"));
    }

    @Test
    @DisplayName("Camera menyimpan resolusi dan memakai getter turunan")
    void cameraKeepsMegapixels() throws Throwable {
        Object m = camera();
        assertEquals("Canon M50", R.call(m, "getName"));
        assertEquals(7, R.call(m, "getMegapixels"));
        R.assertAllFieldsPrivate(R.cls("Camera"));
    }

    @Test
    @DisplayName("rent dan giveBack diwarisi oleh semua subclass")
    void inheritedBehaviour() throws Throwable {
        Object[] items = {new Laptop("1", "ThinkPad X1", 2023), projector(), camera()};
        for (Object item : items) {
            assertEquals(true, R.call(item, "isAvailable"));
            assertEquals(true, R.call(item, "rent"));
            assertEquals(false, R.call(item, "rent"));
            R.call(item, "giveBack");
            assertEquals(true, R.call(item, "isAvailable"));
        }
    }

    @Test
    @DisplayName("Validasi setter ikut diwarisi: setName kosong ditolak di Projector")
    void validationInherited() throws Throwable {
        Object d = projector();
        assertThrows(IllegalArgumentException.class, () -> R.call(d, "setName", ""));
        assertThrows(IllegalArgumentException.class, () -> R.call(d, "setYear", -1));
        assertEquals("Epson EB-X51", R.call(d, "getName"));
    }

    @Test
    @DisplayName("Objek subclass bisa disimpan sebagai Equipment (polymorphism)")
    void isInstance() throws Throwable {
        Class<?> item = R.cls("Equipment");
        assertTrue(item.isInstance(new Laptop("1", "ThinkPad X1", 2023)));
        assertTrue(item.isInstance(projector()));
        assertTrue(item.isInstance(camera()));
    }
}
