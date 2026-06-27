import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class CartItem - Merepresentasikan satu item dalam keranjang belanja
 * Digunakan sebagai elemen di dalam Stack keranjang Customer
 * 
 * CartItem menyimpan referensi ke Product dan jumlah yang dibeli,
 * sehingga bisa menghitung subtotal secara langsung
 */
public class CartItem {

    private Product product;  // referensi ke objek Product
    private int jumlah;       // jumlah yang dibeli

    public CartItem(Product product, int jumlah) {
        this.product = product;
        this.jumlah = jumlah;
    }

    // Getter
    public Product getProduct() { return product; }
    public int getJumlah() { return jumlah; }

    /**
     * Menghitung subtotal = harga satuan * jumlah
     * Kompleksitas: O(1) - operasi perkalian sederhana
     */
    public double getSubtotal() {
        return product.getHarga() * jumlah;
    }

    /**
     * Format tampilan item untuk tabel keranjang di CLI
     */
    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return String.format("| %-20s | %5d | %15s | %15s |",
                product.getNama(), jumlah, nf.format(product.getHarga()),
                nf.format(getSubtotal()));
    }
}
