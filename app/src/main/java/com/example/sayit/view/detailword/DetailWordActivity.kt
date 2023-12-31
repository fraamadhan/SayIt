package com.example.sayit.view.detailword

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sayit.R
import com.example.sayit.databinding.ActivityDetailWordBinding
import com.example.sayit.repository.Result
import com.example.sayit.utils.WORD_ID
import com.example.sayit.view.MainActivity
import com.example.sayit.view.ViewModelFactory
import java.io.File
import java.io.IOException

class DetailWordActivity : AppCompatActivity() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var state: Boolean = false

    private lateinit var sp: SoundPool
    private var correctSoundId: Int = 0
    private var wrongSoundId: Int = 0
    private var spLoaded = false

    private var wordToGrade = "foo"

    private val viewModel by viewModels<DetailWordViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailWordBinding

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wordId = intent.getIntExtra(WORD_ID, 0)
        setUpSoundPool()
        correctSoundId = sp.load(this, R.raw.tururur, 1)
        wrongSoundId = sp.load(this, R.raw.loli_oh_no, 1)

        binding.btnRecord.setOnTouchListener { view, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    startRecording()
                }
                MotionEvent.ACTION_UP -> {
                    stopRecording()
                    val audioFile = File(output)
                    showLoading(true)
                    viewModel.grading(wordToGrade, audioFile).observe(this) { result ->
                        when (result) {
                            is Result.Success -> {
                                showLoading(false)

                                val accuracy = result.data.gradeResult.accuracy
                                Log.d("ACCURACY YES", accuracy.toString())
                                if (accuracy != null) {
                                    if (accuracy >= 80) {
                                        correctGrading()
                                    } else {
                                        wrongGrading()
                                    }
                                }
                            }

                            is Result.Error -> {
                                showLoading(false)
                                Log.d("ERROR EUY", result.error)
                                showToast(result.error)
                            }

                            is Result.Loading -> {
                                showLoading(true)
                            }
                        }
                    }
                }
                else -> {
                }
            }
            return@setOnTouchListener view?.performClick() ?: true
        }

        binding.ivAudio.setOnClickListener {
            playRecording()
        }

        binding.btnBackToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnTryAgain.setOnClickListener {
            resetPage()
        }

        showLoading(true)
        viewModel.getUserToken().observe(this) {token ->
            viewModel.getDetailWord(token.toString(), wordId).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        binding.wordItem.text = result.data.word
                        wordToGrade = result.data.word.toString()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
        requestPermissions()

    }

    private fun resetPage() {
        binding.backgroundWrongState.visibility = View.GONE
        binding.messageWrongState.visibility = View.GONE
        binding.btnRecord.visibility = View.VISIBLE
        binding.btnTryAgain.visibility = View.GONE
    }

    private fun correctGrading() {
        binding.backgroundCorrectState.visibility = View.VISIBLE
        binding.ivAudio.visibility = View.VISIBLE
        binding.btnBackToHome.visibility = View.VISIBLE
        binding.messageCorrectState.visibility = View.VISIBLE
        binding.btnRecord.visibility = View.GONE

        sp.play(correctSoundId, 1f, 1f, 1, 1, 1f)
    }

    private fun wrongGrading() {
        binding.backgroundWrongState.visibility = View.VISIBLE
        binding.messageWrongState.visibility = View.VISIBLE
        binding.btnRecord.visibility = View.GONE
        binding.btnTryAgain.visibility = View.VISIBLE

        sp.play(wrongSoundId, 1f, 1f, 1, 1, 1f)
    }

    private fun setUpSoundPool() {
        sp = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        sp.setOnLoadCompleteListener {_, _, status ->
            if (status == 0) {
                spLoaded = true
            } else {
                showToast("Failed to load sound")
            }
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder = MediaRecorder()
            output = getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath + "/id_wordYes.mp3"

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setAudioSamplingRate(16000)
            output?.let { mediaRecorder?.setOutputFile(it) }

            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            showToast("Recording started ...")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Recording failed: ${e.message}")
        }
    }

    private fun stopRecording() {
        if (state) {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
            showToast("Recording stopped")
        }
        Log.d("INI PATH", File(output).toString())
    }

    private fun playRecording() {
        mediaPlayer = MediaPlayer()
        try {
            output?.let {
                mediaPlayer?.setDataSource(it)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                showToast("Recording playing")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this@DetailWordActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(this, permissions, 0)
    }
}
