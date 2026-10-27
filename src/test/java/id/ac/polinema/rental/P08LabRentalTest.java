package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** LabRental berisi logika saja: tidak boleh mencetak apa pun. */
@Timeout(10)
class P08LabRentalTest {

    private static Object labRental() throws Throwable {
        return R.create("LabRental", "Polinema Lab Rental");
    }

    private static Object projector(String name) throws Throwable {
        return R.create("Projector", name, 2022, 3000);
    }

    private static Object camera(String name) throws Throwable {
        return R.create("Camera", name, 2020, 12);
    }

    @Test
    @DisplayName("LabRental menyimpan nama dan semua field-nya private")
    void basics() throws Throwable {
        R.assertAllFieldsPrivate(R.cls("LabRental"));
        assertEquals("Polinema Lab Rental", R.call(labRental(), "getName"));
    }

    @Test
    @DisplayName("addEquipment menerima Laptop, Projector, dan Camera dalam satu array (polymorphism)")
    void mixedItems() throws Throwable {
        Object lib = labRental();
        Laptop laptop = new Laptop("1", "ThinkPad X1", 2023);
        Object projector = projector("Epson EB-X51");
        Object mag = camera("Canon M50");
        assertEquals(true, R.call(lib, "addEquipment", laptop));
        assertEquals(true, R.call(lib, "addEquipment", projector));
        assertEquals(true, R.call(lib, "addEquipment", mag));
        Object[] items = (Object[]) R.call(lib, "getEquipment");
        assertEquals(3, items.length, "getEquipment hanya mengembalikan item yang terisi");
        assertSame(laptop, items[0]);
        assertSame(projector, items[1]);
        assertSame(mag, items[2]);
    }

    @Test
    @DisplayName("addEquipment menolak null dan menolak jika sudah penuh (15 item)")
    void itemCapacity() throws Throwable {
        Object lib = labRental();
        assertEquals(false, R.call(lib, "addEquipment", (Object) null));
        for (int i = 1; i <= 15; i++) {
            assertEquals(true, R.call(lib, "addEquipment", camera("Unit " + i)));
        }
        assertEquals(false, R.call(lib, "addEquipment", camera("One too many")));
        assertEquals(15, ((Object[]) R.call(lib, "getEquipment")).length);
    }

    @Test
    @DisplayName("addBorrower menolak id kembar dan null; findBorrower mengembalikan objek yang sama")
    void borrowers() throws Throwable {
        Object lib = labRental();
        Borrower sari = new Borrower("B001", "Sari");
        assertEquals(true, R.call(lib, "addBorrower", sari));
        assertEquals(false, R.call(lib, "addBorrower", new Borrower("B001", "Orang Lain")));
        assertEquals(false, R.call(lib, "addBorrower", (Object) null));
        assertEquals(1, ((Object[]) R.call(lib, "getBorrowers")).length);
        assertSame(sari, R.call(lib, "findBorrower", "B001"));
        assertNull(R.call(lib, "findBorrower", "B999"));
    }

    @Test
    @DisplayName("addBorrower menolak jika sudah penuh (8 penyewa)")
    void borrowerCapacity() throws Throwable {
        Object lib = labRental();
        for (int i = 1; i <= 8; i++) {
            assertEquals(true, R.call(lib, "addBorrower", new Borrower("B" + i, "Borrower " + i)));
        }
        assertEquals(false, R.call(lib, "addBorrower", new Borrower("B9", "Borrower 9")));
    }

    @Test
    @DisplayName("findEquipmentByName tidak peduli huruf besar-kecil; null jika tidak ada")
    void findItem() throws Throwable {
        Object lib = labRental();
        Laptop laptop = new Laptop("1", "ThinkPad X1", 2023);
        R.call(lib, "addEquipment", laptop);
        assertSame(laptop, R.call(lib, "findEquipmentByName", "thinkpad x1"));
        assertSame(laptop, R.call(lib, "findEquipmentByName", "THINKPAD X1"));
        assertNull(R.call(lib, "findEquipmentByName", "Unknown"));
    }

    @Test
    @DisplayName("searchByKeyword mengembalikan semua item yang namanya memuat kata kunci")
    void search() throws Throwable {
        Object lib = labRental();
        Laptop thinkpad = new Laptop("1", "ThinkPad X1", 2023);
        Laptop thinkpadT14 = new Laptop("2", "ThinkPad T14", 2022);
        Object projector = projector("Epson EB-X51");
        R.call(lib, "addEquipment", thinkpad);
        R.call(lib, "addEquipment", projector);
        R.call(lib, "addEquipment", thinkpadT14);
        Object[] found = (Object[]) R.call(lib, "searchByKeyword", "thinkpad");
        assertEquals(2, found.length);
        assertSame(thinkpad, found[0]);
        assertSame(thinkpadT14, found[1]);
        Object[] none = (Object[]) R.call(lib, "searchByKeyword", "zzz");
        assertEquals(0, none.length, "Tanpa hasil, kembalikan array kosong, bukan null");
    }

    @Test
    @DisplayName("rent berhasil: item disewa dan jumlah sewaan Borrower naik")
    void rentSuccess() throws Throwable {
        Object lib = labRental();
        Borrower sari = new Borrower("B001", "Sari");
        Laptop laptop = new Laptop("1", "ThinkPad X1", 2023);
        R.call(lib, "addBorrower", sari);
        R.call(lib, "addEquipment", laptop);
        assertEquals(true, R.call(lib, "rent", "B001", "ThinkPad X1"));
        assertEquals(false, laptop.isAvailable());
        assertEquals(1, sari.getRentalCount());
    }

    @Test
    @DisplayName("rent gagal untuk Borrower atau nama yang tidak ada, dan item yang sudah disewa")
    void rentFailures() throws Throwable {
        Object lib = labRental();
        R.call(lib, "addBorrower", new Borrower("B001", "Sari"));
        R.call(lib, "addBorrower", new Borrower("B002", "Dewi"));
        R.call(lib, "addEquipment", new Laptop("1", "ThinkPad X1", 2023));
        assertEquals(false, R.call(lib, "rent", "B999", "ThinkPad X1"));
        assertEquals(false, R.call(lib, "rent", "B001", "Unknown"));
        assertEquals(true, R.call(lib, "rent", "B001", "ThinkPad X1"));
        assertEquals(false, R.call(lib, "rent", "B002", "ThinkPad X1"));
    }

    @Test
    @DisplayName("rent gagal jika Borrower sudah menyewa 2 item")
    void rentLimit() throws Throwable {
        Object lib = labRental();
        Borrower sari = new Borrower("B001", "Sari");
        R.call(lib, "addBorrower", sari);
        for (int i = 1; i <= 3; i++) {
            R.call(lib, "addEquipment", camera("Unit " + i));
        }
        assertEquals(true, R.call(lib, "rent", "B001", "Unit 1"));
        assertEquals(true, R.call(lib, "rent", "B001", "Unit 2"));
        assertEquals(false, R.call(lib, "rent", "B001", "Unit 3"));
        assertEquals(2, sari.getRentalCount());
        Object third = R.call(lib, "findEquipmentByName", "Unit 3");
        assertEquals(true, R.call(third, "isAvailable"));
    }

    @Test
    @DisplayName("giveBack berhasil lalu gagal jika item tidak sedang disewa atau data tidak ada")
    void giveBack() throws Throwable {
        Object lib = labRental();
        Borrower sari = new Borrower("B001", "Sari");
        Object projector = projector("Epson EB-X51");
        R.call(lib, "addBorrower", sari);
        R.call(lib, "addEquipment", projector);
        assertEquals(false, R.call(lib, "giveBack", "B001", "Epson EB-X51"));
        R.call(lib, "rent", "B001", "Epson EB-X51");
        assertEquals(true, R.call(lib, "giveBack", "B001", "Epson EB-X51"));
        assertEquals(true, R.call(projector, "isAvailable"));
        assertEquals(0, sari.getRentalCount());
        assertEquals(false, R.call(lib, "giveBack", "B999", "Epson EB-X51"));
        assertEquals(false, R.call(lib, "giveBack", "B001", "Unknown"));
    }

    @Test
    @DisplayName("LabRental tidak boleh mencetak ke layar (pisahkan logika dan tampilan)")
    void labRentalDoesNotPrint() throws Throwable {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            Object lib = labRental();
            R.call(lib, "addBorrower", new Borrower("B001", "Sari"));
            R.call(lib, "addEquipment", new Laptop("1", "ThinkPad X1", 2023));
            R.call(lib, "rent", "B001", "ThinkPad X1");
            R.call(lib, "giveBack", "B001", "ThinkPad X1");
            R.call(lib, "searchByKeyword", "thinkpad");
            R.call(lib, "getEquipment");
        } finally {
            System.setOut(original);
        }
        assertTrue(buffer.toString(StandardCharsets.UTF_8).isEmpty(),
                "LabRental hanya berisi logika. Semua System.out.println ada di LabRentalApp");
    }
}
