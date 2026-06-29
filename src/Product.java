import java.text.NumberFormat;
import java.util.Locale;

/*
Class Product - Merepresentasikan data produk di toko/kafe
Implements Comparable supaya bisa di-sort berdasarkan nama,
yang diperlukan untuk Binary Search di ProductManager
*/

public class Product implements Comparable<Product> {

    private String id;       // ID unik / barcode produk
    private String nama;     // nama produk
    private double harga;    // harga satuan (Rupiah)
    private int stok;        // jumlah stok tersedia

    // Constructor
    public Product(String id, String nama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    // ==================== Getter & Setter ====================
    public String getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }

    public void setNama(String nama) { this.nama = nama; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setStok(int stok) { this.stok = stok; }

    /*
    Mengurangi stok saat barang dibeli
    Kompleksitas: O(1) - hanya operasi aritmatika sederhana
    @param jumlah jumlah yang akan dikurangi
    @return true jika stok cukup, false jika tidak
    */
    
    public boolean kurangiStok(int jumlah) {
        if (jumlah > stok) {
            return false; // stok tidak cukup
        }
        stok -= jumlah;
        return true;
    }

    /*
    Menambah stok (dipanggil saat void/undo pembelian)
    Kompleksitas: O(1) - hanya operasi penjumlahan
    */
    
    public void tambahStok(int jumlah) {
        stok += jumlah;
    }

    /*
    Format harga ke mata uang Rupiah (misal: Rp18.000,00)
    Menggunakan NumberFormat bawaan Java dengan Locale Indonesia
    */
    
    public String formatHarga() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
        return nf.format(harga);
    }

    /*
    compareTo membandingkan nama produk secara case-insensitive.
    Diperlukan supaya ArrayList<Product> bisa di-sort berdasarkan nama
    sehingga Binary Search bisa dijalankan.
     
    Kompleksitas: O(k) dimana k = panjang string nama
    (tapi biasanya dianggap O(1) karena nama produk pendek)
    */
    
    @Override
    public int compareTo(Product other) {
        return this.nama.compareToIgnoreCase(other.nama);
    }

    /*
    Format tampilan produk untuk tabel CLI
    */
    
    @Override
    public String toString() {
        return String.format("| %-8s | %-20s | %15s | %5d |",
                id, nama, formatHarga(), stok);
    }
}
