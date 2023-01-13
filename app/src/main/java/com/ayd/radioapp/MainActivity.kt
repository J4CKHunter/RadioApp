package com.ayd.radioapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var nextSong: Button
    private lateinit var backSong: Button

    private val NOTIFICATION_ID = 1
    private lateinit var notificationManager: NotificationManager

    private lateinit var player: SimpleExoPlayer
    private lateinit var defaultHttpDataSourceFactory: DefaultHttpDataSourceFactory

    private var currentIndex = 0
    private val urls = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlay = findViewById(R.id.startButton)
        btnPause = findViewById(R.id.pauseButton)
        nextSong = findViewById(R.id.nextButton)
        backSong = findViewById(R.id.backButton)


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        val database = FirebaseDatabase.getInstance("https://radioapp-279ba-default-rtdb.firebaseio.com")
        val myRef = database.reference


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val item = snapshot.getValue(String::class.java).toString()
                    urls.add(item)
                }
                // do something with the items list
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })


        player = ExoPlayerFactory.newSimpleInstance(this)
        defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory()

        btnPlay.setOnClickListener {
            playAudio()
        }
        btnPause.setOnClickListener {
            pauseAudio()
        }

        nextSong.setOnClickListener {
            nextAudio()
        }
        backSong.setOnClickListener {
            backAudio()
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun nextAudio() {
        currentIndex = (currentIndex + 1) % urls.size
        playAudio()
    }

    private fun backAudio() {
        currentIndex = (currentIndex - 1 + urls.size) % urls.size
        playAudio()
    }


    private fun playAudio() {
        val mediaItem = MediaItem.fromUri(urls[currentIndex])
        val mediaSource = HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem) //HlsMediaSource -> ProgressiveMediaSource
        player.prepare(mediaSource)
        player.playWhenReady = true
        Toast.makeText(this,"Çalışıyor",Toast.LENGTH_SHORT).show()

        val fullScreenIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("ACTION", "PAUSE")
        }
        val pausePendingIntent = PendingIntent.getActivity(this, 0, pauseIntent, 0)

        val builder = NotificationCompat.Builder(this, "channelId")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Playing")
            .setAutoCancel(false)
            .setOngoing(true)
            .setFullScreenIntent(pendingIntent, true)
            .addAction(R.drawable.ic_launcher_foreground, "Pause", pausePendingIntent)
        notificationManager.notify(NOTIFICATION_ID, builder.build())


    }


    private fun pauseAudio() {
        player.playWhenReady = false
        Toast.makeText(this,"Durdu",Toast.LENGTH_SHORT).show()

        val fullScreenIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("ACTION", "START")
        }
        val pausePendingIntent = PendingIntent.getActivity(this, 0, pauseIntent, 0)

        val builder = NotificationCompat.Builder(this, "channelId")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Paused")
            .setAutoCancel(false)
            .setOngoing(true)
            .setFullScreenIntent(pendingIntent, true)
            .addAction(R.drawable.ic_launcher_foreground, "Start", pausePendingIntent)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.getStringExtra("ACTION") == "PAUSE") {
            //pauseAudio()
        } else if (intent?.getStringExtra("ACTION") == "PLAY") {
            playAudio()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}