package id.example.scanjudul.Api;

import id.example.scanjudul.Api.Model.Listjudul_response;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceListJudul {
    @FormUrlEncoded
    @POST("scanjudul.php")
    Call<Listjudul_response> getListJudul(@Field("judul") String str, @Query("page") int str1);
}
