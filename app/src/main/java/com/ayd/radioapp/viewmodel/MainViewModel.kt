package com.ayd.radioapp.viewmodel

import android.app.Application
import android.content.Context
import android.media.AudioManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ayd.radioapp.model.Radio
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application){

    private var player: SimpleExoPlayer
    private var defaultHttpDataSourceFactory: DefaultHttpDataSourceFactory

    private val audioManager = application.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var currentIndex = 0

    private var mediaSource: MediaSource? = null

    val urls = MutableLiveData<List<Radio>>()
    val toastMessage: MutableLiveData<String> = MutableLiveData()


    companion object{
        var isOpen = false
    }


    init {

        player = ExoPlayerFactory.newSimpleInstance(application.applicationContext)
        defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory()

        viewModelScope.launch {

            val database = FirebaseDatabase.getInstance("https://radio-app-abc8d-default-rtdb.europe-west1.firebasedatabase.app/")
            val myRef = database.reference

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val urls = mutableListOf<Radio>()
                    for (snapshot in dataSnapshot.children) {

                        val link = snapshot.child("link").getValue(String::class.java).toString()
                        val type = snapshot.child("type").getValue(String::class.java).toString()
                        val name = snapshot.child("name").getValue(String::class.java).toString()

                        val radio = Radio(name, type, link)
                        urls.add(radio)

                    }
                    this@MainViewModel.urls.postValue(urls)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        }

    }


    fun nextAudio() {
        currentIndex = (currentIndex + 1) % urls.value!!.size
        playAudio()
    }

    fun backAudio() {
        currentIndex = (currentIndex - 1 + urls.value!!.size) % urls.value!!.size
        playAudio()
    }

    fun playAudio() {
        if(urls.value!!.isNotEmpty()){
            val mediaItem = MediaItem.fromUri(urls.value!![currentIndex].link!!)

            mediaSource = if(urls.value!![currentIndex].type=="m3u8"){
                HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
            }else{
                ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
            }

            mediaSource?.let { mediaSrc ->
                player.prepare(mediaSrc)
            }

            player.playWhenReady = true
            toastMessage.postValue(urls.value!![currentIndex].name)
        }else{
            // show toast for empty list
            toastMessage.postValue("list is null!")
        }
        isOpen = true
    }

    fun pauseAudio() {
        player.playWhenReady = false
        isOpen = false
    }

    fun volumeUp(){
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
    }

    fun volumeDown(){
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
    }

    fun releasePlayer() {
        player.release()
    }

}