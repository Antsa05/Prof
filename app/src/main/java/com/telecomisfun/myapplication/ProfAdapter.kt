package com.telecomisfun.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfAdapter(
    private val profList: List<Prof>
): RecyclerView.Adapter<ProfAdapter.ProfViewHolder>() {

    class ProfViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profName: TextView = itemView.findViewById(R.id.profName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfViewHolder {
        return ProfViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout_prof, parent, false))
    }

    override fun getItemCount(): Int = profList.size

    override fun onBindViewHolder(holder: ProfViewHolder, position: Int) {
        val currentProf = profList[position]
        holder.profName.text = if (currentProf.isMale){
             holder.itemView.context.getString(R.string.prof_male_name, currentProf.name)
        }else holder.itemView.context.getString(R.string.prof_female_name, currentProf.name)

        holder.itemView.setOnClickListener {
            ProfPopup(currentProf, it.context).show()
        }

    }

}