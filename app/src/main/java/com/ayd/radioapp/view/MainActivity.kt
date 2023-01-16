package com.ayd.radioapp.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ayd.radioapp.NotificationReceiver
import com.ayd.radioapp.R
import com.ayd.radioapp.databinding.ActivityMainBinding
import com.ayd.radioapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var notificationManager: NotificationManagerCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.orangeButton.visibility = View.VISIBLE
        binding.redButton.visibility = View.INVISIBLE

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory(application))[MainViewModel::class.java]

        viewModel.urls.observe(this, Observer{ urls ->
            //update ui with url list
        })

        viewModel.toastMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        binding.orangeButton.setOnClickListener {
                viewModel.playAudio()
                binding.orangeButton.visibility = View.INVISIBLE
                binding.redButton.visibility = View.VISIBLE
        }
        binding.redButton.setOnClickListener {
                viewModel.pauseAudio()
                binding.orangeButton.visibility = View.VISIBLE
                binding.redButton.visibility = View.INVISIBLE

        }

        binding.nextButton.setOnClickListener {
            if(MainViewModel.isOpen){
                viewModel.nextAudio()
            }
        }
        binding.backButton.setOnClickListener {
            if(MainViewModel.isOpen){
                viewModel.backAudio()
            }
        }

        binding.voiceUpButton.setOnClickListener {
            viewModel.volumeUp()
        }
        binding.voiceDownButton.setOnClickListener {
            viewModel.volumeDown()
        }


        val filter = IntentFilter("notification_clicked")
        registerReceiver(NotificationReceiver(viewModel), filter)


        notificationManager = NotificationManagerCompat.from(this)
        createNotification()


    }


    private fun createNotification() {

        val openCloseButtonIntent = Intent("notification_clicked")
        openCloseButtonIntent.putExtra("type","openClose")

        val nextChannelIndent = Intent("notification_clicked")
        nextChannelIndent.putExtra("type","nextChannel")

        val previousChannelIntent = Intent("notification_clicked")
        previousChannelIntent.putExtra("type","previousChannel")

        val volumeUpIntent = Intent("notification_clicked")
        volumeUpIntent.putExtra("type","volumeUp")

        val volumeDownIntent = Intent("notification_clicked")
        volumeDownIntent.putExtra("type","volumeDown")


        val openCloseButtonPendingIntent = PendingIntent.getBroadcast(this, 0, openCloseButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val nextChannelPendingIntent = PendingIntent.getBroadcast(this, 1, nextChannelIndent, PendingIntent.FLAG_UPDATE_CURRENT)
        val previousChannelPendingIntent = PendingIntent.getBroadcast(this, 2, previousChannelIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val volumeUpPendingIntent = PendingIntent.getBroadcast(this, 3, volumeUpIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val volumeDownPendingIntent = PendingIntent.getBroadcast(this, 4, volumeDownIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        // get back to another activity from notification by clicking on it
//        val testIntent = Intent(applicationContext, MainActivity::class.java)
//        val testPendingIntent = PendingIntent.getActivity(this, 1, testIntent, PendingIntent.FLAG_ONE_SHOT)

        val notificationView = RemoteViews(packageName,R.layout.custom_notification_layout)
        val notificationView2 = RemoteViews(packageName,R.layout.custom_notification_layout2)
        //val notificationView3 = RemoteViews(packageName,R.layout.custom_notification_layout3)
        //val notificationView4 = RemoteViews(packageName,R.layout.custom_notification_layout)
        //val notificationView5 = RemoteViews(packageName,R.layout.custom_notification_layout)


        notificationView.setOnClickPendingIntent(R.id.radioOpeningSwitch,openCloseButtonPendingIntent)
        notificationView.setOnClickPendingIntent(R.id.voice_increase,volumeUpPendingIntent)
        notificationView.setOnClickPendingIntent(R.id.voice_decrease,volumeDownPendingIntent)
        notificationView2.setOnClickPendingIntent(R.id.channel_next,nextChannelPendingIntent)
        notificationView2.setOnClickPendingIntent(R.id.channel_previous,previousChannelPendingIntent)





     /*       notificationView.setOnClickPendingIntent(R.id.channel_previous,previousChannelPendingIntent)
            notificationView.setOnClickPendingIntent(R.id.voice_increase,volumeUpPendingIntent)
            notificationView.setOnClickPendingIntent(R.id.voice_decrease,volumeDownPendingIntent)*/

        //val intentx = Intent(this, FullscreenNotificationActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(applicationContext, 30, intentx, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setContentTitle("")
//            .setContentText("")
//            .setContentIntent(testPendingIntent)
              .setCustomContentView(notificationView)
 //           .setCustomContentView(notificationView2)
             // .setCustomBigContentView(notificationView)
 //           .setFullScreenIntent(pendingIntent, true)
            //.addAction(R.drawable.ic_launcher_background, "Button", pendingIntent)
//            .addAction(R.id.radioOpeningSwitch,"1", openCloseButtonPendingIntent)
//            .addAction(R.id.channel_next,"2", nextChannelPendingIntent)
//            .addAction(R.id.channel_previous,"3", previousChannelPendingIntent)
//            .addAction(R.id.voice_increase,"4", volumeUpPendingIntent)
//            .addAction(R.id.voice_decrease,"5", volumeDownPendingIntent)
//            .addInvisibleAction(R.id.radioOpeningSwitch,"", openCloseButtonPendingIntent)
//            .setAllowSystemGeneratedContextualActions(true)
            .setOngoing(true)
            .setAutoCancel(true)


        val builder2 = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setCustomContentView(notificationView2)
            .setOngoing(true)
            .setAutoCancel(true)


/*        val builder3 = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setCustomContentView(notificationView3)
            .setOngoing(true)
            .setAutoCancel(true)*/




/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("0", "My channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId("0")
        }*/

        notificationManager.notify(0, builder.build())
        Thread.sleep(250)
        notificationManager.notify(1,builder2.build())
 /*       Thread.sleep(250)
        notificationManager.notify(2,builder3.build())*/

    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
        unregisterReceiver(NotificationReceiver(viewModel))
    }


}