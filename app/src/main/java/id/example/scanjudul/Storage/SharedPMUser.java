package id.example.scanjudul.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import id.example.scanjudul.Api.Model.DataUsers;

public class SharedPMUser {
    private static final String SHARED_PREF_NAME = "shared_pref_user";
    private static SharedPMUser mInstance;
    private Context mContext;

    private SharedPMUser(Context mContext2) {
        this.mContext = mContext2;
    }

    public static synchronized SharedPMUser getmInstance(Context mContext2) {
        SharedPMUser sharedPMUser;
        synchronized (SharedPMUser.class) {
            if (mInstance == null) {
                mInstance = new SharedPMUser(mContext2);
            }
            sharedPMUser= mInstance;
        }
        return sharedPMUser;
    }

    public void saveUser(DataUsers dataUsers){
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putString("stambuk", dataUsers.getStambuk());
        editor.putString("nama", dataUsers.getNama());
        editor.putString("alamat", dataUsers.getAlamat());
        editor.putString("nohp", dataUsers.getNohp());
        editor.apply();
    }

    public boolean isLoggedIn() {
        if (this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).getString("user", null) != null) {
            return true;
        }
        return false;
    }

    public DataUsers getUser() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        DataUsers dataUsers = new DataUsers(
                sharedPreferences.getString("stambuk", null),
                sharedPreferences.getString("nama", null),
                sharedPreferences.getString("alamat", null),
                sharedPreferences.getString("nohp",null));
        return dataUsers;
    }

    public void clear() {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.clear();
        editor.apply();
    }
}
