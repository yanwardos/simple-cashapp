package com.example.catatankas;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PreferenceHelper {

    public static final String TAG = "PreferenceManager";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Gson gson;

    public PreferenceHelper(Context context) {
        this.context = context;
        sharedPreferences =
                context.getSharedPreferences(
                        context.getString(R.string.KEY_PREFERENCE),
                        Context.MODE_PRIVATE
                );
        editor = sharedPreferences.edit();

        gson = new Gson();
    }

    public List<TransaksiModel> getTransaksis(){
        String jsonData = "";
        try {
            jsonData =
                    this.sharedPreferences.getString(
                            context.getString(R.string.KEY_TRANSAKSI),
                            ""
                    );
        }catch (Throwable throwable){
            throwable.printStackTrace();
            return null;
        }

        try {
            TransactionsOnPreference transactionsOnPreference = null;
            transactionsOnPreference
                    = this.gson.fromJson(
                            jsonData,
                            TransactionsOnPreference.class
            );

            if(transactionsOnPreference==null) return null;

            return  transactionsOnPreference.getTransaksiModelList();
        }catch (Throwable throwable){
            throwable.printStackTrace();
            return null;
        }
    }

    public boolean addTransaksi(TransaksiModel transaksiModel){
        List<TransaksiModel> transaksiList = this.getTransaksis();
        if(transaksiList==null)
            transaksiList = new ArrayList<>();

        transaksiList.add(0, transaksiModel);

        TransactionsOnPreference transactionsOnPreference = new TransactionsOnPreference(transaksiList);

        String transactionData = "";
        try{
            transactionData =
                this.gson.toJson(transactionsOnPreference);
        }catch (Throwable throwable){
            throwable.printStackTrace();
            return false;
        }

        try{
            this.editor.putString(
                    context.getString(R.string.KEY_TRANSAKSI),
                    transactionData
            );

            return this.editor.commit();
        }catch (Throwable throwable){
            throwable.printStackTrace();
            return false;
        }
    }

    public boolean clearTransaksi(){
        List<TransaksiModel> emptyTransList = new ArrayList<>();
        TransactionsOnPreference emptyTransPrefer =
                new TransactionsOnPreference(emptyTransList);

        try{

            this.editor.putString(
                    context.getString(R.string.KEY_TRANSAKSI),
                    this.gson.toJson(emptyTransPrefer)
            );

            return this.editor.commit();
        }catch (Throwable throwable){
            throwable.printStackTrace();
            return false;
        }
    }
    public boolean isInitiated(){
        return this.sharedPreferences.contains(context.getString(R.string.KEY_TRANSAKSI));
    }

    public class TransactionsOnPreference {
        private List<TransaksiModel> transaksiModelList;

        public TransactionsOnPreference(List<TransaksiModel> transaksiModelList) {
            this.transaksiModelList = transaksiModelList;
        }

        public List<TransaksiModel> getTransaksiModelList() {
            return transaksiModelList;
        }

        public void setTransaksiModelList(List<TransaksiModel> transaksiModelList) {
            this.transaksiModelList = transaksiModelList;
        }
    }
}
