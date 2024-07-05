package com.example.catatankas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.catatankas.databinding.ActivityTambahPemasukanBinding;

public class ActivityTambahPemasukan extends AppCompatActivity {
    public static final String TAG = ActivityTambahPemasukan.class.getSimpleName();
    private ActivityTambahPemasukanBinding binding;
    private PreferenceHelper preferenceHelper;
    private DataSourceApi dataSourceApi;
    private boolean isCallOnProgress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTambahPemasukanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferenceHelper = new PreferenceHelper(getApplicationContext());
        dataSourceApi = new DataSourceApi(getApplicationContext());

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkAllFields()) return;

                if(isCallOnProgress) return;;
                isCallOnProgress = true;
                setInputEnabled(false);
                dataSourceApi.addTransaksi(new TransaksiModel(
                        binding.etPemasukanTitle.getText().toString(),
                        binding.etPemasukanDescription.getText().toString(),
                        Integer.parseUnsignedInt(
                                binding.etPemasukanAmount.getText().toString()
                        ),
                        System.currentTimeMillis(),
                        TransaksiModel.CATEGORY_PEMASUKAN
                ), new DataSourceApi.AddTransaksiCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ActivityTambahPemasukan.this, "Berhasil menyimpan data", Toast.LENGTH_SHORT).show();
    //                    startActivity(new Intent(ActivityTambahPemasukan.this, ActivityMain.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        setInputEnabled(true);
                        Toast.makeText(ActivityTambahPemasukan.this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: ", t);
                    }
                });

            }
        });
    }

    private void setInputEnabled(boolean state){
        binding.btnSave.setEnabled(state);
        binding.etPemasukanTitle.setEnabled(state);
        binding.etPemasukanDescription.setEnabled(state);
        binding.etPemasukanAmount.setEnabled(state);
        binding.progressBar.setVisibility(state?View.GONE:View.VISIBLE);
        binding.progressText.setVisibility(state?View.GONE:View.VISIBLE);
    }

    private boolean checkAllFields(){
        if(binding.etPemasukanTitle.getText().length()==0){
            binding.etPemasukanTitle.setError(getString(R.string.error_field_empty));
            binding.etPemasukanTitle.requestFocus();
            return false;
        }
        if(binding.etPemasukanAmount.getText().length()==0){
            binding.etPemasukanAmount.setError(getString(R.string.error_field_empty));
            binding.etPemasukanAmount.requestFocus();
            return false;
        }
        if(binding.etPemasukanDescription.getText().length()==0){
            binding.etPemasukanDescription.setError(getString(R.string.error_field_empty));
            binding.etPemasukanDescription.requestFocus();
            return false;
        }
        return true;
    }
}