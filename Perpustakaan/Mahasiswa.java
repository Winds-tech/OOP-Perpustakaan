public class Mahasiswa extends Anggota {
    private String nim;
    private String jurusan;

    public Mahasiswa(String nim, String nama, String jurusan) {
        setId(nim);
        setNama(nama);
        setMaxPinjam(5);
        this.nim     = nim;
        this.jurusan = jurusan;
    }

    public String getNim()     { return nim; }
    public String getJurusan() { return jurusan; }

    @Override
    public String infoAnggota() {
        return "[Mahasiswa] " + getId() + " | " + getNama() + " | Pinjam: "
             + getBukuDipinjam().size() + "/" + getMaxPinjam() + " | Jurusan: " + jurusan;
    }
}
