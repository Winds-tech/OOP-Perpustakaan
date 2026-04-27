import java.time.LocalDate;

public class Buku {
    private String    isbn;
    private String    judul;
    private String    pengarang;
    private boolean   tersedia;
    private LocalDate jatuhTempo;

    public Buku(String isbn, String judul, String pengarang) {
        this.isbn      = isbn;
        this.judul     = judul;
        this.pengarang = pengarang;
        this.tersedia  = true;
    }

    public String    getIsbn()      { return isbn; }
    public String    getJudul()     { return judul; }
    public String    getPengarang() { return pengarang; }
    public boolean   Tersedia()   { return tersedia; }
    public LocalDate getJatuhTempo(){ return jatuhTempo; }

    public void setTersedia(boolean b)      { this.tersedia  = b; }
    public void setJatuhTempo(LocalDate tgl){ this.jatuhTempo = tgl; }

    @Override
    public String toString() {
        String status = tersedia ? "Tersedia" : "Dipinjam";
        return String.format("[%s] %s - %s (%s)", isbn, judul, pengarang, status);
    }
}
