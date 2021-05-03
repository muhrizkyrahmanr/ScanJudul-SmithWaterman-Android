package id.example.scanjudul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.example.scanjudul.Api.Client;
import id.example.scanjudul.Api.InterfaceLogin;
import id.example.scanjudul.Api.Model.DataUsers;
import id.example.scanjudul.Api.Model.Login_response;
import id.example.scanjudul.ProgresDialog.ProgressDialog;
import id.example.scanjudul.Storage.SharedPMUser;
import id.or.example.sweetalert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText et_user, et_password;
    private Button btn_login;
    private ProgressDialog dialog;

    private String user,password;
    private InterfaceLogin mInterfaceLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_user = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        mInterfaceLogin = (InterfaceLogin) Client.getClient().create(InterfaceLogin.class);
        dialog = new ProgressDialog(this, "Please Wait..");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = et_user.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(user.isEmpty() == false && password.isEmpty() == false){
                    dialog.show();
                    mInterfaceLogin.login(user,password).enqueue(new Callback<Login_response>() {
                        @Override
                        public void onResponse(Call<Login_response> call, Response<Login_response> response) {
                            Login_response login_response = (Login_response) response.body();
                            DataUsers dataUsers = new DataUsers(login_response.getStambuk(),login_response.getNama(),login_response.getAlamat(),login_response.getNohp());
                            SharedPMUser.getmInstance(LoginActivity.this).saveUser(dataUsers);
                            if(!login_response.isError()){
                                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                SweetalertFailed(login_response.getMessage());
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Login_response> call, Throwable t) {
                            SweetalertFailed("Gagal Terhubung KeServer");
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void  SweetalertFailed(String ContentText){
        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(ContentText)
                .show();
    }
}
