package com.kumsal.kyk.Security

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SecurityScreen {
    var securityView:View?=null
    constructor(securityView: View?) {
        this.securityView = securityView
    }
    var recyclerElement:RecyclerView?=null
    var acceptButton:Button?=null
    var textForInfo: TextView?=null
    var allFriends: RadioButton?=null
    var expectFriedns:RadioButton?=null
    var toolbar:Toolbar?=null
    var accepted_selection_name:Button?=null
    var select_all: CheckBox?=null

    fun mainElement(){
        recyclerElement = securityView?.findViewById(R.id.secure_recycler)!!
        recyclerElement.setHasFixedSize(true)
        recyclerElement.layoutManager = LinearLayoutManager(securityView.context)
        mRadioGroup = securityView?.findViewById(R.id.secure_rg)
        allFriends = securityView?.findViewById(R.id.secure_allfriends)
        excpection = securityView?.findViewById(R.id.secure_except)
        textForInfo = securityView?.findViewById(R.id.secure_bind_element_size)
        textForInfo.measure(0, 0);       //must call measure!
        toolbar = securityView.findViewById(R.id.secure_bind_toolbar)
        acceptButton =
            securityView.findViewById(R.id.secure_bind_accept)
        selectedAll = securityView.findViewById(R.id.secure_bind_selectAll)
        checkSecurePanel()
        setSupportActionBar(toolbar)
        recyclerView.adapter = mAdapter
    }
}