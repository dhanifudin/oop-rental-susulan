package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Menjalankan LabRentalApp dengan input skrip (bukan keyboard sungguhan),
 * lalu memeriksa teks yang dicetak.
 */
@Timeout(10)
class P08LabRentalAppTest {

    private static Object labRental() throws Throwable {
        return R.create("LabRental", "Polinema Lab Rental");
    }

    private static String run(Object labRental, String... lines) throws Throwable {
        String input = String.join("\n", lines) + "\n";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        Object app = R.create("LabRentalApp", labRental, new Scanner(input), out);
        R.call(app, "run");
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static void assertHas(String output, String expected) {
        assertTrue(output.contains(expected),
                "Output harus memuat \"" + expected + "\". Output sebenarnya:\n" + output);
    }

    @Test
    @DisplayName("Menu tampil lengkap dan pilihan 0 mencetak 'Goodbye'")
    void menuAndExit() throws Throwable {
        String out = run(labRental(), "0");
        assertHas(out, "=== Polinema Lab Rental ===");
        for (String line : new String[] {"1. List equipment", "2. Add laptop", "3. Add projector", "4. Add camera",
                "5. Register borrower", "6. Rent equipment", "7. Return equipment", "8. Search equipment",
                "9. List borrowers", "0. Exit"}) {
            assertHas(out, line);
        }
        assertHas(out, "Goodbye");
    }

    @Test
    @DisplayName("Pilihan yang tidak dikenal mencetak 'Invalid choice' lalu menu muncul lagi")
    void invalidChoice() throws Throwable {
        String out = run(labRental(), "x", "42", "0");
        assertHas(out, "Invalid choice");
        assertEquals(3, out.split("0\\. Exit", -1).length - 1, "Menu harus dicetak ulang di setiap putaran");
    }

    @Test
    @DisplayName("Daftar kosong mencetak 'No equipment' dan 'No borrowers'")
    void emptyLists() throws Throwable {
        String out = run(labRental(), "1", "9", "0");
        assertHas(out, "No equipment");
        assertHas(out, "No borrowers");
    }

    @Test
    @DisplayName("Tambah laptop lalu tampilkan: describe, status, dan lama sewa")
    void addLaptopAndList() throws Throwable {
        Object lib = labRental();
        String out = run(lib, "2", "SN-1", "ThinkPad X1", "2023", "Lenovo", "China", "1", "0");
        assertHas(out, "Equipment added");
        assertHas(out, "ThinkPad X1 (2023) by Lenovo | Available | 7 days");
        assertEquals(1, ((Object[]) R.call(lib, "getEquipment")).length);
    }

    @Test
    @DisplayName("Tambah laptop tanpa merek: nama merek dikosongkan")
    void addLaptopWithoutBrand() throws Throwable {
        String out = run(labRental(), "2", "SN-2", "MacBook Air", "2021", "", "1", "0");
        assertHas(out, "Equipment added");
        assertHas(out, "MacBook Air (2021) by Unknown | Available | 7 days");
    }

    @Test
    @DisplayName("Tambah proyektor dan kamera")
    void addProjectorAndCamera() throws Throwable {
        String out = run(labRental(), "3", "Epson EB-X51", "2022", "3800", "4", "Canon M50", "2020", "24", "1", "0");
        assertHas(out, "Epson EB-X51 (2022) [3800 lumens] | Available | 1 days");
        assertHas(out, "Canon M50 (2020) | Available | 3 days");
    }

    @Test
    @DisplayName("Tahun yang bukan angka positif mencetak 'Invalid number' dan item tidak ditambah")
    void invalidYear() throws Throwable {
        Object lib = labRental();
        String out = run(lib, "2", "SN-1", "ThinkPad X1", "abc", "3", "Epson EB-X51", "-5", "0");
        assertHas(out, "Invalid number");
        assertEquals(0, ((Object[]) R.call(lib, "getEquipment")).length);
    }

    @Test
    @DisplayName("Nomor seri atau nama kosong mencetak 'Invalid input'")
    void blankInput() throws Throwable {
        Object lib = labRental();
        String out = run(lib, "2", "", "ThinkPad X1", "2023", "0");
        assertHas(out, "Invalid input");
        assertEquals(0, ((Object[]) R.call(lib, "getEquipment")).length);
    }

    @Test
    @DisplayName("Daftar penyewa baru, ID kembar ditolak")
    void registerBorrower() throws Throwable {
        String out = run(labRental(), "5", "B001", "Sari", "5", "B001", "Dewi", "9", "0");
        assertHas(out, "Borrower registered");
        assertHas(out, "Borrower ID already exists");
        assertHas(out, "B001 - Sari (0 rentals)");
    }

    @Test
    @DisplayName("Alur lengkap: tambah penyewa dan item, sewa, lihat status, kembalikan")
    void rentAndReturn() throws Throwable {
        Object lib = labRental();
        String out = run(lib,
                "5", "B001", "Sari",
                "3", "Epson EB-X51", "2022", "3800",
                "6", "B001", "Epson EB-X51",
                "1", "9",
                "7", "B001", "Epson EB-X51",
                "1", "9",
                "0");
        assertHas(out, "Rental successful");
        assertHas(out, "Epson EB-X51 (2022) [3800 lumens] | Rented | 1 days");
        assertHas(out, "B001 - Sari (1 rentals)");
        assertHas(out, "Return successful");
        assertHas(out, "Epson EB-X51 (2022) [3800 lumens] | Available | 1 days");
        assertHas(out, "B001 - Sari (0 rentals)");
    }

    @Test
    @DisplayName("Sewa dan kembalikan yang tidak valid mencetak 'Rental failed' dan 'Return failed'")
    void failures() throws Throwable {
        String out = run(labRental(), "6", "X", "Y", "7", "X", "Y", "0");
        assertHas(out, "Rental failed");
        assertHas(out, "Return failed");
    }

    @Test
    @DisplayName("Cari item: tampilkan yang cocok, atau 'No equipment found'")
    void search() throws Throwable {
        Object lib = labRental();
        R.call(lib, "addEquipment", new Laptop("1", "ThinkPad X1", 2023));
        R.call(lib, "addEquipment", R.create("Projector", "Epson EB-X51", 2022, 3800));
        String out = run(lib, "8", "thinkpad", "8", "zzz", "0");
        assertHas(out, "ThinkPad X1 (2023) by Unknown");
        assertHas(out, "No equipment found");
        assertTrue(!out.contains("Epson EB-X51 (2022)"), "Epson EB-X51 tidak cocok dengan kata kunci 'code'");
    }

    @Test
    @DisplayName("Main mencetak sapaan, lalu menjalankan LabRentalApp dengan data contoh")
    void mainStartsTheApp() {
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream("1\n0\n".getBytes(StandardCharsets.UTF_8)));
        try {
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        String out = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(out.startsWith("Welcome to Polinema Lab Rental"),
                "Baris pertama output Main harus sapaan. Output sebenarnya:\n" + out);
        assertHas(out, "=== Polinema Lab Rental ===");
        assertHas(out, "Goodbye");
        assertTrue(!out.contains("No equipment"),
                "Main harus mengisi data contoh sebelum menjalankan LabRentalApp");
    }
}
