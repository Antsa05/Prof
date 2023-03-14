package com.telecomisfun.myapplication

import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Filter
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.telecomisfun.myapplication.databinding.ActivityMainBinding
import java.lang.reflect.Type
import java.text.Collator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var profList: List<Prof>
    private lateinit var currentProfList: List<Prof>
    private lateinit var profSearchInput: EditText
    private val collator = Collator.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.profRecyclerView
        profSearchInput = binding.profSearchEditText

        val type: Type = object : TypeToken<List<Prof>>() {}.type
        profList = Gson().fromJson(resources.getRawTextFile(R.raw.prof), type) as List<Prof>
        recyclerView.adapter = ProfAdapter(profList.sortedWith(compareBy(collator) { prof -> prof.name}))

        profSearchInput.apply{

            setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    isEnabled = false
                    isActivated = false

                    enableEditTextIfDisabled(isEnabled, isActivated)
                    return@OnKeyListener true
                }
                false
            })

            doOnTextChanged { text, _, _, _ ->
                getFilter().filter(text?.trim())
            }
        }

    }

    private fun enableEditTextIfDisabled(isEditTextEnable: Boolean, isEditTextActivate: Boolean){
        if (!isEditTextEnable && !isEditTextActivate){
            profSearchInput.apply {
                isEnabled = true
                isFocusable = true
                isActivated = true
            }
        }
    }

    private fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                currentProfList = if(charSearch.isEmpty()){
                    profList
                }else{
                    val resultList = ArrayList<Prof>()
                    profList.forEach { prof ->
                        if (prof.name.lowercase().contains(charSearch.lowercase())){
                            resultList.add(prof)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = currentProfList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                recyclerView.adapter = ProfAdapter(currentProfList.sortedWith(compareBy(collator) {prof-> prof.name}))
            }
        }
    }

    private fun Resources.getRawTextFile(@RawRes id: Int) =
        openRawResource(id).bufferedReader().use { it.readText() }
}