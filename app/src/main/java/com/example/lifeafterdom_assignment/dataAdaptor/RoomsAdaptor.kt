package com.example.lifeafterdom_assignment.dataAdaptor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.R
import com.example.lifeafterdom_assignment.data.Rooms
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.DecimalFormat

class RoomsAdaptor(private var roomsList: ArrayList<Rooms>, private val listener: OnItemClickListener): RecyclerView.Adapter <RoomsAdaptor.RoomsDisplayHolder>() {
    // Database
    private lateinit var imgRef : StorageReference
    private var decimalFormat : DecimalFormat = DecimalFormat("RM ###,###.00")

    inner class RoomsDisplayHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val imgRDHRoomImg : ImageView = itemView.findViewById(R.id.imgRDHRoomImg)
        val tvRDHRoomName : TextView = itemView.findViewById(R.id.tvRDHRoomName)
        val tvRDHPrice : TextView = itemView.findViewById(R.id.tvRDHPrice)
        val tvRDHAddress : TextView = itemView.findViewById(R.id.tvRDHAddress)

        // OnClickItem Function
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position : Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.itemClick(position)
            }
        }
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
        imgRef = FirebaseStorage.getInstance().getReference("images/room${currentItem.roomID}.png")
        val localFile : File = File.createTempFile("temp", "png")
        imgRef.getFile(localFile).addOnSuccessListener {
            val bitmap : Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            holder.imgRDHRoomImg.setImageBitmap(bitmap)
        }.addOnFailureListener{
            imgRef = FirebaseStorage.getInstance().getReference("images/empty_Image.png")
            val file : File = File.createTempFile("temp", "png")
            imgRef.getFile(file).addOnSuccessListener {
                val bitmap : Bitmap = BitmapFactory.decodeFile(file.absolutePath)

                holder.imgRDHRoomImg.setImageBitmap(bitmap)
            }
        }
        holder.tvRDHRoomName.text = currentItem.name
        holder.tvRDHPrice.text = decimalFormat.format(currentItem.price)
        holder.tvRDHAddress.text = currentItem.address
    }

    interface OnItemClickListener{
        fun itemClick(position: Int)
    }
}