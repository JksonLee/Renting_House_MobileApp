package com.example.lifeafterdom_assignment.dataAdaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.R
import com.example.lifeafterdom_assignment.data.Rooms

class RoomsAdaptor(private val roomsList: List<Rooms>): RecyclerView.Adapter <RoomsAdaptor.RoomsDisplayHolder>() {
    class RoomsDisplayHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imgRDHRoomImg : ImageView = itemView.findViewById(R.id.imgRDHRoomImg)
        val tvRDHRoomName : TextView = itemView.findViewById(R.id.tvRDHRoomName)
        val tvRDHPrice : TextView = itemView.findViewById(R.id.tvRDHPrice)
        val tvRDHAddress : TextView = itemView.findViewById(R.id.tvRDHAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsDisplayHolder {
        val itemView = LayoutInflater.from(parent.context).inflate( R.layout.rooms_display_holder, parent, false )

        return RoomsDisplayHolder(itemView)
    }

    override fun getItemCount(): Int {
        return roomsList.size
    }

    override fun onBindViewHolder(holder: RoomsDisplayHolder, position: Int) {
        val currentItem = roomsList[position]
//        holder.imgRDHRoomImg = currentItem.image
        holder.tvRDHRoomName.text = currentItem.name
        holder.tvRDHPrice.text = "RM " + currentItem.price.toString()
        holder.tvRDHAddress.text = currentItem.address
    }
}