import java.util.LinkedList;
import java.util.Queue;

/*
Class QueueManager - Mengelola antrean pelanggan di loket kasir
 
Menggunakan Queue yang diimplementasikan dengan LinkedList.
 
Alasan pakai Queue:
- Antrean pelanggan bersifat FIFO (First In, First Out)
- Pelanggan yang datang lebih dulu, harus dilayani lebih dulu
- Operasi utama: masuk antrean (enqueue) dan keluar antrean (dequeue)
 
Alasan pakai LinkedList sebagai implementasi Queue:
- LinkedList mengimplementasikan interface Queue di Java
- enqueue (offer/add) = menambah node di tail -> O(1)
- dequeue (poll/remove) = menghapus node di head -> O(1)
- Tidak perlu resize seperti array-based queue
*/
public class QueueManager {

    private Queue<Customer> antrean;  // FIFO queue

    public QueueManager() {
        antrean = new LinkedList<>();
    }

    /*
    Menambah pelanggan ke akhir antrean (enqueue).
    Menggunakan Queue.offer() yang menambahkan elemen di bagian belakang.
     
    Kompleksitas: O(1)
    - LinkedList.offer() = addLast()
    - Hanya mengubah pointer tail, tidak perlu geser elemen
    */
    
    public void masukAntrean(Customer customer) {
        antrean.offer(customer);
    }

    /*
    Mengambil dan mengeluarkan pelanggan paling depan dari antrean (dequeue).
    Menggunakan Queue.poll() yang mengeluarkan elemen di bagian depan.
     
    Kompleksitas: O(1)
    - LinkedList.poll() = removeFirst()
    - Hanya mengubah pointer head ke node berikutnya
    - Inilah prinsip FIFO: yang masuk pertama, keluar pertama
     
    @return Customer paling depan, atau null jika antrean kosong
    */
    
    public Customer layaniBerikutnya() {
        return antrean.poll();
    }

    /*
    Melihat pelanggan paling depan tanpa mengeluarkannya.
    Menggunakan Queue.peek() -> hanya intip, tidak menghapus.
     
    Kompleksitas: O(1) - langsung akses head
    */
    
    public Customer lihatPertama() {
        return antrean.peek();
    }

    /*
    Menampilkan seluruh daftar antrean ke layar CLI.
    Harus iterasi seluruh Queue dari depan ke belakang.
    
    Kompleksitas: O(n) dimana n = jumlah pelanggan dalam antrean
    */
    
    public void lihatAntrean() {
        if (antrean.isEmpty()) {
            System.out.println("  Antrean kosong. Belum ada pelanggan.");
            return;
        }

        int nomor = 1;
        for (Customer c : antrean) {
            String penanda = (nomor == 1) ? " <-- Dilayani berikutnya" : "";
            System.out.println("  " + nomor + ". " + c.getNama() + penanda);
            nomor++;
        }
        System.out.println("  Total antrean: " + antrean.size() + " pelanggan");
    }

    /*
    Mendapatkan jumlah pelanggan dalam antrean
    Kompleksitas: O(1) - LinkedList menyimpan variabel size
    */
    
    public int jumlahAntrean() {
        return antrean.size();
    }

    /*
    Mengecek apakah antrean kosong
    Kompleksitas: O(1)
    */
    
    public boolean isEmpty() {
        return antrean.isEmpty();
    }
}
