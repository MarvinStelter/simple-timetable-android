package com.citroncode.stundenplan.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.citroncode.stundenplan.R
import com.citroncode.stundenplan.databinding.ItemListBinding
import com.citroncode.stundenplan.items.DatabaseItem

class TimetableAdapter(private val mList: List<DatabaseItem>, private var  showGrids:Boolean) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {


    lateinit var view : View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(showGrids){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_lined, parent, false)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        }


        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        for (z in 0..11) {
            holder.binding.tvSubject.text = (position + 1).toString()
            for(xyz in 0..48){
                if(mList.size > xyz) {
                    val x = mList[xyz]
                    if(x.day == 0){
                        //Monday
                        if(x.hour == position){
                            holder.binding.tvMonday.text = x.subject
                            holder.binding.rlMonday.setBackgroundColor(Color.parseColor(x.color))
                        }
                    }
                    if(x.day == 1){
                        //Tuesday
                        if(x.hour == position){
                            holder.binding.tvTuesday.text = x.subject
                            holder.binding.rlTuesday.setBackgroundColor(Color.parseColor(x.color))
                        }
                    }
                    if(x.day == 2){
                        //Wednesday
                        if(x.hour == position){
                            holder.binding.tvWednesday.text = x.subject
                            holder.binding.rlWednesday.setBackgroundColor(Color.parseColor(x.color))
                        }
                    }
                    if(x.day == 3){
                        //Thursday
                        if(x.hour == position){
                            holder.binding.tvThursday.text = x.subject
                            holder.binding.rlThursday.setBackgroundColor(Color.parseColor(x.color))
                        }
                    }
                    if(x.day == 4){
                        //Friday
                        if(x.hour == position){
                            holder.binding.tvFriday.text = x.subject
                            holder.binding.rlFriday.setBackgroundColor(Color.parseColor(x.color))
                        }
                    }
                }

            }

        }
    }
    override fun getItemCount(): Int {
        return 12
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val binding = ItemListBinding.bind(ItemView)
    }
}