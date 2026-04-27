import java.util.ArrayList;
import java.util.List;

public class Perpustakaan {
    private String        nama;
    private List<Buku>    koleksiBuku;
    private List<Anggota> daftarAnggota;
    private List<Login>    daftarUser;

    public Perpustakaan(String nama) {
        this.nama          = nama;
        this.koleksiBuku   = new ArrayList<>();
        this.daftarAnggota = new ArrayList<>();
        this.daftarUser    = new ArrayList<>();
    }

    public String getNama() { return nama; }

    public void tambahBuku(Buku b)  { koleksiBuku.add(b); }

    public boolean tambahBukuBaru(String isbn, String judul, String pengarang) {
        if (cariBukuByIsbn(isbn) != null) return false;
        tambahBuku(new Buku(isbn, judul, pengarang));
        return true;
    }

    public Buku cariBukuByIsbn(String isbn) {
        String target = normalizeIsbn(isbn);
        for (Buku b : koleksiBuku)
            if (normalizeIsbn(b.getIsbn()).equals(target)) return b;
        return null;
    }

    public Buku cariBukuByInputPinjam(String input) {
        if (input == null) return null;

        String nilai = input.trim();
        if (nilai.isEmpty()) return null;

        try {
            int nomor = Integer.parseInt(nilai);
            if (nomor >= 1 && nomor <= koleksiBuku.size()) {
                return koleksiBuku.get(nomor - 1);
            }
        } catch (NumberFormatException e) {
        }

        return cariBukuByIsbn(nilai);
    }

    public boolean hapusBukuByInput(String input) {
        Buku target = cariBukuByInputPinjam(input);
        if (target == null) return false;
        if (!target.Tersedia()) return false;
        return koleksiBuku.remove(target);
    }

    public List<Buku> cariBuku(String keyword) {
        List<Buku> hasil = new ArrayList<>();
        for (Buku b : koleksiBuku)
            if (b.getJudul().toLowerCase().contains(keyword.toLowerCase())
             || b.getPengarang().toLowerCase().contains(keyword.toLowerCase()))
                hasil.add(b);
        return hasil;
    }

    public List<Buku> cariBuku(String keyword, boolean hanyaTersedia) {
        List<Buku> hasil = new ArrayList<>();
        for (Buku b : koleksiBuku) {
            boolean cocokKeyword = b.getJudul().toLowerCase().contains(keyword.toLowerCase())
                               || b.getPengarang().toLowerCase().contains(keyword.toLowerCase());
            if (!cocokKeyword) continue;

            if (!hanyaTersedia || b.Tersedia()) {
                hasil.add(b);
            }
        }
        return hasil;
    }

    public void tampilBuku() {
        System.out.println("\n  === KOLEKSI BUKU ===");
        for (int i = 0; i < koleksiBuku.size(); i++)
            System.out.println("  " + (i+1) + ". " + koleksiBuku.get(i));
    }

    public void tambahAnggota(Anggota a) { daftarAnggota.add(a); }

    public Anggota cariAnggota(String id) {
        for (Anggota a : daftarAnggota)
            if (a.getId().equalsIgnoreCase(id)) return a;
        return null;
    }

    public void tampilAnggota() {
        System.out.println("\n  === DAFTAR ANGGOTA ===");
        for (Anggota a : daftarAnggota)
            System.out.println("  - " + a);
    }

    public boolean hapusAnggotaDanUserById(String id) {
        Anggota target = cariAnggota(id);
        if (target == null) return false;

        daftarAnggota.remove(target);
        for (int i = daftarUser.size() - 1; i >= 0; i--) {
            Login u = daftarUser.get(i);
            if (u.getUsername().equalsIgnoreCase(id)) {
                daftarUser.remove(i);
            }
        }
        return true;
    }

    public void tambahUser(Login u) { daftarUser.add(u); }

    public Login loginUser(String username, String password) {
        for (Login u : daftarUser)
            if (u.getUsername().equals(username) && u.cekPassword(password)) return u;
        return null;
    }

    private String normalizeIsbn(String isbn) {
        if (isbn == null) return "";
        return isbn.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
    }
}
