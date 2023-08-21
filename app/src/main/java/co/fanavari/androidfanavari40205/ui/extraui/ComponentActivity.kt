package co.fanavari.androidfanavari40205.ui.extraui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import co.fanavari.androidfanavari40205.R
import co.fanavari.androidfanavari40205.databinding.ActivityComponentBinding
import co.fanavari.androidfanavari40205.utils.showToast
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ComponentActivity : AppCompatActivity() ,
    //Listener session A19-07-11
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityComponentBinding

    //session A19-07-11
    private lateinit var planetsArray: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component)

        binding = ActivityComponentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle edit text press
                    true
                }
                R.id.favorite -> {
                    showToast("favorite clicked")
                    // Handle favorite icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                R.id.favorite -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        binding.floatingActionButton.setOnClickListener {
            if(binding.bottomAppBar.menuAlignmentMode == BottomAppBar.MENU_ALIGNMENT_MODE_START)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        }

        binding.topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }


        //session A20-07-04
        val checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Responds to child RadioButton checked/unchecked
            when(checkedId){
                R.id.radio_button_1 -> showToast("radio button 1 clicked")

            }
        }

// To check a radio button
        binding.radioButton2.isChecked = true

// To listen for a radio button's checked/unchecked state changes
        binding.radioButton2.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to radio button being checked/unchecked
        }

        binding.check1.setOnClickListener {
            val checked: Boolean = binding.check2.isChecked
            if(checked)
                binding.radioButton3.isChecked = true
        }

        binding.check2.setOnClickListener {
            val checked: Boolean = binding.check2.isChecked
            if(checked)
                Snackbar.make(binding.linearLayoutComponent,"snackbar",
                    Snackbar.LENGTH_INDEFINITE).setAction("retry"){
                    //response to action
                }.show()
        }

        //session A19-07-11
        planetsArray = resources.getStringArray(R.array.planets_array)

        //session A19-07-09
        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            // Respond to button selection
            if (isChecked)
                showToast(isChecked.toString())

            if (checkedId == R.id.button1toggle) {
                showToast("toggle 1 selected")
                binding.check2.isChecked = false
                binding.check1.isEnabled = true
            }


            when (checkedId) {
                R.id.button1toggle -> {
                    // handle my rules
                    true
                }
                R.id.button2toggle -> {
                    // handle my rules
                    showToast(checkedId.toString())
                    true
                }
                R.id.button3toggle -> {
                    // handle my rules
                    true
                }
                else -> false
            }
        }


  /*      binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Responds to child RadioButton checked/unchecked
        }
*/

        //session A19-07-11
        val spinner = binding.planetsSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this


        //session A19-07-13

        //روش قبلی depricate شده و این روش استفاده می شود
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.contains(R.id.chip3)) {
                showToast("chip 3")
                binding.switchMaterial.isChecked = false
            }

        }
    }

    ////session A19-07-12
    fun onCheckboxClicked(view: View){

        if(view is CheckBox){
            val checked: Boolean = view.isChecked

            when(view.id){
                R.id.check2 -> {
                    if (checked){
                        Snackbar.make(
                            binding.linearLayoutComponent,
                            "Text label",
                            Snackbar.LENGTH_INDEFINITE,
                        ).setAction("Action"){
                            //respond to click on the action
                        }
                            .show()
                    }
                }
            }
        }
    }

    //session A20-07-04
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {

            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_female -> {
                    if (checked) {
                        binding.radioButton1.isChecked = false
                    }

                }

                R.id.radio_male ->
                    if (checked) {

                        //session A19-07-12
                        MaterialAlertDialogBuilder(this)
                            .setTitle(resources.getString(R.string.title))
                            .setMessage(resources.getString(R.string.supporting_text))
                            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                                // Respond to neutral button press
                            }
                            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                                // Respond to negative button press
                            }
                            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                // Respond to positive button press
                            }
                            .show()
                    }
            }
        }
    }


    //session A19-07-11
    override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {

        showToast("item with id $id and position $pos selected")
        showToast("item ${planetsArray[pos]}")

    }

    //session A19-07-11
    override fun onNothingSelected(p0: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }


    }

