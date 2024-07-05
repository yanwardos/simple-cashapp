package com.example.catatankas;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kotlin.contracts.Returns;

public class TransaksiModel{
    public static final int CATEGORY_NONE = 0;
    public static final int CATEGORY_PEMASUKAN = 1;
    public static final int CATEGORY_PENGELUARAN = 2;
    private long time;
    private String title;
    private String description;
    private long amount;
    private int tipeTransaksi;

    public TransaksiModel(String title, String description, long amount, long time, int tipeTransaksi) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.time = time;
        this.tipeTransaksi = tipeTransaksi;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public int getTipeTransaksi() {
        return tipeTransaksi;
    }


    public long getTime() {
        return time;
    }

    public String getFormatAmount(){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("ID", "ID"));
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

    public String getFormatDate(){
        SimpleDateFormat formatter
                = new SimpleDateFormat ("dd/MM/yyyy", new Locale("ID", "ID"));
        return formatter.format(new Date(this.time));
    }

}
