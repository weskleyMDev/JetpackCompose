package com.weskley.hdc_app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {
//    @Inject
//    lateinit var viewModel: AlarmViewModel

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
//                TODO
            }
        } catch (e: Exception) {
            Log.e("BootReceiver", "Error occurred while creating notification", e)
        }
    }
}