package com.example.INTIFoodie;

import android.os.Parcelable;

public interface WalletUpdateListener extends Parcelable {
    void onWalletUpdated(long newWalletAmount);
}
