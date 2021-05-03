package id.example.scanjudul.Api.Model;

public class Login_response {
        private boolean error;
        private String message;
        private String stambuk;
        private String nama;
        private String alamat;
        private String nohp;

    public Login_response(boolean error, String message, String stambuk, String nama, String alamat, String nohp) {
        this.error = error;
        this.message = message;
        this.stambuk = stambuk;
        this.nama = nama;
        this.alamat = alamat;
        this.nohp = nohp;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
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
