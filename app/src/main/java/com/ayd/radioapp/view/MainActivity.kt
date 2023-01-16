package com.ayd.radioapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ayd.radioapp.viewmodel.MainViewModel
import com.ayd.radioapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory(application))[MainViewModel::class.java]

        viewModel.urls.observe(this, Observer{ urls ->
            //update ui with url list
        })

        viewModel.toastMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        binding.startButton.setOnClickListener {
            viewModel.playAudio()
        }
        binding.pauseButton.setOnClickListener {
            viewModel.pauseAudio()
        }

        binding.nextButton.setOnClickListener {
            viewModel.nextAudio()
        }
        binding.backButton.setOnClickListener {
            viewModel.backAudio()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

}