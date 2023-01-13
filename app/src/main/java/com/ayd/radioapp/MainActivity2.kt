package com.ayd.radioapp


import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util


class MainActivity2 : AppCompatActivity() {
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val userAgent = Util.getUserAgent(this, "RadioApp")
        val dataSourceFactory = DefaultDataSourceFactory(this, userAgent)
        val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("https://moondigitalmaster.radyotvonline.net/90lar/playlist.m3u8"))

        player = ExoPlayer.Builder(this).build()
        player.setMediaSource(hlsMediaSource)
    }

    override fun onStart() {
        super.onStart()
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}