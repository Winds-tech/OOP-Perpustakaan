public class Dosen extends Anggota {
    private String nip;
    private String mataKuliah;

    public Dosen(String nip, String nama, String mataKuliah) {
        setId(nip);
        setNama(nama);
        setMaxPinjam(10);
        this.nip        = nip;
        this.mataKuliah = mataKuliah;
    }

    public String getNip()        { return nip; }
    public String getMataKuliah() { return mataKuliah; }

    @Override
    public String infoAnggota() {
        return "[Dosen] " + getId() + " | " + getNama() + " | Pinjam: "
             + getBukuDipinjam().size() + "/" + getMaxPinjam() + " | MK: " + mataKuliah;
    }
}
