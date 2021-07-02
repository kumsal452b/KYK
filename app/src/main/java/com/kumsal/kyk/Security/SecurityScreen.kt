package com.kumsal.kyk.Security

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class SecurityScreen {
    var securityView:View?=null
    constructor(securityView: View?) {
        this.securityView = securityView
    }
    var recyclerElement:RecyclerView?=null
    var acceptButton:Button?=null
}