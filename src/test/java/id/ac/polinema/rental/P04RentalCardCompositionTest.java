package id.ac.polinema.rental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/** Komposisi: RentalCard dibuat di dalam Borrower dan tidak datang dari luar. */
class P04RentalCardCompositionTest {

    @Test
    @DisplayName("RentalCard ada dan field-nya private")
    void cardClass() {
        R.assertAllFieldsPrivate(R.cls("RentalCard"));
    }

    @Test
    @DisplayName("Borrower punya kartu bernomor 'RC-' + borrowerId")
    void borrowerHasCard() throws Throwable {
        Borrower m = new Borrower("B001", "Sari");
        Object card = R.call(m, "getCard");
        assertNotNull(card, "Borrower harus membuat RentalCard sendiri di constructor");
        assertEquals("RC-B001", R.call(card, "getCardNumber"));
    }

    @Test
    @DisplayName("getCard selalu mengembalikan kartu yang sama untuk satu Borrower")
    void sameCardEveryTime() throws Throwable {
        Borrower m = new Borrower("B001", "Sari");
        assertSame(R.call(m, "getCard"), R.call(m, "getCard"));
    }

    @Test
    @DisplayName("Dua Borrower punya dua kartu berbeda")
    void differentBorrowersDifferentCards() throws Throwable {
        Borrower a = new Borrower("B001", "Sari");
        Borrower b = new Borrower("B002", "Dewi");
        assertNotSame(R.call(a, "getCard"), R.call(b, "getCard"));
        assertEquals("RC-B002", R.call(R.call(b, "getCard"), "getCardNumber"));
    }

    @Test
    @DisplayName("Constructor Borrower tidak menerima RentalCard dari luar (komposisi)")
    void cardIsNotPassedIn() {
        Class<?> card = R.cls("RentalCard");
        for (Constructor<?> k : Borrower.class.getConstructors()) {
            for (Class<?> p : k.getParameterTypes()) {
                org.junit.jupiter.api.Assertions.assertNotEquals(card, p,
                        "Borrower harus MEMBUAT RentalCard sendiri, bukan menerimanya dari parameter");
            }
        }
    }
}
