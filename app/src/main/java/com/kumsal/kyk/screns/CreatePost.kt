package com.kumsal.kyk.screns

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.hendraanggrian.socialview.commons.Hashtag
import com.hendraanggrian.socialview.commons.Mention
import com.hendraanggrian.widget.SocialAutoCompleteTextView
import com.hendraanggrian.widget.SocialTextView
import com.kumsal.kyk.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class CreatePost : AppCompatActivity() {
    private lateinit var profile_image:CircleImageView
    private lateinit var select_image:ImageButton
    private lateinit var share_button:Button
    private lateinit var post_text_element: SocialTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        profile_image=findViewById(R.id.activity_create_post_image)
        select_image=findViewById(R.id.activity_create_post_select_image)
        share_button=findViewById(R.id.activity_create_post_share)
        post_text_element=findViewById(R.id.activity_create_post_post_text_element)

        var names=ArrayList<Mention>()

        val mention1 = Mention("dirtyhobo")
        val mention2 = Mention.Builder("hobo")
            .setDisplayname("Regular Hobo")
            .setAvatarDrawable(R.mipmap.ic_launcher)
            .build()
        val mention3 = Mention.Builder("hendraanggrian")
            .setDisplayname("Hendra Anggrian")
            .setAvatarURL("https://avatars0.githubusercontent.com/u/11507430?v=3&s=460")
            .build()
        names.add(mention2)
        names.add(mention1)
        names.add(mention3)
        var adapter=ArrayAdapter<Mention>(this, android.R.layout.simple_list_item_1, names)
        val mentions: MutableList<String> = arrayListOf()
        mentions.add("@hasankucuk")
        mentions.add("@yahya")
        mentions.add("@kumsal")
//        post_text_element.setLinkedMention(mentions)
    }
}