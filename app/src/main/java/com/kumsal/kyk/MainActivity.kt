package com.kumsal.kyk


import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.opengl.Visibility
import android.os.*
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kumsal.kyk.AdapterModel.UsersModel
import com.kumsal.kyk.DBModels.DbUsers
import com.kumsal.kyk.Internet.NetworkChangeReceiver
import com.kumsal.kyk.bottomTabs.SectionPagerAdapter
import com.kumsal.kyk.bottomTabs.home_fragment
import com.kumsal.kyk.interfaces.GetCenterSimilar
import com.kumsal.kyk.interfaces.checkInternet
import com.kumsal.kyk.screns.CreatePost
import com.kumsal.kyk.screns.StarterActivity
import com.kumsal.voice_newspaper.DbElements
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.grpc.SynchronizationContext
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar
import java.util.*


class MainActivity : AppCompatActivity(), OnItemSelectedListener, View.OnClickListener,
    checkInternet {

    private var toolbar: Toolbar? = null
    private var mViewPager: ViewPager? = null
    private var sectionPagerAdapter: SectionPagerAdapter? = null
    private var mBottomBar: SmoothBottomBar? = null

    private var add: FloatingActionButton? = null
    private var addPost: FloatingActionButton? = null
    private var addMessage: FloatingActionButton? = null

    private var mDrawerLayout: DrawerLayout? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private lateinit var mFstoreUserDb: FirebaseFirestore

    private lateinit var mNavbar: NavigationView
    private lateinit var proImage: CircleImageView
    private lateinit var name: TextView
    private lateinit var username: TextView
    private lateinit var layout: LinearLayout
    lateinit var connectionState: TextView
    private var mFsPostDb: FirebaseFirestore? = null

    var isOpen: Boolean = false
    var interPolator: OvershootInterpolator = OvershootInterpolator()

    var thread = Thread()
    var thread2 = Thread()

    //Image send element
    var imageUri = ""
    var userId = ""
    var thmbImageUri = ""
    var networkChangeReceiver: NetworkChangeReceiver? = null
    var broadCastReceiver: BroadcastReceiver? = null
    var fadeIn: Animation? = null
    var fadeOut: Animation? = null
    var forbidDoubleCircle:Boolean=true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialzed
        initializeComponent()

        //FAB anime zoon
        initializeAnimation()
        addListener()

        broadCastReceiver = NetworkChangeReceiver(this)
        registerNetworkBroadcastForNougat()

    }

    companion object {
        fun dialog(value: Boolean) {
            if (value) {

            } else
                println("lost")
        }

        var stateOfInternet: Boolean? = null
    }

    fun registerNetworkBroadcastForNougat() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerReceiver(
                broadCastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.registerReceiver(
                broadCastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    protected fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(broadCastReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun addListener() {
        mViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                mBottomBar?.itemActiveIndex = position
                if (isOpen) {
                    closeFABMenu()
                }
            }

        })
        mNavbar.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.menu_bar_quit) {
                FirebaseAuth.getInstance().signOut()
                val intent: Intent = Intent(applicationContext, StarterActivity::class.java)
                startActivity(intent)
                this@MainActivity.finish()
            }
            true
        }
    }

    private fun initializeComponent() {
        toolbar = findViewById(R.id.main_activity_toolbar)
        mViewPager = findViewById(R.id.main_activity_pager_view)
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        mBottomBar = findViewById(R.id.main_activity_bottomBar)
        connectionState = findViewById(R.id.main_page_connectionStatte)
        mDrawerLayout = findViewById(R.id.main_activity_drawer)
        mNavbar = findViewById(R.id.nav_bar)
        //header initializeAnimationize

        var view = mNavbar.getHeaderView(0)

        name = view.findViewById(R.id.header_circle_name)
        username = view.findViewById(R.id.header_circle_user)
        proImage = view.findViewById(R.id.header_circle_image)

        mAuth = FirebaseAuth.getInstance()
        val mUser1: FirebaseUser? = mAuth?.currentUser
        mUser = mUser1
        mFstoreUserDb = FirebaseFirestore.getInstance()
        mFsPostDb = FirebaseFirestore.getInstance()

        userId = mUser?.uid.toString()
        Globals.ınstance?.uid = userId
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close)
        actionBarDrawerToggle?.drawerArrowDrawable?.color = Color.WHITE
        mDrawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle?.syncState()

        mViewPager?.adapter = sectionPagerAdapter
        mBottomBar?.onItemSelectedListener = this
        mBottomBar?.setOnClickListener {
            View.OnClickListener {

            }
        }
        stateOfInternet = false
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeIn?.setRepeatCount(1)
        fadeOut?.setRepeatCount(1)
        fadeOut?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                connectionState.visibility = View.INVISIBLE;
            }

            override fun onAnimationStart(animation: Animation?) {
                connectionState.visibility = View.VISIBLE;
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        fadeIn?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                connectionState.visibility = View.INVISIBLE;
            }

            override fun onAnimationStart(animation: Animation?) {
                connectionState.visibility = View.VISIBLE;
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        connectionState.visibility = View.VISIBLE
        connectionState.startAnimation(fadeOut)

    }

    private fun initializeAnimation() {
        add = findViewById(R.id.fab_add)
        addPost = findViewById(R.id.fab_edit)
        addMessage = findViewById(R.id.fab_message)

        addMessage?.alpha = 0F
        addPost?.alpha = 0F

        addMessage?.translationY = 100F
        addPost?.translationY = 100F

        add?.setOnClickListener(this)
        addPost?.setOnClickListener(this)
        addMessage?.setOnClickListener(this)

    }

    private fun closeFABMenu() {
        isOpen = false
        add?.animate()?.setInterpolator(interPolator)?.rotationBy(45f)?.setDuration(300)?.start()
        addMessage?.animate()?.translationY(100F)?.alpha(0F)?.setInterpolator(interPolator)
            ?.setDuration(
                300
            )
            ?.start()
        addPost?.animate()?.translationY(100F)?.alpha(0F)?.setInterpolator(interPolator)
            ?.setDuration(
                300
            )
            ?.start()

    }

    private fun showFABMenu() {
        isOpen = true
        add?.animate()?.setInterpolator(interPolator)?.rotationBy(45f)?.setDuration(300)?.start()
        addMessage?.animate()?.translationY(0F)?.alpha(1F)?.setInterpolator(interPolator)
            ?.setDuration(
                300
            )
            ?.start()
        addPost?.animate()?.translationY(0F)?.alpha(1F)?.setInterpolator(interPolator)?.setDuration(
            300
        )
            ?.start()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle?.onOptionsItemSelected(item)!!) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelect(pos: Int): Boolean {
        if (pos == 0) {
            mViewPager?.setCurrentItem(0)
            if (isOpen) {
                closeFABMenu()
            }
        }
        if (pos == 1) {
            mViewPager?.setCurrentItem(1)
            if (isOpen) {
                closeFABMenu()
            }
        }
        if (pos == 2) {
            mViewPager?.setCurrentItem(2)
            if (isOpen) {
                closeFABMenu()
            }
        }
        if (pos == 3) {
            mViewPager?.setCurrentItem(3)
            if (isOpen) {
                closeFABMenu()
            }
        }
        return true
    }

    override fun onStart() {
        if (mUser == null) {
            val intent: Intent = Intent(this, StarterActivity::class.java).apply {
            }
            startActivity(intent)
            super.finish()
        } else {

            var userElement = DbUsers<UsersModel>(mFstoreUserDb, UsersModel())
            var userList = userElement.readyElement(object : GetCenterSimilar<UsersModel> {
                override fun getUsers(array: ArrayList<UsersModel>) {
                    var photoImage = array.get(0).theThmbImage
                    if (!TextUtils.isEmpty(photoImage))
                        Picasso.get().load(array.get(0).theThmbImage).into(proImage)
                    name.setText(array.get(0).theNameSurname)
                    username.setText(array.get(0).theUserName)
                    thmbImageUri = array[0].theThmbImage!!
                }
            }, "Users", userId)
        }
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_action, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(v: View?) {
        var post_Activity = Intent(this, CreatePost::class.java)
        post_Activity.putExtra("thmburi", thmbImageUri)
        post_Activity.putExtra("name", name.text.toString())
        post_Activity.putExtra("username", username.text.toString())
        post_Activity.putExtra("uid", userId)
        post_Activity.putExtra("imageUri", imageUri)
        when (v?.id) {
            R.id.fab_add ->
                if (!isOpen) {
                    showFABMenu()
                } else {
                    closeFABMenu()
                }
            R.id.fab_message ->
                println("")
            R.id.fab_edit ->
                startActivity(post_Activity)
        }
    }


    override fun isOnline(value: Boolean) {
        stateOfInternet = value
        if (forbidDoubleCircle){
            if (value) {
                var theDbElement = DbElements(this, 1, "likes")
                var theDBReadElement = theDbElement.readableDatabase
                var cursor = theDBReadElement.rawQuery("SELECT * from likes", null)
                cursor.moveToFirst()
                do{
                    var dataMap = HashMap<String, Any>()
                    var dataMapForUser = HashMap<String, Any>()
                    dataMap.put("likes", FieldValue.arrayUnion(cursor.getString(0)))
                    dataMapForUser.put("postOfLiked", FieldValue.arrayUnion(cursor.getString(1)))
                    var task = mFsPostDb?.collection("Post")?.document(cursor.getString(1))
                        ?.set(dataMap, SetOptions.merge())
                    var taskForUsers =
                        mFsPostDb?.collection("Users")?.document(cursor.getString(0))?.set(
                            dataMapForUser,
                            SetOptions.merge()
                        )
                    task?.addOnCompleteListener {
                        OnCompleteListener<Void> {
                            if (it.isSuccessful) {
                                if (taskForUsers?.isSuccessful!!) {
                                    var theDbWritable = theDbElement.writableDatabase
                                    var array =
                                        theDbWritable.delete(
                                            home_fragment.FeedReaderContract.FeedEntry.TABLE_NAME,
                                            "WHERE uid=?,pid=?",
                                            arrayOf(cursor.getString(1), cursor.getString(2))
                                        )

                                } else {
                                    Log.d("Error", "Error occures")
                                }
                            } else {
                                Log.d("Error", "Error occures")
                            }
                        }
                    }
                    task?.addOnFailureListener(OnFailureListener {
                        println(it.localizedMessage)
                    })
                    break
                }while (cursor.moveToNext())

                println("is online funtion have runn")
                connectionState.startAnimation(fadeIn)
                connectionState.text = "Connection"
                connectionState.setBackgroundColor(Color.GREEN)
                setVisible(true)
                var timer1 = object : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
//                    connectionState.startAnimation(fadeOut)
                    }
                }
                timer1.start();
            } else {

                connectionState.startAnimation(fadeIn)
                connectionState.text = "Connection Lose"
                connectionState.setBackgroundColor(Color.RED)
                var timer2 = object : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        connectionState.startAnimation(fadeOut)
                    }
                }
                timer2.start()
            }
            forbidDoubleCircle=false
        }
    }

    override fun onDestroy() {
        unregisterNetworkChanges()
        super.onDestroy()
    }
}
