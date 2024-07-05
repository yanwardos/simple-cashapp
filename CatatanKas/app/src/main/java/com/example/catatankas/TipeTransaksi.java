package com.example.catatankas;

public enum TipeTransaksi {
    PEMASUKAN(1),
    PENGELUARAN(2),
    UNCATEGORIZED(0);

    private int tipe;

    TipeTransaksi(int tipe){
        this.tipe = tipe;
    }

    public int getTipe(){
        return tipe;
    }
}
