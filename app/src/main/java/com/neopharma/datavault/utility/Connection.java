package com.neopharma.datavault.utility;

import androidx.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class Connection extends LiveData<Boolean> {

    private Context context;
    private NetworkChangeReceiver receiver;

    public Connection(Context context) {
        this.context = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(NetworkChangeReceiver.CONNECTIVITY_CHANGE);
        receiver = new NetworkChangeReceiver();
        context.registerReceiver(receiver, filter);
        postValue();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        context.unregisterReceiver(receiver);
    }

    private boolean isNetworkAvailable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();

        return info != null && info.isConnectedOrConnecting();
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        public static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

        @Override
        public void onReceive(final Context context, final Intent intent) {
            postValue();
        }
    }

    private void postValue() {
        postValue(isNetworkAvailable(context));
    }
}