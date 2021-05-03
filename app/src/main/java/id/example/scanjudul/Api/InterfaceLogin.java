package id.example.scanjudul.Api;

import id.example.scanjudul.Api.Model.Login_response;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InterfaceLogin {
    @FormUrlEncoded
    @POST("login.php")
    Call<Login_response> login(@Field("stambuk") String str, @Field("password") String str1);
}
