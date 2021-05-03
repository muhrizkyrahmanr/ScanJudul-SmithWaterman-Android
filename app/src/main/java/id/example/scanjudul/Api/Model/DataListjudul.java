package id.example.scanjudul.Api.Model;

public class DataListjudul {
    private String judul_skripsi;
    private String nama_mahasiswa;
    private String tahun;
    private String no_buku;
    private int kemiripan;
    private String kata_mirip;

    public DataListjudul(String judul_skripsi, String nama_mahasiswa, String tahun, String no_buku, int kemiripan, String kata_mirip) {
        this.judul_skripsi = judul_skripsi;
        this.nama_mahasiswa = nama_mahasiswa;
        this.tahun = tahun;
        this.no_buku = no_buku;
        this.kemiripan = kemiripan;
        this.kata_mirip = kata_mirip;
    }

    public String getJudul_skripsi() {
        return judul_skripsi;
    }

    public String getNama_mahasiswa() {
        return nama_mahasiswa;
    }

    public String getTahun() {
        return tahun;
    }

    public String getNo_buku() {
        return no_buku;
    }

    public int getKemiripan() {
        return kemiripan;
    }

    public String getKata_mirip() {
        return kata_mirip;
    }
}
