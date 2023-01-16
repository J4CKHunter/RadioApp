package com.ayd.radioapp

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.remotecallback.BroadcastReceiverWithCallbacks
import com.ayd.radioapp.viewmodel.MainViewModel


open class NotificationReceiver(private val mainViewModel: MainViewModel) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

/*        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val notificationView = layoutInflater.inflate(R.layout.custom_notification_layout, null)
        val button1 = notificationView.findViewById<Button>(R.id.radioOpeningSwitch)*/

        if (intent.action == "notification_clicked") {

/*            val toast = Toast.makeText(context,"You clicked on notification button", Toast.LENGTH_SHORT)
            toast.show()*/

        if(intent.getStringExtra("type").equals("openClose"))
            if(MainViewModel.isOpen){
                mainViewModel.pauseAudio()
                //button1.setBackgroundColor(Color.WHITE)
            }else{
                mainViewModel.playAudio()
            }
        }
        if(intent.getStringExtra("type").equals("nextChannel")){
            if(MainViewModel.isOpen){
                mainViewModel.nextAudio()
            }
        }
        if(intent.getStringExtra("type").equals("previousChannel")){
            if(MainViewModel.isOpen){
                mainViewModel.backAudio()
            }
        }
        if(intent.getStringExtra("type").equals("volumeUp")){
            mainViewModel.volumeUp()
        }
        if(intent.getStringExtra("type").equals("volumeDown")){
            mainViewModel.volumeDown()
        }


    }


}