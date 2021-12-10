package com.umbrella.ermolaevshiftapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umbrella.ermolaevshiftapp.R
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_LANGUAGE = "language"
        private const val DEFAULT_LANGUAGE = "ru"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val language = getPreferences(MODE_PRIVATE)
            .getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE

        val config = resources.configuration
        val locale = Locale(language)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}