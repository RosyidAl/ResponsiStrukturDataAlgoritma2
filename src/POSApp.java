import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class POSApp - Aplikasi utama Point of Sale System
 * 
 * Menyediakan antarmuka CLI interaktif untuk sistem kasir.
 * Menghubungkan semua komponen: ProductManager, QueueManager, TransactionManager.
 * 
 * Ringkasan Struktur Data yang digunakan:
 * 1. HashMap<String, Product>   -> pencarian produk by ID,     O(1) average
 * 2. ArrayList + Binary Search  -> pencarian produk by nama,   O(log n)
 * 3. Queue (LinkedList)         -> antrean pelanggan FIFO,     O(1) enqueue/dequeue
 * 4. Stack<CartItem>            -> keranjang belanja + undo,   O(1) push/pop
 * 5. ArrayList<Transaction>     -> riwayat transaksi,          O(1) add
 */
public class POSApp {

    private static Scanner scanner = new Scanner(System.in);
    private static ProductManager produkManager = new ProductManager();
    private static QueueManager queueManager = new QueueManager();
    private static TransactionManager trxManager = new TransactionManager();
    private static NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(
            new Locale("id", "ID"));

    // ==================== MAIN METHOD ====================

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            clearScreen();
            tampilkanHeader();
            tampilkanMenuUtama();
            int pilihan = inputAngka("Pilih menu");

            switch (pilihan) {
                case 1:
                    menuKelolaProduk();
                    break;
                case 2:
                    menuAntrean();
                    break;
                case 3:
                    menuKasir();
                    break;
                case 4:
                    menuRiwayat();
                    break;
                case 5:
                    running = false;
                    System.out.println("\n  Terima kasih telah menggunakan Sistem POS!");
                    System.out.println("  Program ditutup.\n");
                    break;
                default:
                    System.out.println("  [!] Menu tidak valid. Masukkan angka 1-5.");
            }
        }
        scanner.close();
    }

    // ==================== TAMPILAN UTAMA ====================

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private static void tampilkanHeader() {
        System.out.println();
        System.out.println("  ======================================================");
        System.out.println("  ||                                                  ||");
        System.out.println("  ||          SISTEM POS - KAFE NUSANTARA             ||");
        System.out.println("  ||          Point of Sale System v1.0               ||");
        System.out.println("  ||                                                  ||");
        System.out.println("  ||          Kelompok: Kelompok 11                   ||");
        System.out.println("  ||                                                  ||");
        System.out.println("  ======================================================");
        System.out.println();
    }

    private static void tampilkanMenuUtama() {
        System.out.println("  +----------------------------------------------------+");
        System.out.println("  |                   MENU UTAMA                       |");
        System.out.println("  +----------------------------------------------------+");
        System.out.println("  |  [1] Kelola Produk                                 |");
        System.out.println("  |  [2] Antrean Pelanggan                             |");
        System.out.println("  |  [3] Kasir (Proses Belanja)                        |");
        System.out.println("  |  [4] Riwayat Transaksi                             |");
        System.out.println("  |  [5] Keluar                                        |");
        System.out.println("  +----------------------------------------------------+");
    }

    // ==================== MENU 1: KELOLA PRODUK ====================

    private static void menuKelolaProduk() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n  === KELOLA PRODUK ===");
            System.out.println("  [1] Tampilkan Semua Produk");
            System.out.println("  [2] Cari Produk (by ID/Barcode)");
            System.out.println("  [3] Cari Produk (by Nama)");
            System.out.println("  [4] Tambah Produk Baru");
            System.out.println("  [0] Kembali ke Menu Utama");

            int pilihan = inputAngka("Pilih");

            switch (pilihan) {
                case 1:
                    System.out.println();
                    produkManager.tampilkanSemuaProduk();
                    break;
                case 2:
                    cariProdukById();
                    break;
                case 3:
                    cariProdukByNama();
                    break;
                case 4:
                    tambahProdukBaru();
                    break;
                case 0:
                    kembali = true;
                    break;
                default:
                    System.out.println("  [!] Pilihan tidak valid.");
            }
        }
    }

    /**
     * Pencarian produk berdasarkan ID.
     * Menggunakan HashMap -> O(1) average case
     */
    private static void cariProdukById() {
        System.out.print("  Masukkan ID Produk: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("  [!] ID tidak boleh kosong.");
            return;
        }

        // lookup HashMap -> O(1)
        Product p = produkManager.cariById(id);
        if (p != null) {
            System.out.println("\n  >> Produk ditemukan! (pencarian HashMap - O(1))");
            tampilkanSatuProduk(p);
        } else {
            System.out.println("  [!] Produk dengan ID '" + id + "' tidak ditemukan.");
        }
    }

    /**
     * Pencarian produk berdasarkan nama.
     * Menggunakan Binary Search pada sorted ArrayList -> O(log n)
     */
    private static void cariProdukByNama() {
        System.out.print("  Masukkan Nama Produk: ");
        String nama = scanner.nextLine().trim();

        if (nama.isEmpty()) {
            System.out.println("  [!] Nama tidak boleh kosong.");
            return;
        }

        // Binary Search -> O(log n)
        Product p = produkManager.cariByNama(nama);
        if (p != null) {
            System.out.println("\n  >> Produk ditemukan! (pencarian Binary Search - O(log n))");
            tampilkanSatuProduk(p);
        } else {
            System.out.println("  [!] Produk dengan nama '" + nama + "' tidak ditemukan.");
            System.out.println("  Catatan: Nama harus tepat sama (contoh: 'Americano', 'Nasi Goreng')");
        }
    }

    /**
     * Helper method untuk menampilkan 1 produk dalam format tabel
     */
    private static void tampilkanSatuProduk(Product p) {
        System.out.println("  +----------+----------------------+-----------------+-------+");
        System.out.println("  | ID       | Nama Produk          | Harga           | Stok  |");
        System.out.println("  +----------+----------------------+-----------------+-------+");
        System.out.println("  " + p.toString());
        System.out.println("  +----------+----------------------+-----------------+-------+");
    }

    /**
     * Menambah produk baru ke dalam sistem.
     * Input: ID, Nama, Harga, Stok awal
     * Validasi: ID tidak duplikat, harga > 0, stok >= 0
     */
    private static void tambahProdukBaru() {
        System.out.println("\n  --- Tambah Produk Baru ---");

        System.out.print("  ID Produk  : ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("  [!] ID tidak boleh kosong.");
            return;
        }

        System.out.print("  Nama Produk: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("  [!] Nama tidak boleh kosong.");
            return;
        }

        double harga = inputDouble("Harga");
        if (harga <= 0) {
            System.out.println("  [!] Harga harus lebih dari 0.");
            return;
        }

        int stok = inputAngka("Stok awal");
        if (stok < 0) {
            System.out.println("  [!] Stok tidak boleh negatif.");
            return;
        }

        Product produk = new Product(id, nama, harga, stok);
        if (produkManager.tambahProduk(produk)) {
            System.out.println("  [OK] Produk '" + nama + "' berhasil ditambahkan!");
        } else {
            System.out.println("  [!] Gagal! ID '" + id + "' sudah digunakan.");
        }
    }

    // ==================== MENU 2: ANTREAN PELANGGAN ====================

    private static void menuAntrean() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n  === ANTREAN PELANGGAN ===");
            System.out.println("  Jumlah dalam antrean: " + queueManager.jumlahAntrean());
            System.out.println("  [1] Tambah Pelanggan ke Antrean");
            System.out.println("  [2] Lihat Daftar Antrean");
            System.out.println("  [0] Kembali ke Menu Utama");

            int pilihan = inputAngka("Pilih");

            switch (pilihan) {
                case 1:
                    tambahPelanggan();
                    break;
                case 2:
                    System.out.println("\n  Daftar Antrean Pelanggan:");
                    System.out.println("  --------------------------");
                    queueManager.lihatAntrean();
                    break;
                case 0:
                    kembali = true;
                    break;
                default:
                    System.out.println("  [!] Pilihan tidak valid.");
            }
        }
    }

    /**
     * Menambah pelanggan baru ke antrean.
     * Menggunakan Queue.offer() -> O(1)
     */
    private static void tambahPelanggan() {
        System.out.print("  Nama Pelanggan: ");
        String nama = scanner.nextLine().trim();

        if (nama.isEmpty()) {
            System.out.println("  [!] Nama tidak boleh kosong.");
            return;
        }

        Customer customer = new Customer(nama);
        // masuk antrean -> offer() = O(1)
        queueManager.masukAntrean(customer);
        System.out.println("  [OK] '" + nama + "' masuk antrean. " +
                "(Posisi ke-" + queueManager.jumlahAntrean() + ")");
    }

    // ==================== MENU 3: KASIR ====================

    /**
     * Menu kasir - melayani pelanggan terdepan dari antrean.
     * 
     * Alur:
     * 1. Ambil pelanggan terdepan dari Queue (dequeue) -> O(1)
     * 2. Pelanggan bisa tambah item ke keranjang (Stack.push) -> O(1)
     * 3. Pelanggan bisa void item terakhir (Stack.pop) -> O(1)
     * 4. Proses pembayaran jika sudah selesai belanja
     */
    private static void menuKasir() {
        // cek apakah ada pelanggan di antrean
        if (queueManager.isEmpty()) {
            System.out.println("\n  [!] Antrean kosong!");
            System.out.println("  Silakan tambah pelanggan ke antrean terlebih dahulu (Menu 2).");
            return;
        }

        // ambil pelanggan terdepan -> Queue.poll() = O(1)
        Customer customer = queueManager.layaniBerikutnya();
        System.out.println("\n  ======================================================");
        System.out.println("   KASIR - Melayani: " + customer.getNama());
        System.out.println("  ======================================================");

        boolean selesai = false;
        while (!selesai) {
            System.out.println("\n  --- Menu Kasir ---");
            System.out.println("  [1] Tambah Item ke Keranjang");
            System.out.println("  [2] Void Item Terakhir (Undo)");
            System.out.println("  [3] Lihat Keranjang");
            System.out.println("  [4] Proses Pembayaran");
            System.out.println("  [0] Batalkan Transaksi & Kembali");

            int pilihan = inputAngka("Pilih");

            switch (pilihan) {
                case 1:
                    tambahItemKeKeranjang(customer);
                    break;
                case 2:
                    voidItemTerakhir(customer);
                    break;
                case 3:
                    tampilkanKeranjang(customer);
                    break;
                case 4:
                    selesai = prosesPembayaran(customer);
                    break;
                case 0:
                    batalkanTransaksi(customer);
                    selesai = true;
                    break;
                default:
                    System.out.println("  [!] Pilihan tidak valid.");
            }
        }
    }

    /**
     * Menambah item ke keranjang belanja pelanggan.
     * 
     * Proses:
     * 1. Input ID produk -> cari di HashMap = O(1)
     * 2. Input jumlah -> validasi stok
     * 3. Kurangi stok produk -> O(1)
     * 4. Push CartItem ke Stack keranjang -> O(1)
     * 
     * Total Kompleksitas: O(1) per operasi
     */
    private static void tambahItemKeKeranjang(Customer customer) {
        // Tampilkan daftar menu agar user tidak bingung
        System.out.println("\n  --- Daftar Menu ---");
        produkManager.tampilkanSemuaProduk();
        
        System.out.print("\n  Masukkan ID atau Nama Produk: ");
        String input = scanner.nextLine().trim();

        // Cari produk: coba cari di HashMap (by ID) terlebih dahulu -> O(1)
        Product produk = produkManager.cariById(input);
        
        // Jika tidak ketemu by ID, coba cari di ArrayList (by Nama) -> O(log n)
        if (produk == null) {
            produk = produkManager.cariByNama(input);
        }

        if (produk == null) {
            System.out.println("  [!] Produk dengan ID atau Nama '" + input + "' tidak ditemukan.");
            return;
        }

        // tampilkan info produk
        System.out.println("  Produk : " + produk.getNama());
        System.out.println("  Harga  : " + produk.formatHarga());
        System.out.println("  Stok   : " + produk.getStok());

        if (produk.getStok() == 0) {
            System.out.println("  [!] Stok produk ini habis!");
            return;
        }

        int jumlah = inputAngka("Jumlah beli");
        if (jumlah <= 0) {
            System.out.println("  [!] Jumlah harus lebih dari 0.");
            return;
        }

        // validasi stok cukup
        if (jumlah > produk.getStok()) {
            System.out.println("  [!] Stok tidak cukup! Stok tersedia: " + produk.getStok());
            return;
        }

        // kurangi stok produk -> O(1)
        produk.kurangiStok(jumlah);

        // buat CartItem dan push ke Stack keranjang -> O(1)
        CartItem item = new CartItem(produk, jumlah);
        customer.tambahItem(item);

        System.out.println("  [OK] " + produk.getNama() + " x" + jumlah +
                " ditambahkan ke keranjang.");
        System.out.println("  Subtotal item : " + rupiahFormat.format(item.getSubtotal()));
        System.out.println("  Total sementara: " + rupiahFormat.format(customer.getTotal()));
    }

    /**
     * Void / Undo item terakhir dari keranjang.
     * 
     * Menggunakan Stack.pop() untuk mengambil item paling atas (terakhir masuk).
     * Stok produk otomatis dikembalikan setelah void.
     * 
     * Kompleksitas: O(1)
     * - Stack.pop() = O(1)
     * - tambahStok() = O(1)
     */
    private static void voidItemTerakhir(Customer customer) {
        // pop dari Stack -> O(1)
        CartItem voided = customer.voidItemTerakhir();

        if (voided == null) {
            System.out.println("  [!] Keranjang kosong, tidak ada item untuk di-void.");
            return;
        }

        // kembalikan stok produk yang sudah dikurangi -> O(1)
        voided.getProduct().tambahStok(voided.getJumlah());

        System.out.println("  [VOID] " + voided.getProduct().getNama() +
                " x" + voided.getJumlah() + " dihapus dari keranjang.");
        System.out.println("  Stok '" + voided.getProduct().getNama() +
                "' dikembalikan menjadi: " + voided.getProduct().getStok());

        if (!customer.keranjangKosong()) {
            System.out.println("  Total sementara: " +
                    rupiahFormat.format(customer.getTotal()));
        } else {
            System.out.println("  Keranjang sekarang kosong.");
        }
    }

    /**
     * Menampilkan isi keranjang belanja pelanggan.
     * Iterasi seluruh Stack untuk menampilkan setiap item.
     * 
     * Kompleksitas: O(n) dimana n = jumlah item di keranjang
     */
    private static void tampilkanKeranjang(Customer customer) {
        if (customer.keranjangKosong()) {
            System.out.println("  Keranjang kosong.");
            return;
        }

        System.out.println("\n  Keranjang Belanja - " + customer.getNama());
        System.out.println("  +----------------------+-------+-----------------+-----------------+");
        System.out.println("  | Item                 | Qty   | Harga           | Subtotal        |");
        System.out.println("  +----------------------+-------+-----------------+-----------------+");

        for (CartItem item : customer.getKeranjang()) {
            System.out.println("  " + item.toString());
        }

        System.out.println("  +----------------------+-------+-----------------+-----------------+");
        System.out.println("  Total: " + rupiahFormat.format(customer.getTotal()) +
                " (" + customer.jumlahItem() + " item)");
    }

    /**
     * Proses pembayaran pelanggan.
     * 
     * @return true jika transaksi berhasil diproses, false jika gagal
     */
    private static boolean prosesPembayaran(Customer customer) {
        if (customer.keranjangKosong()) {
            System.out.println("  [!] Keranjang kosong! Tambah item terlebih dahulu.");
            return false;
        }

        // tampilkan keranjang dulu supaya pelanggan bisa review
        tampilkanKeranjang(customer);

        double total = customer.getTotal();
        System.out.println("\n  Total yang harus dibayar: " + rupiahFormat.format(total));

        double bayar = inputDouble("Jumlah bayar");
        if (bayar < 0) {
            System.out.println("  [!] Jumlah bayar tidak valid.");
            return false;
        }

        // validasi uang cukup
        if (bayar < total) {
            System.out.println("  [!] Pembayaran kurang!");
            System.out.println("  Kurang: " + rupiahFormat.format(total - bayar));
            return false;
        }

        // proses transaksi -> buat Transaction dan simpan ke riwayat
        Transaction trx = trxManager.prosesTransaksi(customer, bayar);

        // cetak struk di CLI
        trxManager.tampilkanStruk(trx);

        System.out.println("  [OK] Transaksi " + trx.getIdTransaksi() + " berhasil diproses!");

        // cek sisa antrean
        if (!queueManager.isEmpty()) {
            System.out.println("  Pelanggan berikutnya: " + queueManager.lihatPertama().getNama());
        } else {
            System.out.println("  Tidak ada pelanggan lagi di antrean.");
        }

        return true;
    }

    /**
     * Batalkan seluruh transaksi pelanggan.
     * Kembalikan stok untuk semua item yang sudah masuk keranjang.
     * 
     * Kompleksitas: O(n) dimana n = jumlah item di keranjang
     * - Harus iterasi semua item untuk mengembalikan stok masing-masing
     */
    private static void batalkanTransaksi(Customer customer) {
        if (!customer.keranjangKosong()) {
            // kembalikan stok untuk setiap item di keranjang
            for (CartItem item : customer.getKeranjang()) {
                item.getProduct().tambahStok(item.getJumlah());
            }
            System.out.println("  [!] Transaksi dibatalkan. Seluruh stok telah dikembalikan.");
        } else {
            System.out.println("  Transaksi dibatalkan. (Keranjang kosong)");
        }
    }

    // ==================== MENU 4: RIWAYAT TRANSAKSI ====================

    private static void menuRiwayat() {
        System.out.println("\n  === RIWAYAT TRANSAKSI ===");
        trxManager.tampilkanRiwayat();
    }

    // ==================== UTILITAS INPUT ====================

    /**
     * Membaca input angka integer dari user dengan validasi.
     * Jika input bukan angka, akan menampilkan pesan error.
     * 
     * @param prompt teks yang ditampilkan sebelum input
     * @return angka yang diinput user, atau -1 jika invalid
     */
    private static int inputAngka(String prompt) {
        System.out.print("  " + prompt + ": ");
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Input harus berupa angka bulat.");
            return -1;
        }
    }

    /**
     * Membaca input angka desimal (double) dari user dengan validasi.
     * Digunakan untuk input harga dan jumlah pembayaran.
     * 
     * @param prompt teks yang ditampilkan sebelum input
     * @return angka desimal yang diinput, atau -1 jika invalid
     */
    private static double inputDouble(String prompt) {
        System.out.print("  " + prompt + ": ");
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Input harus berupa angka.");
            return -1;
        }
    }
}
