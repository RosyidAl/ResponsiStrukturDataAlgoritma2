import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class Transaction - Menyimpan data transaksi yang sudah selesai
 * 
 * Objek Transaction bersifat immutable (tidak diubah setelah dibuat).
 * Menyimpan snapshot dari item-item yang dibeli, total, bayar, kembalian,
 * dan waktu transaksi.
 */
public class Transaction {

    private String idTransaksi;        // ID unik transaksi (TRX0001, TRX0002, dst)
    private String namaCustomer;       // nama pelanggan
    private List<CartItem> items;      // daftar item yang dibeli (snapshot)
    private double total;              // total harga
    private double bayar;              // jumlah uang yang dibayarkan
    private double kembalian;          // uang kembalian
    private String waktu;              // timestamp transaksi

    public Transaction(String idTransaksi, String namaCustomer,
                       List<CartItem> items, double total,
                       double bayar, double kembalian) {
        this.idTransaksi = idTransaksi;
        this.namaCustomer = namaCustomer;
        this.items = items;
        this.total = total;
        this.bayar = bayar;
        this.kembalian = kembalian;
        // simpan waktu saat transaksi dibuat
        this.waktu = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    // ==================== Getter ====================
    public String getIdTransaksi() { return idTransaksi; }
    public String getNamaCustomer() { return namaCustomer; }
    public List<CartItem> getItems() { return items; }
    public double getTotal() { return total; }
    public double getBayar() { return bayar; }
    public double getKembalian() { return kembalian; }
    public String getWaktu() { return waktu; }

    /**
     * Helper static method untuk format angka ke Rupiah
     * Dipakai di beberapa tempat saat menampilkan harga
     */
    public static String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return nf.format(amount);
    }
}
