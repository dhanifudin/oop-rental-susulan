package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Dependensi: Technician hanya memakai Borrower dan Laptop lewat parameter. */
class P04TechnicianDependencyTest {

    private static Object technician() throws Throwable {
        return R.create("Technician");
    }

    @Test
    @DisplayName("Technician tidak menyimpan apa pun sebagai field (dependensi, bukan asosiasi)")
    void noFields() {
        Class<?> c = R.cls("Technician");
        assertEquals(0, c.getDeclaredFields().length,
                "Technician hanya BERGANTUNG pada Borrower dan Laptop lewat parameter, jangan simpan sebagai field");
    }

    @Test
    @DisplayName("handOut berhasil: laptop disewa dan jumlah sewaan Borrower naik")
    void handOutSuccess() throws Throwable {
        Object l = technician();
        Borrower m = new Borrower("B001", "Sari");
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        assertEquals(true, R.call(l, "handOut", m, b));
        assertFalse(b.isAvailable());
        assertEquals(1, m.getRentalCount());
    }

    @Test
    @DisplayName("handOut gagal jika laptop sudah disewa; jumlah sewaan tidak berubah")
    void handOutLaptopTaken() throws Throwable {
        Object l = technician();
        Borrower a = new Borrower("B001", "Sari");
        Borrower b = new Borrower("B002", "Dewi");
        Laptop laptop = new Laptop("1", "ThinkPad X1", 2023);
        R.call(l, "handOut", a, laptop);
        assertEquals(false, R.call(l, "handOut", b, laptop));
        assertEquals(0, b.getRentalCount());
    }

    @Test
    @DisplayName("handOut gagal jika Borrower sudah menyewa 2 laptop; laptop tetap tersedia")
    void handOutBorrowerAtLimit() throws Throwable {
        Object l = technician();
        Borrower m = new Borrower("B001", "Sari");
        for (int i = 1; i <= 2; i++) {
            R.call(l, "handOut", m, new Laptop("" + i, "Laptop " + i, 2000));
        }
        Laptop third = new Laptop("3", "Laptop 3", 2000);
        assertEquals(false, R.call(l, "handOut", m, third));
        assertTrue(third.isAvailable());
        assertEquals(2, m.getRentalCount());
    }

    @Test
    @DisplayName("takeBack berhasil: laptop tersedia lagi dan jumlah sewaan turun")
    void takeBackSuccess() throws Throwable {
        Object l = technician();
        Borrower m = new Borrower("B001", "Sari");
        Laptop b = new Laptop("1", "ThinkPad X1", 2023);
        R.call(l, "handOut", m, b);
        assertEquals(true, R.call(l, "takeBack", m, b));
        assertTrue(b.isAvailable());
        assertEquals(0, m.getRentalCount());
    }

    @Test
    @DisplayName("takeBack gagal untuk laptop yang tidak sedang disewa")
    void takeBackNotRented() throws Throwable {
        Object l = technician();
        Borrower m = new Borrower("B001", "Sari");
        assertEquals(false, R.call(l, "takeBack", m, new Laptop("1", "ThinkPad X1", 2023)));
        assertEquals(0, m.getRentalCount());
    }
}
