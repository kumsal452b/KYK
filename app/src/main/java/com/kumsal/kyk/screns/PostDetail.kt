package com.kumsal.kyk.screns

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.snapshot.StringNode
import com.google.firebase.firestore.FirebaseFirestore
import com.kumsal.kyk.AdapterModel.SendPostDataModel
import com.kumsal.kyk.AdapterModel.SliderImagePageAdapter
import com.kumsal.kyk.R
import com.kumsal.kyk.interfaces.imageCallback

open class PostDetail : AppCompatActivity(), imageCallback {
    var pagerView: ViewPager? = null
    var recyclerView: RecyclerView? = null
    var commentContent: EditText? = null
    var shareCommentBtn: Button? = null
    var gettedPostArguman: SendPostDataModel? = null
    var adapter: SliderImagePageAdapter? = null
    var send_message: TextInputEditText? = null
    var fsReferenceForComment: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        recyclerView = findViewById(R.id.activity_post_detail_recyclerView)
        pagerView = findViewById(R.id.activity_post_detail_pagerView)
        commentContent = findViewById(R.id.activity_post_detail_edttext)
        shareCommentBtn = findViewById(R.id.activity_post_detail_commentBtn)
        send_message = findViewById(R.id.activity_post_detail_msgText)

        gettedPostArguman = intent.getParcelableExtra<SendPostDataModel>("images")!!
        adapter = SliderImagePageAdapter(this, gettedPostArguman?.post_image_Url)
        adapter?.setOnCallbackListener(this)
        pagerView?.adapter = adapter

        //Firebase
        fsReferenceForComment = FirebaseFirestore.getInstance()
    }

    override fun imageLoadDoneCallback() {

    }

    fun activityPostDetailSend(v: View) {

        var dataMap = HashMap<String, Any>()
        dataMap.put("childFrom", "")
        var key = fsReferenceForComment?.collection("Comments")?.id
    }

    fun doCommit(view: View) {
        send_message?.let {
            if (it.requestFocus()) {
                val imm =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        }

    }
}