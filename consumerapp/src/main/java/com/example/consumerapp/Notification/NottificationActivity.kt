package com.example.consumerapp.Notification

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ActivityNottificationBinding

class NottificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNottificationBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var SharedPreferences: SharedPreferences

    companion object {
        const val PREFERENSES = "SettingPref"
        private const val DAILY = "daily"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nottification)
        binding = ActivityNottificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Setting"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        alarmReceiver = AlarmReceiver()
        SharedPreferences = getSharedPreferences(PREFERENSES, Context.MODE_PRIVATE)

        binding.DailySetting.isChecked = SharedPreferences.getBoolean(DAILY, false)
        binding.DailySetting.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setRepeatingAlarm(
                    this,
                    AlarmReceiver.TYPE_REPEATING,
                    getString(R.string.daily_reminder)
                )
            } else {
                alarmReceiver.cancelAlarm(this)
            }
            saveChange(isChecked)
        }
    }

    private fun saveChange(value: Boolean) {
        val editor = SharedPreferences.edit()
        editor.putBoolean(DAILY, value)
        editor.apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}