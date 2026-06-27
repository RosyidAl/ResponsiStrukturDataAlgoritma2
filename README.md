# Sistem POS (Point of Sale) - Kafe Nusantara

Aplikasi kasir (Point of Sale) berbasis CLI yang dikembangkan menggunakan bahasa Java murni. Sistem ini mampu menangani transaksi penjualan secara real-time, melakukan pencarian data produk secara cepat dan efisien, mengelola antrean pelanggan di loket kasir, serta memproses pembatalan item belanjaan terakhir (Void/Undo).

**Mata Kuliah**: Struktur Data dan Algoritma  
**Tema**: Tema 3 - POS (Point of Sale) System

## Anggota Kelompok

| No | Nama                        | NIM        |
|----|-----------------------------|------------|
| 1  | Ahmad Fahmi Haykal          | L0125001   |
| 2  | Rosyid Al Ansorri           | L0125065   |
| 3  | Keisha Nasywa               | L0124102   |
| 4  | Satrio Unggul Prayogo       | L0125114   |

## Fitur Utama

### 1. Manajemen Produk
- Menampilkan seluruh daftar produk dalam format tabel
- **Pencarian produk berdasarkan ID/Barcode** menggunakan HashMap *(O(1) average)*
- **Pencarian produk berdasarkan Nama** menggunakan Binary Search *(O(log n))*
- Menambahkan produk baru ke sistem (otomatis sorted)

### 2. Antrean Pelanggan (Queue)
- Menambahkan pelanggan ke antrean kasir *(enqueue - O(1))*
- Melayani pelanggan sesuai urutan kedatangan *(dequeue - O(1))*
- Melihat daftar seluruh antrean
- Prinsip FIFO: First In, First Out

### 3. Kasir (Proses Belanja)
- Melayani pelanggan terdepan dari antrean secara otomatis
- Menambah item ke keranjang belanja *(Stack push - O(1))*
- **Void/Undo item terakhir** dengan otomatis memulihkan stok *(Stack pop - O(1))*
- Proses pembayaran dengan validasi uang cukup
- **Cetak struk/receipt** di layar CLI

### 4. Riwayat Transaksi
- Menyimpan seluruh transaksi yang telah selesai
- Menampilkan ringkasan transaksi (ID, pelanggan, total, waktu)
- Menghitung total pendapatan

## Struktur Data dan Algoritma

| Struktur Data | Kegunaan dalam Program | Kompleksitas | Alasan Pemilihan |
|---------------|----------------------|-------------|------------------|
| **HashMap\<String, Product\>** | Pencarian produk berdasarkan ID/barcode | O(1) average | HashMap menggunakan teknik hashing dimana key (ID produk) di-hash untuk menentukan posisi penyimpanan. Saat pencarian, hash dihitung ulang sehingga langsung menuju posisi tanpa iterasi. Sangat cocok untuk operasi kasir yang butuh lookup cepat. |
| **ArrayList\<Product\>** (sorted) + **Binary Search** | Pencarian produk berdasarkan nama | O(log n) | ArrayList di-maintain dalam kondisi sorted berdasarkan nama produk. Binary Search bekerja dengan membagi ruang pencarian menjadi separuh setiap iterasi. Misal 1000 produk, hanya butuh ~10 perbandingan. Jauh lebih efisien dari linear search O(n). |
| **Queue (LinkedList\<Customer\>)** | Antrean pelanggan di loket kasir | O(1) enqueue/dequeue | Queue mengimplementasikan prinsip FIFO (First In First Out) yang sesuai dengan konsep antrean di dunia nyata. LinkedList dipilih karena operasi offer() dan poll() hanya mengubah pointer (head/tail), tidak perlu menggeser elemen. |
| **Stack\<CartItem\>** | Keranjang belanja + Void/Undo | O(1) push/pop | Stack mengimplementasikan prinsip LIFO (Last In First Out). Saat user ingin void/undo, yang dibatalkan adalah item **terakhir** yang dimasukkan - cocok dengan operasi pop() di Stack. push() untuk tambah item dan pop() untuk void, keduanya O(1). |
| **ArrayList\<Transaction\>** | Riwayat transaksi | O(1) add | ArrayList digunakan untuk menyimpan data transaksi secara kronologis (berurutan waktu). Operasi add() di akhir list adalah O(1) amortized. Cocok untuk data yang terus bertambah. |

## Struktur File

```
Responsi 2 SDA/
├── src/
│   ├── Product.java            # Model data produk (implements Comparable)
│   ├── CartItem.java           # Model item di keranjang belanja
│   ├── Customer.java           # Model pelanggan dengan Stack keranjang
│   ├── Transaction.java        # Model data transaksi selesai
│   ├── ProductManager.java     # Manajemen produk (HashMap + Binary Search)
│   ├── QueueManager.java       # Manajemen antrean pelanggan (Queue)
│   ├── TransactionManager.java # Proses transaksi & cetak struk
│   └── POSApp.java             # Aplikasi utama (Main class + CLI)
└── README.md                   # Dokumentasi proyek
```

## Panduan Menjalankan Program

### Prasyarat
- **Java Development Kit (JDK)** versi 8 atau lebih baru
- Terminal / Command Prompt / PowerShell

### Cara Kompilasi dan Menjalankan

```bash
# 1. Buka terminal dan masuk ke folder src
cd src

# 2. Kompilasi semua file Java sekaligus
javac *.java

# 3. Jalankan program utama
java POSApp
```

### Alur Penggunaan Program

1. **Jalankan program** → tampil header dan menu utama
2. **Menu 1 - Kelola Produk**: Lihat daftar produk, cari by ID/Nama, atau tambah produk baru
3. **Menu 2 - Antrean Pelanggan**: Tambahkan pelanggan ke antrean kasir
4. **Menu 3 - Kasir**: Layani pelanggan terdepan dari antrean
   - Tambah item ke keranjang (input ID + jumlah)
   - Gunakan Void/Undo untuk membatalkan item terakhir
   - Proses pembayaran → struk otomatis dicetak
5. **Menu 4 - Riwayat Transaksi**: Lihat seluruh transaksi yang sudah selesai
6. **Menu 5 - Keluar**: Tutup program

## Library yang Digunakan

Program ini **tidak menggunakan library eksternal**. Seluruh implementasi menggunakan library standar bawaan Java:

| Library | Kegunaan |
|---------|----------|
| `java.util.HashMap` | Penyimpanan dan pencarian produk by ID |
| `java.util.ArrayList` | Penyimpanan produk terurut dan riwayat transaksi |
| `java.util.LinkedList` | Implementasi Queue untuk antrean pelanggan |
| `java.util.Stack` | Implementasi keranjang belanja dengan fitur undo |
| `java.util.Collections` | Binary search untuk pencarian posisi insert |
| `java.util.Scanner` | Membaca input dari keyboard |
| `java.text.NumberFormat` | Format angka ke mata uang Rupiah |
| `java.time.LocalDateTime` | Timestamp transaksi |
