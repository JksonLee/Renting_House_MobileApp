package com.example.lifeafterdom_assignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony.Sms.Intents
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.lifeafterdom_assignment.R

class RoomDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_detail, container, false)


        // Back Navigation
        val ibtnBack : ImageButton = view.findViewById(R.id.ibtnBack)

        ibtnBack.setOnClickListener{
            val action =
                com.example.lifeafterdom_assignment.RoomDetailFragmentDirections.actionRoomDetailFragmentToHomeFragment()

            Navigation.findNavController(view).navigate(action)
        }


        // Link To Whatsapp
        val btnContact : Button = view.findViewById(R.id.btnContact)

        btnContact.setOnClickListener{
            val uri = Uri.parse("smsto" + "+601139359182")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.setPackage("com.whatsapp")
            if(intent.resolveActivity(view.context.packageManager)!= null){
                startActivity(intent)
            }else{
                Toast.makeText(view.context,"Package is not installed", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}