import java.util.Stack;
import java.util.ArrayList;

/*
Class Customer - Merepresentasikan pelanggan di sistem POS

Keranjang belanja menggunakan Stack<CartItem>

Alasan pakai Stack:
- Fitur void/undo harus membatalkan item TERAKHIR yang ditambahkan
- Stack bersifat LIFO (Last In, First Out), jadi item terakhir
  yang di-push akan menjadi yang pertama di-pop saat void
- push() untuk tambah item -> O(1)
- pop() untuk void item terakhir -> O(1)
 */

public class Customer {

    private String nama;
    private Stack<CartItem> keranjang;  // LIFO untuk fitur undo/void

    public Customer(String nama) {
        this.nama = nama;
        this.keranjang = new Stack<>();
    }

    // Getter
    public String getNama() { return nama; }
    public Stack<CartItem> getKeranjang() { return keranjang; }

    /*
    Menambah item ke keranjang belanja.
    Menggunakan Stack.push() -> item masuk di posisi paling atas (top)
     
    Kompleksitas: O(1) 
    - Stack.push() hanya menaruh elemen di atas stack
    - Tidak perlu geser elemen lain
    */

    public void tambahItem(CartItem item) {
        keranjang.push(item);
    }

    /**
    Void / Undo item terakhir yang ditambahkan ke keranjang.
    Menggunakan Stack.pop() -> mengambil dan menghapus item paling atas
     
    Kompleksitas: O(1)
    - Stack.pop() langsung mengakses elemen teratas
    - Tidak perlu iterasi atau pencarian
     
    @return CartItem yang di-void, null jika keranjang kosong
    */

    public CartItem voidItemTerakhir() {
        if (keranjang.isEmpty()) {
            return null;
        }
        return keranjang.pop();
    }

    /*
    Menghitung total harga seluruh item di keranjang.
    Harus iterasi semua elemen Stack untuk menjumlahkan subtotal.
     
    Kompleksitas: O(n) dimana n = jumlah item di keranjang
    - Setiap item dihitung subtotal-nya (O(1) per item)
    - Total = sum dari n item = O(n)
    */

    public double getTotal() {
        double total = 0;
        for (CartItem item : keranjang) {
            total += item.getSubtotal();
        }
        return total;
    }

    /*
    Cek apakah keranjang kosong
    Kompleksitas: O(1) - Stack.isEmpty() cek size
     */

    public boolean keranjangKosong() {
        return keranjang.isEmpty();
    }

    /*
    Jumlah item di keranjang
    Kompleksitas: O(1) - Stack.size() return variable internal
    */

    public int jumlahItem() {
        return keranjang.size();
    }

    /**
    Mengambil semua item dari keranjang sebagai ArrayList (snapshot).
    Digunakan saat menyimpan data transaksi selesai.
    
    Kompleksitas: O(n) - meng-copy seluruh elemen Stack ke ArrayList baru
    */
   
    public ArrayList<CartItem> getSemuaItem() {
        return new ArrayList<>(keranjang);
    }
}
