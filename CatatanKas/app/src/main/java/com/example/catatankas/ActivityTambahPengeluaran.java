package com.example.catatankas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.catatankas.databinding.ActivityTambahPengeluaranBinding;

public class ActivityTambahPengeluaran extends AppCompatActivity {

    private ActivityTambahPengeluaranBinding binding;
    private PreferenceHelper preferenceHelper;
    private DataSourceApi dataSourceApi;
    private boolean isCallOnProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTambahPengeluaranBinding.inflate(getLayoutInflater());
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
                if(isCallOnProgress) return;

                isCallOnProgress = true;
                setInputEnabled(false);

                dataSourceApi.addTransaksi(new TransaksiModel(
                        binding.etPengeluaranTitle.getText().toString(),
                        binding.etPengeluaranDescription.getText().toString(),
                        Integer.parseUnsignedInt(
                                binding.etPengeluaranAmount.getText().toString()
                        ),
                        System.currentTimeMillis(),
                        TransaksiModel.CATEGORY_PENGELUARAN
                ), new DataSourceApi.AddTransaksiCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ActivityTambahPengeluaran.this, "Berhasil menyimpan data", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(ActivityTambahPengeluaran.this, ActivityMain.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(ActivityTambahPengeluaran.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(ActivityTambahPengeluaran.this, ActivityMain.class));
                        finish();
                    }
                });
            }
        });
    }


    private void setInputEnabled(boolean state){
        binding.btnSave.setEnabled(state);
        binding.etPengeluaranTitle.setEnabled(state);
        binding.etPengeluaranDescription.setEnabled(state);
        binding.etPengeluaranAmount.setEnabled(state);
        binding.progressBar.setVisibility(state?View.GONE:View.VISIBLE);
        binding.progressText.setVisibility(state?View.GONE:View.VISIBLE);
    }

    private boolean checkAllFields(){
        if(binding.etPengeluaranTitle.getText().length()==0){
            binding.etPengeluaranTitle.setError(getString(R.string.error_field_empty));
            binding.etPengeluaranTitle.requestFocus();
            return false;
        }
        if(binding.etPengeluaranAmount.getText().length()==0){
            binding.etPengeluaranAmount.setError(getString(R.string.error_field_empty));
            binding.etPengeluaranAmount.requestFocus();
            return false;
        }
        if(binding.etPengeluaranDescription.getText().length()==0){
            binding.etPengeluaranDescription.setError(getString(R.string.error_field_empty));
            binding.etPengeluaranDescription.requestFocus();
            return false;
        }
        return true;
    }
}