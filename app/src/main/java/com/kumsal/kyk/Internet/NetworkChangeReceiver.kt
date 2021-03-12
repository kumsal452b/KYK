package com.kumsal.kyk.Internet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.kumsal.kyk.MainActivity.Companion.dialog
import java.lang.NullPointerException

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        try
        {
            if (isOnline(context!!)) {
                dialog(true);
                Log.e("keshav", "Online Connect Intenet ");

            } else {
                dialog(false);
                Log.e("keshav", "Conectivity Failure !!! ");
            }
        } catch (e:NullPointerException) {
            e.printStackTrace();
        }
    }
    private fun isOnline(context: Context):Boolean {
        try {
        var cm=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo=cm.activeNetwork as NetworkInfo
         return (networkInfo != null && networkInfo.isConnected)
        }catch (e:NullPointerException){
            return false
        }

    }
}