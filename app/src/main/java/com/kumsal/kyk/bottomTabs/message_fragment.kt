package com.kumsal.kyk.bottomTabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.kumsal.kyk.R

class message_fragment : Fragment() {

    private lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Deneme("Merhaba","Kumsal")
        mView=inflater.inflate(R.layout.fragment_message, container, false)
        return mView
    }
    fun Deneme( ad: String, soyad: String) {
       println("selam evlat")
    }

}