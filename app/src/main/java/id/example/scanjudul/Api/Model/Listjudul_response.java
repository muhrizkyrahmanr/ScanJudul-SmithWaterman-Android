package id.example.scanjudul.Api.Model;

import java.util.List;

public class Listjudul_response {
    private boolean error;
    private String message;
    private List<DataListjudul> listjudul;

    public Listjudul_response(boolean error, String message, List<DataListjudul> listjudul) {
        this.error = error;
        this.message = message;
        this.listjudul = listjudul;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<DataListjudul> getListjudul() {
        return listjudul;
    }
}
