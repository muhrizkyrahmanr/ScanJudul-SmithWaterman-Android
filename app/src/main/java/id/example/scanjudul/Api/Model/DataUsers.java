package id.example.scanjudul.Api.Model;

public class DataUsers {
    private String stambuk;
    private String nama;
    private String alamat;
    private String nohp;

    public DataUsers(String stambuk, String nama, String alamat, String nohp) {
        this.stambuk = stambuk;
        this.nama = nama;
        this.alamat = alamat;
        this.nohp = nohp;
    }

    public String getStambuk() {
        return stambuk;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNohp() {
        return nohp;
    }
}
