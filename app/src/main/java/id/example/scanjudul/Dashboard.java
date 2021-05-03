package id.example.scanjudul;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import id.example.scanjudul.Adapter.AdapterListJudul;
import id.example.scanjudul.Api.Client;
import id.example.scanjudul.Api.InterfaceListJudul;
import id.example.scanjudul.Api.Model.DataListjudul;
import id.example.scanjudul.Api.Model.DataUsers;
import id.example.scanjudul.Api.Model.Listjudul_response;
import id.example.scanjudul.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private TextView tv_stambuk, tv_nama;
    private Toolbar toolbar;
    private EditText et_judul;
    private ImageView iv_gambar;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;

    private RelativeLayout errorLayout;
    private TextView errorTitle,errorMessage;
    private Button errorRetry;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<DataListjudul> dataListjudul = new ArrayList();
    private InterfaceListJudul mInterfaceListJudul;

    private AdapterListJudul mAdapterListJudul;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    private int page_number = 1;
    private int totalItemCount;
    private int view_threshold = 10;
    private int visibleItemCount;
    private int pastVisableItems;
    private boolean isLoading = true;
    private int previous_total = 0;

    private String judulSkripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        DataUsers dataUser = SharedPMUser.getmInstance(this).getUser();
        mInterfaceListJudul = (InterfaceListJudul) Client.getClient().create(InterfaceListJudul.class);

        tv_stambuk = (TextView) findViewById(R.id.tv_stambuk);
        tv_nama = (TextView) findViewById(R.id.tv_nama);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_judul = (EditText) findViewById(R.id.et_judul);
        iv_gambar = (ImageView) findViewById(R.id.iv_gambar);

        mRecyclerView = (RecyclerView) findViewById(R.id.listjudul);
        layoutManager = new LinearLayoutManager(Dashboard.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperfrsh);

        errorLayout = (RelativeLayout) findViewById(R.id.errorLayout);
        errorTitle = (TextView) findViewById(R.id.errorTitle);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorRetry = (Button) findViewById(R.id.errorRetry);

        setSupportActionBar(toolbar);

        tv_stambuk.setText(dataUser.getStambuk());
        tv_nama.setText(dataUser.getNama());


        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataListjudul.clear();
                isLoading = true;
                previous_total = 0;
                page_number = 1;
                getData();
            }
        });

        getData();

        et_judul.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(judulSkripsi.length() <= 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisableItems = layoutManager.findFirstVisibleItemPosition();
                    if (dy > 0) {
                        if (isLoading && totalItemCount > previous_total) {
                            isLoading = false;
                            previous_total = totalItemCount;
                        }
                        if (!isLoading && totalItemCount - visibleItemCount <= pastVisableItems + view_threshold) {
                            page_number = page_number + 1;
                            loadPage(page_number);
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.tambahgambar){
            tampilkanMasukkanGambarDialog();
        }
//        if(id == R.id.profil){
//            Toast.makeText(this, "Pengaturan", Toast.LENGTH_SHORT).show();
//        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        page_number = 1;
        dataListjudul.clear();
        errorLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        judulSkripsi = et_judul.getText().toString();
        mInterfaceListJudul.getListJudul(judulSkripsi,page_number).enqueue(new Callback<Listjudul_response>() {
            @Override
            public void onResponse(Call<Listjudul_response> call, Response<Listjudul_response> response) {
                    Listjudul_response listjudul_response = (Listjudul_response) response.body();
                    if(!listjudul_response.isError()){
                        mAdapterListJudul = new AdapterListJudul(((Listjudul_response) response.body()).getListjudul(), Dashboard.this, judulSkripsi);
                        mRecyclerView.setAdapter(mAdapterListJudul);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }else{
                        showErrorMessage("Oops..", listjudul_response.getMessage());
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
            }

            @Override
            public void onFailure(Call<Listjudul_response> call, Throwable t) {
                showErrorMessage("Oops..", "Gagal Terhubung Keserver");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void loadPage(int i) {
        mSwipeRefreshLayout.setRefreshing(true);
        final String judulSkripsi = et_judul.getText().toString();
        mInterfaceListJudul.getListJudul(judulSkripsi, i).enqueue(new Callback<Listjudul_response>() {
            public void onResponse(Call<Listjudul_response> call, Response<Listjudul_response> response) {
                Listjudul_response listjudul_response = (Listjudul_response) response.body();
                if (!listjudul_response.isError()) {
                    mAdapterListJudul.addDataJudul(((Listjudul_response) response.body()).getListjudul());
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            public void onFailure(Call<Listjudul_response> call, Throwable th) {
                showErrorMessage("Oops..", "Gagal Terhubung Keserver");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void showErrorMessage(String str, String str2) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorTitle.setText(str);
        errorMessage.setText(str2);
        errorRetry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getData();
            }
        });
    }


    //SCAN JUDUL
    private void tampilkanMasukkanGambarDialog(){
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Pilih Gambar");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickCamera();
                    }
                }
                if(i == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show();
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "GambarBaru");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Gambar Menjadi Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }else{
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickGallery();
                    }else{
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                iv_gambar.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) iv_gambar.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                    }
                    et_judul.setText(sb.toString());

                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
