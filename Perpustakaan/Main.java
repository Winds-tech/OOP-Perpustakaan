import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner      sc     = new Scanner(System.in);
    static Perpustakaan perpus = new Perpustakaan("Perpustakaan Universitas");
    static Login         userLogin = null;
    static Anggota      anggotaLogin = null;

    public static void main(String[] args) {
        seedData();
        menuLogin();
        sc.close();
    }


    static void menuLogin() {
        while (true) {
            System.out.println("\n=== " + perpus.getNama() + " ===");
            System.out.println("[1] Login");
            System.out.println("[2] Daftar Akun Baru");
            System.out.println("[0] Keluar");
            System.out.print("Pilihan: ");
            String p = sc.nextLine().trim();

            if (p.equals("1"))      doLogin();
            else if (p.equals("2")) daftarAkunBaru();
            else if (p.equals("0")) { System.out.println("Sampai jumpa!"); break; }
        }
    }

    static void doLogin() {
        System.out.print("ID/NIM/NIP: "); String u = sc.nextLine().trim();
        System.out.print("Password: "); String pw = sc.nextLine().trim();
        Login user = perpus.loginUser(u, pw);
        if (user == null) { System.out.println("ID/password salah."); return; }

        userLogin = user;

        if (isPustakawan()) {
            anggotaLogin = null;
            System.out.println("Login berhasil! Selamat datang, " + user.getUsername());
            menuUtama();
            userLogin    = null;
            anggotaLogin = null;
            return;
        }

        Anggota anggota = perpus.cariAnggota(u);
        if (anggota == null) {
            System.out.println("Data anggota tidak ditemukan.");
            userLogin = null;
            return;
        }

        anggotaLogin = anggota;
        System.out.println("Login berhasil! Selamat datang, " + anggota.getNama());
        menuUtama();
        userLogin    = null;
        anggotaLogin = null;
    }

    static void daftarAkunBaru() {
        System.out.println("\n-- Daftar Akun Baru --");
        System.out.println("[1] Mahasiswa");
        System.out.println("[2] Dosen");
        System.out.print("Tipe: ");
        String tipe = sc.nextLine().trim();

        System.out.print("ID/NIM/NIP : "); String id   = sc.nextLine().trim();
        System.out.print("Nama       : "); String nama = sc.nextLine().trim();
        System.out.print("Password   : "); String pw   = sc.nextLine().trim();

        if (tipe.equals("1")) {
            System.out.print("Jurusan    : "); String jurusan = sc.nextLine().trim();
            perpus.tambahAnggota(new Mahasiswa(id, nama, jurusan));
            perpus.tambahUser(new Login(id, pw, "mahasiswa"));
            System.out.println("Akun mahasiswa berhasil dibuat! Silakan login.");
        } else if (tipe.equals("2")) {
            System.out.print("Mata Kuliah: "); String mk = sc.nextLine().trim();
            perpus.tambahAnggota(new Dosen(id, nama, mk));
            perpus.tambahUser(new Login(id, pw, "dosen"));
            System.out.println("Akun dosen berhasil dibuat! Silakan login.");
        } else {
            System.out.println("Tipe tidak valid.");
        }
    }

    static void menuUtama() {
        boolean aktif = true;
        while (aktif) {
            if (isPustakawan()) {
                System.out.println("\n--- Menu Pustakawan [" + userLogin.getUsername() + "] ---");
                System.out.println("[1] Lihat & Cari Buku");
                System.out.println("[2] Lihat Semua Anggota");
                System.out.println("[3] Hapus Anggota");
                System.out.println("[4] Tambah Buku");
                System.out.println("[5] Hapus Buku");
                System.out.println("[0] Logout");
            } else {
                System.out.println("\n--- Menu Anggota [" + anggotaLogin.getNama() + "] ---");
                System.out.println("[1] Lihat & Cari Buku");
                System.out.println("[2] Pinjam Buku");
                System.out.println("[3] Kembalikan Buku");
                System.out.println("[4] Lihat Pinjaman Saya");
                System.out.println("[0] Logout");
            }
            System.out.print("Pilihan: ");
            String p = sc.nextLine().trim();

            if (isPustakawan()) {
                switch (p) {
                    case "1":
                        menuLihatCari();
                        break;
                    case "2":
                        perpus.tampilAnggota();
                        break;
                    case "3":
                        menuHapusAnggota();
                        break;
                    case "4":
                        menuTambahBuku();
                        break;
                    case "5":
                        menuHapusBuku();
                        break;
                    case "0":
                        System.out.println("Logout...");
                        aktif = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } else {
                switch (p) {
                    case "1":
                        menuLihatCari();
                        break;
                    case "2":
                        menuPinjam();
                        break;
                    case "3":
                        menuKembali();
                        break;
                    case "4":
                        anggotaLogin.lihatPinjaman();
                        break;
                    case "0":
                        System.out.println("Logout...");
                        aktif = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            }
        }
    }

    static void menuHapusAnggota() {
        System.out.println("\n-- Hapus Anggota --");
        perpus.tampilAnggota();
        System.out.print("Masukkan ID/NIM/NIP anggota yang akan dihapus: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("  ID tidak boleh kosong.");
            return;
        }

        boolean berhasil = perpus.hapusAnggotaDanUserById(id);
        if (berhasil) {
            System.out.println("  Data anggota berhasil dihapus.");
        } else {
            System.out.println("  Anggota dengan ID tersebut tidak ditemukan.");
        }
    }

    static void menuTambahBuku() {
        System.out.println("\n-- Tambah Buku --");
        System.out.print("ISBN      : ");
        String isbn = sc.nextLine().trim();
        System.out.print("Judul     : ");
        String judul = sc.nextLine().trim();
        System.out.print("Pengarang : ");
        String pengarang = sc.nextLine().trim();

        if (isbn.isEmpty() || judul.isEmpty() || pengarang.isEmpty()) {
            System.out.println("  Data buku tidak boleh kosong.");
            return;
        }

        boolean berhasil = perpus.tambahBukuBaru(isbn, judul, pengarang);
        if (berhasil) {
            System.out.println("  Buku berhasil ditambahkan.");
        } else {
            System.out.println("  ISBN sudah terdaftar. Gunakan ISBN lain.");
        }
    }

    static void menuHapusBuku() {
        System.out.println("\n-- Hapus Buku --");
        perpus.tampilBuku();
        System.out.print("Masukkan nomor daftar atau ISBN buku: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("  Input tidak boleh kosong.");
            return;
        }

        Buku cek = perpus.cariBukuByInputPinjam(input);
        if (cek == null) {
            System.out.println("  Buku tidak ditemukan.");
            return;
        }
        if (!cek.Tersedia()) {
            System.out.println("  Buku sedang dipinjam, tidak bisa dihapus.");
            return;
        }

        boolean berhasil = perpus.hapusBukuByInput(input);
        if (berhasil) {
            System.out.println("  Buku berhasil dihapus.");
        } else {
            System.out.println("  Gagal menghapus buku.");
        }
    }

    static void menuLihatCari() {
        perpus.tampilBuku();
        System.out.print("\nCari buku (Enter untuk skip): ");
        String kw = sc.nextLine().trim();
        if (!kw.isEmpty()) {
            System.out.print("Hanya tampilkan buku tersedia? (y/n): ");
            String pilihFilter = sc.nextLine().trim();
            boolean hanyaTersedia = pilihFilter.equalsIgnoreCase("y");

            List<Buku> hasil;
            if (hanyaTersedia) {
                hasil = perpus.cariBuku(kw, true);
            } else {
                hasil = perpus.cariBuku(kw);
            }

            System.out.println("  Hasil (" + hasil.size() + "):");
            for (Buku b : hasil) System.out.println("  - " + b);
        }
    }

    static void menuPinjam() {
        System.out.println("\n-- Pinjam Buku --");
        System.out.println("  Pinjaman: " + anggotaLogin.getBukuDipinjam().size() + "/" + anggotaLogin.getMaxPinjam());

        if (anggotaLogin.getBukuDipinjam().size() >= anggotaLogin.getMaxPinjam()) {
            System.out.println("  Batas maksimal " + anggotaLogin.getMaxPinjam() + " buku sudah tercapai. Tidak dapat meminjam.");
            return;
        }

        perpus.tampilBuku();
        System.out.print("Pilih nomor daftar atau ISBN buku: ");
        String inputBuku = sc.nextLine().trim();
        Buku buku = perpus.cariBukuByInputPinjam(inputBuku);
        if (buku == null) { System.out.println("  Buku tidak ditemukan. Masukkan nomor/ISBN yang valid."); return; }
        anggotaLogin.pinjamBuku(buku);
    }

    static void menuKembali() {
        System.out.println("\n-- Kembalikan Buku --");
        anggotaLogin.lihatPinjaman();
        if (anggotaLogin.getBukuDipinjam().isEmpty()) return;

        System.out.print("ISBN buku: ");
        String isbn = sc.nextLine().trim();
        Buku buku = perpus.cariBukuByIsbn(isbn);
        if (buku == null) { System.out.println("  Buku tidak ditemukan."); return; }
        long denda = anggotaLogin.kembalikanBuku(buku);
        if (denda > 0)
            System.out.println("  Total denda: Rp" + denda);
    }

    
    static void seedData() {
        perpus.tambahBuku(new Buku("978-1", "Clean Code",                  "Robert"));
        perpus.tambahBuku(new Buku("978-2", "Design Patterns",             "Gang"));
        perpus.tambahBuku(new Buku("978-3", "Algoritma & Struktur Data",   "Cormen"));
        perpus.tambahBuku(new Buku("978-4", "Pemrograman Java",            "James Gosling"));
        perpus.tambahBuku(new Buku("978-5", "Basis Data Modern",           "CJ "));
        perpus.tambahBuku(new Buku("978-6", "Sistem Operasi",              "William Jemping"));

        perpus.tambahUser(new Login("admin", "admin123", "pustakawan"));

    }

    static boolean isPustakawan() {
        return userLogin != null && "pustakawan".equalsIgnoreCase(userLogin.getRole());

    }
}
