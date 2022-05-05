package com.citroncode.stundenplan

import android.R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.citroncode.stundenplan.database.DatabaseHelper
import com.citroncode.stundenplan.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private lateinit var databaseHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        ini();

        binding.swGrids.isChecked = sharedPreferences.getBoolean("show_grids",false)

        binding.swGrids.setOnCheckedChangeListener { _, b ->
            preferencesEditor.putBoolean("show_grids", b)
            preferencesEditor.apply()
        }
        binding.btnClear.setOnClickListener{
            databaseHelper.clearTimetable()
            startActivity(Intent(this@SettingsActivity,MainActivity::class.java))
            finish()
        }
    }
    private fun ini(){
        sharedPreferences = getSharedPreferences(packageName,0)
        preferencesEditor = sharedPreferences.edit()

        databaseHelper = DatabaseHelper(this)
        databaseHelper.iniDatabase()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}