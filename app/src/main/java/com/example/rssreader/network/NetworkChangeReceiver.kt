package com.example.rssreader.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.example.rssreader.R


class NetworkChangeReceiver(val isOnline: ImageView?=null) : BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        var status = NetworkUtil.getConnectivityStatus(context)
        if(status == NetworkUtil.TYPE_NOT_CONNECTED) {
            isOnline?.setImageResource(R.drawable.ic_offline)
        }
        else {
            isOnline?.setImageResource(R.drawable.ic_online)
        }
    }

}