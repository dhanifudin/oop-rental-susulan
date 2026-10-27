package id.ac.polinema.rental;

/**
 * Bagian 2 dan 3: laptop laboratorium.
 *
 * Jangan ubah nama kelas, nama method, atau signature-nya. Autograder
 * memanggilnya persis seperti yang tertulis di sini.
 *
 * Field tidak dideklarasikan di sini: baca diagram kelas di README.
 * Method untuk bagian berikutnya (setBrand, getBrand, dan seterusnya) kamu
 * tambahkan sendiri sesuai diagram di bagian tersebut.
 *
 * Kerangka field (lengkapi tipe dan nama dari diagram), letakkan di atas constructor:
 *   public String serialNumber;
 *   public ____ name;
 *   public ____ year;
 *   public ____ available;
 */
public class Laptop {

    /**
     * Membuat laptop baru yang masih tersedia (belum disewa).
     * Bagian 3: serialNumber tidak boleh kosong; name dan year divalidasi seperti setter-nya.
     */
    public Laptop(String serialNumber, String name, int year) {
        // TODO Bantuan: isi semua field. Laptop baru: apakah sedang disewa atau tidak?
        // Bagian 3: aturan validasi nama dan tahun sama dengan setter. Bisakah constructor
        // memakai setter-nya?
        //
        // Kerangka (Bagian 2):
        //   this.serialNumber = serialNumber;
        //   this.name = ____;
        //   this.year = ____;
        //   this.available = ____;
        //
        // Kerangka (Bagian 3): ganti dua baris name dan year dengan pemanggilan setter.
        //   setName(____);
        //   setYear(____);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getSerialNumber() {
        // TODO Bantuan: kembalikan data yang disimpan constructor.
        //
        // Kerangka:
        //   return ____;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getName() {
        // TODO Bantuan: sama seperti getSerialNumber.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getYear() {
        // TODO Bantuan: sama seperti getSerialNumber.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** True jika laptop sedang ada di laboratorium (tidak disewa). */
    public boolean isAvailable() {
        // TODO Bantuan: field apa yang menyimpan status sewa?
        //
        // Kerangka:
        //   return ____;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Menyewa laptop. Berhasil (true) jika laptop tersedia. Jika laptop sedang
     * disewa, hasilnya false dan keadaan laptop tidak berubah.
     */
    public boolean rent() {
        // TODO Bantuan: apa yang harus kamu periksa lebih dulu sebelum mengubah status?
        // Method ini punya dua jalur return yang berbeda.
        //
        // Kerangka:
        //   if (____) {
        //       return false;
        //   }
        //   ____ = false;
        //   return true;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Mengembalikan laptop: laptop bisa disewa lagi. */
    public void giveBack() {
        // TODO Bantuan: satu baris saja. Status apa yang berubah?
        //
        // Kerangka:
        //   ____ = true;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Bagian 3. Mengubah nama. Nama null, kosong, atau hanya spasi ditolak
     * dengan IllegalArgumentException, dan nama lama tetap.
     */
    public void setName(String name) {
        // TODO Bantuan: periksa dulu, baru isi field. Class String punya method untuk
        // memeriksa teks kosong atau hanya spasi. Jangan lupa kemungkinan null.
        //
        // Kerangka:
        //   if (name == null || ____) {
        //       throw new IllegalArgumentException("____");
        //   }
        //   this.name = name;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Bagian 3. Mengubah tahun produksi. Tahun 0 atau negatif ditolak dengan
     * IllegalArgumentException, dan tahun lama tetap.
     */
    public void setYear(int year) {
        // TODO Bantuan: pola yang sama dengan setName. Apa syarat tahun yang valid?
        //
        // Kerangka:
        //   if (year ____ 0) {
        //       throw new IllegalArgumentException("____");
        //   }
        //   this.year = year;
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
