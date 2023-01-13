package com.ayd.radioapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ayd.radioapp.databinding.ActivityMainBinding
import com.ayd.radioapp.model.Radio
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var player: SimpleExoPlayer
    private lateinit var defaultHttpDataSourceFactory: DefaultHttpDataSourceFactory

    private var currentIndex = 0
    private val urls = mutableListOf<Radio>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val database = FirebaseDatabase.getInstance("https://radioapp-279ba-default-rtdb.firebaseio.com")
        val myRef = database.reference


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {

                    val link = snapshot.child("link").getValue(String::class.java).toString()
                    val type = snapshot.child("type").getValue(String::class.java).toString()
                    val name = snapshot.child("name").getValue(String::class.java).toString()

                    val radio = Radio(name, type, link)
                    urls.add(radio)

                }
                // do something with the items list
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })


        player = ExoPlayerFactory.newSimpleInstance(this)
        defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory()

        binding.startButton.setOnClickListener {
            playAudio()
        }
        binding.pauseButton.setOnClickListener {
            pauseAudio()
        }

        binding.nextButton.setOnClickListener {
            nextAudio()
        }
        binding.backButton.setOnClickListener {
            backAudio()
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
        if(urls.isNotEmpty()){
            val mediaItem = MediaItem.fromUri(urls[currentIndex].link!!)
            if(urls[currentIndex].type=="m3u8"){
                val mediaSource = HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem) //HlsMediaSource -> ProgressiveMediaSource
                player.prepare(mediaSource)
            }else{ //link and mp3
                val mediaSource = ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem) //HlsMediaSource -> ProgressiveMediaSource
                player.prepare(mediaSource)
            }
            player.playWhenReady = true
            Toast.makeText(this,urls[currentIndex].name,Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "list is null!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun pauseAudio() {
        player.playWhenReady = false
        Toast.makeText(this,"stopped.",Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}