package com.kumsal.kyk.Security

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore
import com.kumsal.kyk.Adapter.security_adapter
import com.kumsal.kyk.CustomAnimation.Animation
import com.kumsal.kyk.Globals
import com.kumsal.kyk.Models.UsersModel
import com.kumsal.kyk.Models.security_model
import com.kumsal.kyk.R
import com.kumsal.kyk.Screns.CreatePost
import com.kumsal.kyk.Screns.GetDeniedList

class SecurityScreen :AppCompatActivity,security_adapter.OnITemClickListener{
    var securityView:View?=null
    var context: Context?=null
    constructor(securityView: View?,context: Context?) {
        this.securityView = securityView
        this.context=context
    }
    constructor()
    var recyclerElement:RecyclerView?=null
    var acceptButton:Button?=null
    var textForInfo: TextView?=null
    var allFriends: RadioButton?=null
    var expectFriedns:RadioButton?=null
    var toolbar: Toolbar?=null
    var accepted_selection_name:Button?=null
    var select_all: CheckBox?=null
    private var listElement = ArrayList<security_model>()
    private var selectedlistElement = ArrayList<security_model>()
    private lateinit var search: MenuItem
    var mAdapter:security_adapter?=null
    private lateinit var mFirestore: FirebaseFirestore

    private lateinit var listOfRemoveMember: ArrayList<security_model>
    private lateinit var listOfBlockedMember: ArrayList<security_model>
    companion object{
        private var selectedlistElement = ArrayList<security_model>()
        private var mcounter = 0
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initialized();

    }
    private fun accesList(theDeniedElement: GetDeniedList) {
        mFirestore.collection("Users").document(Globals.ınstance?.uid!!)
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.d("error denied list", e.message!!)
                    return@addSnapshotListener
                }
                var theuser = document?.toObject(UsersModel::class.java)
                if (theuser?.blockBy == null) {
                    theuser?.blockBy = ArrayList<String>()
                }
                if(theuser?.blocked == null){
                    theuser?.blocked = ArrayList();
                }

                theDeniedElement.accedDenied(theuser?.blockBy, theuser?.blocked)
            }
    }

     fun initialized() {
         listOfBlockedMember= ArrayList()
         listOfRemoveMember=ArrayList()
         mFirestore= FirebaseFirestore.getInstance()
        recyclerElement = securityView?.findViewById(R.id.secure_recycler)!!
        recyclerElement!!.setHasFixedSize(true)
        recyclerElement!!.layoutManager = LinearLayoutManager(securityView!!.context)
//        mRadioGroup = securityView?.findViewById(R.id.secure_rg)
        allFriends = securityView?.findViewById(R.id.secure_allfriends)
        expectFriedns = securityView?.findViewById(R.id.secure_except)
        textForInfo = securityView?.findViewById(R.id.secure_bind_element_size)
        textForInfo?.measure(0, 0);       //must call measure!
        toolbar = securityView!!.findViewById(R.id.secure_bind_toolbar)
        acceptButton =
            securityView!!.findViewById(R.id.secure_bind_accept)
        select_all = securityView!!.findViewById(R.id.secure_bind_selectAll)
        mAdapter=security_adapter(listElement,this,SecurityScreen())
//        checkSecurePanel()
//         applicationInfo
//        context.setSupportActionBar(toolbar)
        recyclerElement!!.adapter = mAdapter

       mFirestore.collection("Users")
            .addSnapshotListener { it, error ->
              listElement.clear()
                accesList(object : GetDeniedList {
                    override fun accedDenied(
                        blockBy: ArrayList<String>?,
                        blocked: ArrayList<String>?
                    ) {
                        val mUserIDs = ArrayList<String>()
                        if (selectedlistElement.size > 0) {
                            for (i in 0..selectedlistElement.size - 1) {
                                mUserIDs.add(selectedlistElement[i].thePersonId.toString())
                            }
                        }
                        selectedlistElement.clear()
                        var isCheck = false
                        for (doc in it!!) {
                            if (doc.id == Globals.ınstance?.uid)
                                continue
                            var theData =
                                doc.toObject(UsersModel::class.java)
                            theData.theId = doc.id
                            isCheck = blocked?.contains(theData.theId)!!
                            var theSecureData = security_model(
                                theData.theNameSurname!!,
                                theData.theUserName!!,
                                theData.theThmbImage!!,
                                isCheck,
                                theData.theId!!
                            )
                            if (isCheck) {
                                selectedlistElement.add(theSecureData)
                                listOfBlockedMember.add(theSecureData)
                            }

                            listElement.add(theSecureData)
                        }
//                        firstControl = false
                    }
                })

                mAdapter?.notifyDataSetChanged()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_action, menu)

        search = menu?.findItem(R.id.search_bar_for_count)!!
        search.isVisible = false
        var searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter?.filter?.filter(newText.toString())

                var counter = 0
                for (i in 0..mAdapter?.filerList?.size?.minus(1)!!) {
                    var theSecureM = mAdapter?.filerList?.get(i);
                    if (theSecureM?.theisChecked == false) {
                        select_all?.isChecked = false;
                        counter++
                        break
                    }
                }
                if (counter == 0) {
                    select_all?.isChecked = true
                }

                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
    fun startSelection(index: Int, checkBox: LottieAnimationView) {
        if (SecurityScreen.selectedlistElement == null) {
            SecurityScreen.selectedlistElement = ArrayList<security_model>()
        }
        textForInfo?.visibility = View.VISIBLE
//        currentWith = textView.measuredWidth;
//        var anim = Animation(currentWith, textForInfo)
//        textView.animation = anim
        select_all?.visibility = View.VISIBLE
        updateToolbarText(SecurityScreen.mcounter)
//        mAdapter.notifyDataSetChanged()
        if (checkBox.frame == 0) {
            checkBox.speed = 1.0f
            checkBox.playAnimation()
            mAdapter!!.items[index].theisChecked = true
        } else {
            mAdapter!!.items[index].theisChecked = false
            checkBox.speed = -2.5f
            checkBox.playAnimation()
        }
        if (!SecurityScreen.selectedlistElement.contains(listElement.get(index))) {
            SecurityScreen.selectedlistElement.add(listElement.get(index))
            mcounter++
            updateToolbarText(SecurityScreen.mcounter)
            var theSecureM = listElement.get(index);
            theSecureM.theisChecked = true
            listElement.set(index, theSecureM)
        } else {
            select_all?.isChecked = false
            mcounter--
            updateToolbarText(SecurityScreen.mcounter)
            if (SecurityScreen.mcounter == 0) {
//                SecurityScreen.isActionMode = false
                mAdapter!!.notifyDataSetChanged()
                textForInfo?.visibility = View.GONE
                select_all?.visibility = View.GONE
            }
            SecurityScreen.selectedlistElement.remove(listElement.get(index))
            var theSecureM = listElement.get(index);
            theSecureM.theisChecked = false
            listElement.set(index, theSecureM)
        }
        acceptButton?.isEnabled = !selectedlistElement.isEmpty()
        if (listElement.size == selectedlistElement.size) {
            select_all?.isChecked = true
        } else if (listElement.size > selectedlistElement.size) {
            select_all?.isChecked = false
        }
    }
    private fun updateToolbarText(counter: Int) {
        if (counter == 0) {
            textForInfo?.setText("0 person selected ")
        } else {
            textForInfo?.setText("$counter person selected ")
        }
    }

    override fun clickCheckBox(position: Int) {
        if (!selectedlistElement.contains(listElement.get(position))) {
            selectedlistElement.add(listElement.get(position))
            mcounter++
            updateToolbarText(mcounter)
            var theSecureM = listElement.get(position);
            theSecureM.theisChecked = true
            listElement?.set(position, theSecureM)
        } else {
            select_all?.isChecked = false
            mcounter--
            updateToolbarText(mcounter)
            if (mcounter == 0) {
//                isActionMode = false
                mAdapter?.notifyDataSetChanged()
                textForInfo?.visibility = View.GONE
                select_all?.visibility = View.GONE
            }
            selectedlistElement.remove(listElement.get(position))
            var theSecureM = listElement.get(position);
            theSecureM.theisChecked = false
            listElement.set(position, theSecureM)
        }
        acceptButton?.isEnabled = !selectedlistElement.isEmpty()
        if (listElement.size == selectedlistElement.size) {
            select_all?.isChecked = true
        } else if (listElement.size > selectedlistElement.size) {
            select_all?.isChecked = false
        }
    }
}