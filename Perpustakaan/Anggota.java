import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Anggota {
    private   String     id;
    private   String     nama;
    protected int        maxPinjam;
    protected List<Buku> bukuDipinjam;

    public static final int  DURASI_HARI  = 7;
    public static final long DENDA_PERHARI = 10000;

    protected Anggota() {
        this("", "", 0);
    }

    public Anggota(String id, String nama, int maxPinjam) {
        this.id          = id;
        this.nama        = nama;
        this.maxPinjam   = maxPinjam;
        this.bukuDipinjam = new ArrayList<>();
    }

    protected void setId(String id)       { this.id = id; }
    protected void setNama(String nama)   { this.nama = nama; }
    protected void setMaxPinjam(int max)  { this.maxPinjam = max; }

    public String     getId()           { return id; }
    public String     getNama()         { return nama; }
    public int        getMaxPinjam()    { return maxPinjam; }
    public List<Buku> getBukuDipinjam() { return bukuDipinjam; }

    public boolean pinjamBuku(Buku buku) {
        if (!buku.Tersedia()) {
            System.out.println("  Buku tidak tersedia.");
            return false;
        }
        if (bukuDipinjam.size() >= maxPinjam) {
            System.out.println("  Tidak bisa meminjam, batas maksimal " + maxPinjam + " buku sudah tercapai.");
            return false;
        }
        buku.setTersedia(false);
        buku.setJatuhTempo(LocalDate.now().plusDays(DURASI_HARI));
        bukuDipinjam.add(buku);
        System.out.println("  Berhasil meminjam: " + buku.getJudul());
        System.out.println("  Jatuh tempo: " + buku.getJatuhTempo());
        return true;
    }

    public long kembalikanBuku(Buku buku) {
        if (!bukuDipinjam.contains(buku)) {
            System.out.println("  Buku tidak ada di daftar pinjaman Anda.");
            return -1;
        }
        long denda = 0;
        LocalDate sekarang = LocalDate.now();
        if (sekarang.isAfter(buku.getJatuhTempo())) {
            long telat = ChronoUnit.DAYS.between(buku.getJatuhTempo(), sekarang);
            denda = telat * DENDA_PERHARI;
            System.out.println("  Terlambat " + telat + " hari. Denda: Rp" + denda);
        } else {
            System.out.println("  Tepat waktu, tidak ada denda.");
        }
        bukuDipinjam.remove(buku);
        buku.setTersedia(true);
        buku.setJatuhTempo(null);
        System.out.println("  Buku berhasil dikembalikan.");
        return denda;
    }

    public void lihatPinjaman() {
        System.out.println("\n  Pinjaman " + nama + ":");
        if (bukuDipinjam.isEmpty()) { System.out.println("  (kosong)"); return; }
        LocalDate sekarang = LocalDate.now();
        for (int i = 0; i < bukuDipinjam.size(); i++) {
            Buku b = bukuDipinjam.get(i);
            long telat = b.getJatuhTempo() != null ? ChronoUnit.DAYS.between(b.getJatuhTempo(), sekarang) : 0;
            String info = telat > 0 ? " [TELAT " + telat + " hari | Denda: Rp" + (telat * DENDA_PERHARI) + "]"
                                    : " [Jatuh tempo: " + b.getJatuhTempo() + "]";
            System.out.println("  " + (i+1) + ". " + b.getJudul() + info);
        }
    }

    public String infoAnggota() {
        return id + " | " + nama + " | Pinjam: " + bukuDipinjam.size() + "/" + maxPinjam;
    }

    @Override
    public String toString() { return infoAnggota(); }
}
