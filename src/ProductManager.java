import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class ProductManager - Mengelola seluruh data produk dalam sistem POS
 * 
 * Menggunakan 2 struktur data utama:
 * 
 * 1. HashMap<String, Product> (produkById)
 *    - Untuk pencarian produk berdasarkan ID / barcode
 *    - Key = ID produk, Value = objek Product
 *    - Kompleksitas lookup: O(1) average case
 *    - Alasan: di kasir, pencarian by ID/barcode harus instan
 * 
 * 2. ArrayList<Product> (daftarProduk) - selalu dalam keadaan sorted by nama
 *    - Untuk pencarian produk berdasarkan nama menggunakan Binary Search
 *    - Kompleksitas pencarian: O(log n)
 *    - Alasan: lebih efisien daripada linear search O(n), dan menunjukkan
 *      pemahaman konsep Binary Search
 */
public class ProductManager {

    private HashMap<String, Product> produkById;     // lookup by ID -> O(1)
    private ArrayList<Product> daftarProduk;          // sorted by nama -> untuk Binary Search

    public ProductManager() {
        produkById = new HashMap<>();
        daftarProduk = new ArrayList<>();
        initDataAwal();  // load 15 produk sample
    }

    // ==================== PENCARIAN PRODUK ====================

    /**
     * Mencari produk berdasarkan ID menggunakan HashMap.get()
     * 
     * Kompleksitas Waktu: O(1) average case
     * Penjelasan:
     * - HashMap menyimpan data dalam bentuk array of buckets
     * - Saat get(key), Java menghitung hashCode dari key
     * - hashCode dikonversi menjadi index bucket -> langsung akses
     * - Tidak perlu iterasi seluruh data seperti linear search
     * - Worst case O(n) jika terjadi banyak collision, tapi sangat jarang
     * 
     * @param id ID produk yang dicari
     * @return Product jika ditemukan, null jika tidak ada
     */
    public Product cariById(String id) {
        return produkById.get(id.toUpperCase());
    }

    /**
     * Mencari produk berdasarkan nama menggunakan Binary Search (manual).
     * 
     * Kompleksitas Waktu: O(log n)
     * Penjelasan:
     * - Prasyarat: ArrayList harus sudah sorted berdasarkan nama
     * - Setiap iterasi, ruang pencarian diperkecil separuhnya
     * - Contoh: 1000 produk -> max 10 perbandingan (log2(1000) ≈ 10)
     * 
     * Cara kerja:
     * 1. Tentukan batas low dan high
     * 2. Hitung mid = (low + high) / 2
     * 3. Bandingkan nama target dengan nama produk di posisi mid
     *    - Jika sama -> return produk (ketemu)
     *    - Jika target < mid -> geser high = mid - 1 (cari di kiri)
     *    - Jika target > mid -> geser low = mid + 1 (cari di kanan)
     * 4. Ulangi sampai low > high (berarti tidak ditemukan)
     * 
     * @param nama nama produk yang dicari (case-insensitive)
     * @return Product jika ditemukan, null jika tidak ada
     */
    public Product cariByNama(String nama) {
        int low = 0;
        int high = daftarProduk.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;  // rumus ini menghindari integer overflow
            Product midProduct = daftarProduk.get(mid);
            int hasil = midProduct.getNama().compareToIgnoreCase(nama);

            if (hasil == 0) {
                return midProduct;      // ketemu! nama sama
            } else if (hasil < 0) {
                low = mid + 1;          // nama target ada di sebelah kanan
            } else {
                high = mid - 1;         // nama target ada di sebelah kiri
            }
        }

        return null; // tidak ditemukan
    }

    // ==================== KELOLA PRODUK ====================

    /**
     * Menambah produk baru ke dalam sistem.
     * 
     * Kompleksitas Waktu: O(n) 
     * Penjelasan:
     * - Insert ke HashMap: O(1) 
     * - Cari posisi insert di ArrayList pakai binarySearch: O(log n)
     * - Insert ke ArrayList di posisi tertentu: O(n) karena elemen 
     *   setelah posisi insert harus digeser ke kanan
     * - Total: O(1) + O(log n) + O(n) = O(n)
     * 
     * @return true jika berhasil, false jika ID sudah ada
     */
    public boolean tambahProduk(Product produk) {
        // cek duplikat ID
        if (produkById.containsKey(produk.getId().toUpperCase())) {
            return false;
        }

        // masukkan ke HashMap -> O(1)
        produkById.put(produk.getId().toUpperCase(), produk);

        // cari posisi insert yang tepat supaya ArrayList tetap sorted
        // Collections.binarySearch return index jika ada, atau (-(insertion point) - 1) jika tidak
        int pos = Collections.binarySearch(daftarProduk, produk);
        if (pos < 0) {
            pos = -(pos + 1);  // konversi ke insertion point
        }
        // insert di posisi yang tepat -> O(n) karena geser elemen
        daftarProduk.add(pos, produk);

        return true;
    }

    /**
     * Update stok produk berdasarkan ID.
     * 
     * Kompleksitas: O(1)
     * - Lookup produk di HashMap: O(1)
     * - Update field stok: O(1)
     * 
     * @param id ID produk
     * @param delta perubahan stok (positif = tambah, negatif = kurangi)
     * @return true jika berhasil
     */
    public boolean updateStok(String id, int delta) {
        Product p = cariById(id);
        if (p == null) return false;

        int stokBaru = p.getStok() + delta;
        if (stokBaru < 0) {
            return false; // stok tidak boleh minus
        }
        p.setStok(stokBaru);
        return true;
    }

    // ==================== TAMPILAN ====================

    /**
     * Menampilkan seluruh produk dalam bentuk tabel di CLI.
     * Iterasi seluruh ArrayList untuk menampilkan data.
     * 
     * Kompleksitas: O(n) dimana n = jumlah produk
     */
    public void tampilkanSemuaProduk() {
        System.out.println("+----------+----------------------+-----------------+-------+");
        System.out.println("| ID       | Nama Produk          | Harga           | Stok  |");
        System.out.println("+----------+----------------------+-----------------+-------+");

        for (Product p : daftarProduk) {
            System.out.println(p.toString());
        }

        System.out.println("+----------+----------------------+-----------------+-------+");
        System.out.println("  Total: " + daftarProduk.size() + " produk terdaftar");
    }

    /**
     * Jumlah produk yang terdaftar
     * Kompleksitas: O(1)
     */
    public int jumlahProduk() {
        return daftarProduk.size();
    }

    // ==================== DATA AWAL ====================

    /**
     * Inisialisasi 15 produk awal untuk kafe/toko.
     * Produk dimasukkan dalam urutan abjad supaya ArrayList langsung sorted
     * (karena tambahProduk() juga melakukan sorted insert).
     */
    private void initDataAwal() {
        tambahProduk(new Product("BRG001", "Americano", 18000, 50));
        tambahProduk(new Product("BRG002", "Caffe Latte", 25000, 40));
        tambahProduk(new Product("BRG003", "Cappuccino", 27000, 35));
        tambahProduk(new Product("BRG004", "Cheesecake", 35000, 20));
        tambahProduk(new Product("BRG005", "Croissant", 22000, 30));
        tambahProduk(new Product("BRG006", "Es Coklat", 20000, 35));
        tambahProduk(new Product("BRG007", "Jus Jeruk", 15000, 30));
        tambahProduk(new Product("BRG008", "Kentang Goreng", 18000, 40));
        tambahProduk(new Product("BRG009", "Matcha Latte", 30000, 25));
        tambahProduk(new Product("BRG010", "Mie Goreng", 25000, 30));
        tambahProduk(new Product("BRG011", "Mineral Water", 8000, 100));
        tambahProduk(new Product("BRG012", "Nasi Goreng", 28000, 35));
        tambahProduk(new Product("BRG013", "Roti Bakar", 20000, 40));
        tambahProduk(new Product("BRG014", "Sandwich", 23000, 25));
        tambahProduk(new Product("BRG015", "Teh Manis", 10000, 60));
    }
}
