package com.android.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Class which will ensure single instance of class in whole application
 *
 * 1. Class maintains the network connection status. 
 * 2. Broadcast Receiver is used to receive network updates
 * 3. It uses the default server to check the connectivity and updates the status accordingly
 */
public class NetworkStateManager {

    public static final int CONNECTIVITY_TYPE_NONE = -1;

    public static final int NOT_CONNECTED = 0;
    public static final int CONNECTED_TO_NETWORK = 1;

    private int mNetworkType = CONNECTIVITY_TYPE_NONE;
    private AtomicInteger sNetworkState = new AtomicInteger(NOT_CONNECTED);

    private ConnectivityReceiver mConnectivityReceiver = null;
    private Context mContext = null;

    private static final String MOD_NETWORK = "Network Info";
    private static final String DEFAULT_URL = "http://www.google.com";

    /**
     * private constructor, singleton class
     */

    private NetworkStateManager() {
        //private
    }

    /**
     * Maintains only one instance of NetworkStateManager Class
     */

    private static class SingletonHolder {
        public static final NetworkStateManager INSTANCE = new NetworkStateManager();
    }

    /**
     * @return the instance of class
     */

    public static NetworkStateManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * This method registers the broadcast receiver, must be called once
     *
     * @param context
     */

    public void initialize(Context context) {

        if(mConnectivityReceiver == null) {

            mConnectivityReceiver = new ConnectivityReceiver();
        }

        if(mContext == null){
            mContext = context;
        }

        setNetworkState(NOT_CONNECTED);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mConnectivityReceiver, filter);

        // Initialize current network type
        onConnectivityChanged(context, null);
    }

    public void unRegister() {

        try {
            if(mConnectivityReceiver != null) {

                mContext.unregisterReceiver(mConnectivityReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Receiver will be triggered on any change in network state
     */

    public class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i(MOD_NETWORK, "broadcast received");

            ConnectivityManager connectivityManager = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            onConnectivityChanged(context, networkInfo);
        }
    }

    /**
     * Updates the connection state, acquires lock
     *
     * @param context
     * @param networkInfo
     */

    private synchronized void onConnectivityChanged(Context context, NetworkInfo networkInfo) {

        Log.i(MOD_NETWORK, "Connectivity changed");

        int networkType = (networkInfo != null && networkInfo.isConnected()) ? networkInfo.getType()
                : CONNECTIVITY_TYPE_NONE;

        Log.d(MOD_NETWORK, "current Network Type : " + networkType + " previousType : " + mNetworkType);

        // Ignore the event if the current active network is not
        // changed.
        if (mNetworkType == networkType) {

            Log.d(MOD_NETWORK, "Ignoring broadcast, No change in network");
            return;
        }

        Log.i(MOD_NETWORK, "Updating network state.... to Type -> " + networkType);

        mNetworkType = networkType;

        if (mNetworkType != CONNECTIVITY_TYPE_NONE) {

            //check if we can access internet
            new Thread(new ConnectionTask()).start();
        }
        else {

            Log.d(MOD_NETWORK, "Not connected to any network");

            setNetworkState(NOT_CONNECTED);
            broadcastNetworkState(getNetworkState());  //TODO: can be enabled to notify users (stub)
        }
    }

    /**
     *
     * Makes sure if we have IP connectivity
     * It uses the default server.
     */

    private class ConnectionTask implements Runnable {

        @Override
        public void run() {

            if (hasIpConnectivity() == true) {

                Log.i(MOD_NETWORK, "\tIP connectivity found, updating network state to connected");

                setNetworkState(CONNECTED_TO_NETWORK);
                broadcastNetworkState(getNetworkState()); //TODO: can be enabled to notify users (stub)
            }
        }
    }

    private boolean hasIpConnectivity() {

        Log.i(MOD_NETWORK, "Checking for IP connectivitiy");

        try {

            if (mNetworkType == CONNECTIVITY_TYPE_NONE) {

                Log.i(MOD_NETWORK, "\tNo IP connectivity");

                return false;
            }

            // make a URL to a known source
            URL url = new URL(DEFAULT_URL );

            // open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.connect();
            urlConnect.disconnect();

            Log.i(MOD_NETWORK, "\tconnected to network");

            return true;

        } catch (ConnectException e) {

            Log.e(MOD_NETWORK, "ConnectException : " + e.getMessage());
            return false;

        } catch (UnknownHostException e) {

            Log.e(MOD_NETWORK, "UnknownHostException : " + e.getMessage());
            return false;

        } catch (IOException e) {

            Log.e(MOD_NETWORK, "IOException : " + e.getMessage());
            return false;
        }
    }

    public boolean isWifiConnected() {
        return mNetworkType == ConnectivityManager.TYPE_WIFI;
    }

    public boolean isNetworkAvailable() {

        if (sNetworkState.get() == (CONNECTED_TO_NETWORK)) {

            return true;
        }
        else {
            return false;
        }
    }

    private void setNetworkState(int state) {
        sNetworkState.set(state);
    }

    public int getNetworkState() {
        return sNetworkState.get();
    }

    public void reset(boolean closeApp) {

        mNetworkType = CONNECTIVITY_TYPE_NONE;

        if (closeApp) {

            setNetworkState(NOT_CONNECTED);
            mContext.unregisterReceiver(mConnectivityReceiver);
        }
    }

    private void broadcastNetworkState(int state) {
       /* Log.d(MOD_NETWORK, "Sending Broadcast for Network state change.");
        Intent intent = new Intent(NETWORK_STATE_CONSTANTS.NETWORK_STATE_CHANGE_INTENT);
        intent.putExtra(NETWORK_STATE_CONSTANTS.NETWORK_STATE, state);
        LocalBroadcastManager.getInstance(AipApplication.sContext).sendBroadcast(intent);*/
    }
}
