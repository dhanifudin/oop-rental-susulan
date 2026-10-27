package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class P03BorrowerTest {

    @Test
    @DisplayName("Semua field Borrower private")
    void fieldsPrivate() {
        R.assertAllFieldsPrivate(Borrower.class);
    }

    @Test
    @DisplayName("Constructor menyimpan id dan nama; jumlah sewaan awal 0")
    void constructor() {
        Borrower m = new Borrower("B001", "Sari");
        assertEquals("B001", m.getBorrowerId());
        assertEquals("Sari", m.getName());
        assertEquals(0, m.getRentalCount());
    }

    @Test
    @DisplayName("Constructor menolak id atau nama null/kosong")
    void constructorValidates() {
        assertThrows(IllegalArgumentException.class, () -> new Borrower("", "Sari"));
        assertThrows(IllegalArgumentException.class, () -> new Borrower(null, "Sari"));
        assertThrows(IllegalArgumentException.class, () -> new Borrower("B001", "  "));
        assertThrows(IllegalArgumentException.class, () -> new Borrower("B001", null));
    }

    @Test
    @DisplayName("borrowerId dan rentalCount read-only: tidak ada setter")
    void readOnlyFields() {
        assertFalse(R.hasPublicMethod(Borrower.class, "setBorrowerId"));
        assertFalse(R.hasPublicMethod(Borrower.class, "setRentalCount"));
    }

    @Test
    @DisplayName("setName menolak nama kosong dan menjaga nama lama")
    void setName() {
        Borrower m = new Borrower("B001", "Sari");
        assertThrows(IllegalArgumentException.class, () -> m.setName(""));
        assertThrows(IllegalArgumentException.class, () -> m.setName(null));
        assertEquals("Sari", m.getName());
        m.setName("Dewi Wulandari");
        assertEquals("Dewi Wulandari", m.getName());
    }

    @Test
    @DisplayName("addRental sampai batas 2; sewaan ke-3 ditolak")
    void addRentalLimit() {
        Borrower m = new Borrower("B001", "Sari");
        assertTrue(m.addRental());
        assertTrue(m.addRental());
        assertFalse(m.addRental());
        assertEquals(2, m.getRentalCount());
    }

    @Test
    @DisplayName("canRent dihitung dari jumlah sewaan (derived getter)")
    void canRentIsDerived() {
        Borrower m = new Borrower("B001", "Sari");
        assertTrue(m.canRent());
        m.addRental();
        assertTrue(m.canRent());
        m.addRental();
        assertFalse(m.canRent());
        m.endRental();
        assertTrue(m.canRent());
    }

    @Test
    @DisplayName("endRental tidak boleh membuat jumlah sewaan negatif")
    void endRentalFloor() {
        Borrower m = new Borrower("B001", "Sari");
        assertFalse(m.endRental());
        assertEquals(0, m.getRentalCount());
        m.addRental();
        assertTrue(m.endRental());
        assertEquals(0, m.getRentalCount());
    }
}
