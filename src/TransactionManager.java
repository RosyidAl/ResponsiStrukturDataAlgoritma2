import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class TransactionManager - Mengelola proses transaksi dan menyimpan riwayat
 * 
 * Menggunakan ArrayList<Transaction> untuk riwayat transaksi.
 * Alasan: ArrayList cocok untuk data sequential/kronologis yang
 * terus bertambah (add di akhir = O(1) amortized).
 */
public class TransactionManager {

    private ArrayList<Transaction> riwayat;   // daftar semua transaksi selesai
    private int counterTransaksi;             // auto-increment untuk ID transaksi

    public TransactionManager() {
        riwayat = new ArrayList<>();
        counterTransaksi = 0;
    }

    /**
     * Memproses transaksi pembayaran untuk pelanggan.
     * Membuat objek Transaction baru dan menyimpannya ke riwayat.
     * 
     * Kompleksitas: O(n) dimana n = jumlah item di keranjang
     * - customer.getSemuaItem() -> copy n item ke ArrayList baru = O(n)
     * - customer.getTotal() -> iterasi n item = O(n)
     * - riwayat.add() -> O(1) amortized
     * - Total: O(n) + O(n) + O(1) = O(n)
     * 
     * @param customer pelanggan yang melakukan transaksi
     * @param bayar jumlah uang yang dibayarkan
     * @return objek Transaction yang sudah dibuat
     */
    public Transaction prosesTransaksi(Customer customer, double bayar) {
        double total = customer.getTotal();
        double kembalian = bayar - total;

        // generate ID transaksi otomatis (TRX0001, TRX0002, dst)
        counterTransaksi++;
        String idTrx = String.format("TRX%04d", counterTransaksi);

        // ambil snapshot item keranjang sebelum keranjang dikosongkan
        List<CartItem> items = customer.getSemuaItem();

        // buat objek transaksi
        Transaction trx = new Transaction(idTrx, customer.getNama(),
                items, total, bayar, kembalian);

        // simpan ke riwayat -> O(1) amortized
        riwayat.add(trx);

        return trx;
    }

    /**
     * Mencetak struk/receipt transaksi ke layar CLI.
     * Iterasi semua item dalam transaksi untuk ditampilkan.
     * 
     * Kompleksitas: O(n) dimana n = jumlah item dalam transaksi
     */
    public void tampilkanStruk(Transaction trx) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        System.out.println();
        System.out.println("  ================================================");
        System.out.println("              KAFE NUSANTARA POS                   ");
        System.out.println("           Jl. Merdeka No. 17, Solo               ");
        System.out.println("  ================================================");
        System.out.println("  No. Transaksi : " + trx.getIdTransaksi());
        System.out.println("  Tanggal       : " + trx.getWaktu());
        System.out.println("  Kasir         : ADMIN");
        System.out.println("  Pelanggan     : " + trx.getNamaCustomer());
        System.out.println("  ------------------------------------------------");
        System.out.println(String.format("  %-18s %5s %12s %12s",
                "Item", "Qty", "Harga", "Subtotal"));
        System.out.println("  ------------------------------------------------");

        // cetak setiap item yang dibeli
        for (CartItem item : trx.getItems()) {
            System.out.println(String.format("  %-18s %5d %12s %12s",
                    item.getProduct().getNama(),
                    item.getJumlah(),
                    nf.format(item.getProduct().getHarga()),
                    nf.format(item.getSubtotal())));
        }

        System.out.println("  ------------------------------------------------");
        System.out.println(String.format("  %-24s %24s", "TOTAL",
                nf.format(trx.getTotal())));
        System.out.println(String.format("  %-24s %24s", "BAYAR",
                nf.format(trx.getBayar())));
        System.out.println(String.format("  %-24s %24s", "KEMBALIAN",
                nf.format(trx.getKembalian())));
        System.out.println("  ================================================");
        System.out.println("        Terima kasih atas kunjungan Anda!          ");
        System.out.println("  ================================================");
        System.out.println();
    }

    /**
     * Menampilkan ringkasan seluruh riwayat transaksi dalam bentuk tabel.
     * Iterasi semua transaksi yang tersimpan.
     * 
     * Kompleksitas: O(m) dimana m = jumlah transaksi
     */
    public void tampilkanRiwayat() {
        if (riwayat.isEmpty()) {
            System.out.println("  Belum ada riwayat transaksi.");
            return;
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        System.out.println("+----------+-----------------+-----------------+-----------------------+");
        System.out.println("| ID Trx   | Pelanggan       | Total           | Waktu                 |");
        System.out.println("+----------+-----------------+-----------------+-----------------------+");

        double grandTotal = 0;
        for (Transaction trx : riwayat) {
            System.out.println(String.format("| %-8s | %-15s | %15s | %-21s |",
                    trx.getIdTransaksi(),
                    trx.getNamaCustomer(),
                    nf.format(trx.getTotal()),
                    trx.getWaktu()));
            grandTotal += trx.getTotal();
        }

        System.out.println("+----------+-----------------+-----------------+-----------------------+");
        System.out.println("  Total Transaksi  : " + riwayat.size());
        System.out.println("  Total Pendapatan : " + nf.format(grandTotal));
    }

    /**
     * Mendapatkan jumlah transaksi yang tercatat
     * Kompleksitas: O(1)
     */
    public int jumlahTransaksi() {
        return riwayat.size();
    }
}
