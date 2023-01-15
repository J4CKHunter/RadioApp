package com.ayd.radioapp

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import androidx.remotecallback.BroadcastReceiverWithCallbacks
import com.ayd.radioapp.viewmodel.MainViewModel


open class NotificationReceiver(private val mainViewModel: MainViewModel) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "notification_clicked") {


            val toast = Toast.makeText(context,"You clicked on notification button", Toast.LENGTH_SHORT)
            toast.show()

            Log.d("logza",intent.getStringExtra("type").toString())

        if(intent.getStringExtra("type").equals("openClose"))
            mainViewModel.playAudio()
        }
        if(intent.getStringExtra("type").equals("nextChannel")){
            mainViewModel.nextAudio()
        }
        if(intent.getStringExtra("type").equals("previousChannel")){
            mainViewModel.backAudio()
        }
        if(intent.getStringExtra("type").equals("volumeUp")){
            mainViewModel.volumeUp()
        }
        if(intent.getStringExtra("type").equals("volumeDown")){
            mainViewModel.volumeDown()
        }



    }


}