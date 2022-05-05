package com.citroncode.stundenplan

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.citroncode.stundenplan.adapter.TimetableAdapter
import com.citroncode.stundenplan.database.DatabaseHelper
import com.citroncode.stundenplan.databinding.ActivityMainBinding
import com.citroncode.stundenplan.databinding.BottomSheetDialogBinding
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.material.bottomsheet.BottomSheetDialog


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter : TimetableAdapter
    private lateinit var databaseHelper : DatabaseHelper
    private lateinit var bottomSheetDialog : BottomSheetDialog
    private lateinit var mBottomSheetBinding : BottomSheetDialogBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var selectedColor : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadTimetable()

        binding.fabEdit.setOnClickListener{
            addTimetable()
        }

    }
    fun loadTimetable(){


        databaseHelper = DatabaseHelper(this)
        databaseHelper.iniDatabase()
        sharedPreferences = getSharedPreferences(packageName,0)

        //TODO Improve this garbage
        if(sharedPreferences.getBoolean("show_grids",false)){
            binding.rlEmpty.foreground = ResourcesCompat.getDrawable(resources,R.drawable.view_line,this.theme)
            binding.rlMonday.foreground = ResourcesCompat.getDrawable(resources,R.drawable.view_line,this.theme)
            binding.rlTuesday.foreground = ResourcesCompat.getDrawable(resources,R.drawable.view_line,this.theme)
            binding.rlWednesday.foreground = ResourcesCompat.getDrawable(resources,R.drawable.view_line,this.theme)
            binding.rlThursday.foreground = ResourcesCompat.getDrawable(resources,R.drawable.view_line,this.theme)
            binding.rlFriday.foreground = ResourcesCompat.getDrawable(resources,R.drawable.view_line,this.theme)
        }else{
            binding.rlEmpty.foreground = null
            binding.rlMonday.foreground = null
            binding.rlTuesday.foreground = null
            binding.rlWednesday.foreground = null
            binding.rlThursday.foreground = null
            binding.rlFriday.foreground = null
        }

        if(databaseHelper.list.size != 0){
            binding.rlNoContent.setVisibility(visible = false)
            binding.rlTopBar.setVisibility(visible = true)

            layoutManager = LinearLayoutManager(this)
            adapter = TimetableAdapter(databaseHelper.list, sharedPreferences.getBoolean("show_grids",false))

            binding.rvTimetable.layoutManager = layoutManager
            binding.rvTimetable.adapter = adapter
        }else{
            binding.rlNoContent.setVisibility(visible = true)
            binding.rlTopBar.setVisibility(visible = false)
        }

    }
    fun addTimetable(){
        bottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetBinding = BottomSheetDialogBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setContentView(mBottomSheetBinding.root)

        mBottomSheetBinding.btnColor.setOnClickListener{
            colorPicker(mBottomSheetBinding.btnColor)
        }

        val day: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,arrayOf(getString(
                    R.string.monday),getString(R.string.tuesday),getString(R.string.wednesday),getString(
                                R.string.thursday),getString(R.string.friday)))
        day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val hours: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, arrayOf("1","2","3","4","5","6","7","8","9","10","11","12"))
        hours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mBottomSheetBinding.spDay.adapter = day
        mBottomSheetBinding.spHour.adapter = hours

        mBottomSheetBinding.btnSave.setOnClickListener{
            //The color could be empty if this happens we set a default color
            if(selectedColor.isEmpty()){
                selectedColor = R.color.orange.toString()
            }
            //Atleast the subject edittext should be filled in order to save the new database entry
            if(!mBottomSheetBinding.etSubject.toString().isEmpty()){

                if(databaseHelper.checkIfExists(mBottomSheetBinding.spHour.selectedItemPosition,mBottomSheetBinding.spDay.selectedItemPosition)){
                   databaseHelper.update(mBottomSheetBinding.etSubject.text.toString(),mBottomSheetBinding.etTeacher.text.toString(),mBottomSheetBinding.etRoom.text.toString(),selectedColor,mBottomSheetBinding.spHour.selectedItemPosition.toString(),mBottomSheetBinding.spDay.selectedItemPosition.toString())
                }else{
                   databaseHelper.insert(mBottomSheetBinding.etSubject.text.toString(),mBottomSheetBinding.etTeacher.text.toString(),mBottomSheetBinding.etRoom.text.toString(),selectedColor,mBottomSheetBinding.spHour.selectedItemPosition.toString(),mBottomSheetBinding.spDay.selectedItemPosition.toString())
                }
                //Reinitalize the timetable to display changes
                loadTimetable()
            }else{
                Toast.makeText(this,getString(R.string.empty_subject_form),Toast.LENGTH_SHORT).show()
            }
            //bottomSheetDialog.cancel()
        }

        bottomSheetDialog.show()
    }
    fun colorPicker(selectorButton : Button) {
        MaterialColorPickerDialog
            .Builder(this)
            .setTitle(R.string.select_color)
            .setColorShape(ColorShape.CIRCLE)
            .setColorSwatch(ColorSwatch._500)
            .setDefaultColor(R.color.orange)
            .setColorListener { color, colorHex ->
               selectorButton.setBackgroundColor(color)
                selectedColor = colorHex
            }
            .show()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}