package com.kumsal.kyk.screns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import com.hendraanggrian.socialview.commons.Hashtag
import com.hendraanggrian.socialview.commons.Mention
import com.hendraanggrian.widget.SocialAutoCompleteTextView
import com.kumsal.kyk.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class CreatePost : AppCompatActivity() {
    private lateinit var profile_image:CircleImageView
    private lateinit var select_image:ImageButton
    private lateinit var share_button:Button
    private lateinit var post_text_element:SocialAutoCompleteTextView<Hashtag,Mention>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        profile_image=findViewById(R.id.activity_create_post_image)
        select_image=findViewById(R.id.activity_create_post_select_image)
        share_button=findViewById(R.id.activity_create_post_share)
        post_text_element=findViewById(R.id.activity_create_post_post_text_element)

        post_text_element.setMentionEnabled(false)
        var names=ArrayList<Mention>()

        names.add(Mention("Selam"))
        names.add(Mention("yahya"))
        names.add(Mention("mahsin"))
        names.add(Mention("sdsd"))
        var adapter=ArrayAdapter<Mention>(this,android.R.layout.simple_list_item_1,names)
        post_text_element.mentionAdapter=adapter
    }
}