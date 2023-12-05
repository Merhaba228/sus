package com.example.sus

import SharedPrefManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.sus.activity.logic.auth.retrofit.dto.ForumMessage

class ForumMessage : AppCompatActivity() {

    private lateinit var sendMessageButton: ImageButton
    private lateinit var textMessage: EditText
    private lateinit var scrollView: ScrollView
    private val myCoroutineScope = CoroutineScope(Dispatchers.Main)
    private val deleteButtonArray = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_message)

        val disciplineid = SharedPrefManager.getDiscipline()?.id
        val title = SharedPrefManager.getDiscipline()?.title

        sendMessageButton = findViewById(R.id.sendMessageButton)
        textMessage = findViewById(R.id.sendMessageText)
        scrollView = findViewById(R.id.scrollviewforum)
        val MainLayout: LinearLayout = findViewById(R.id.forum_message_layout)
        val sr: TextView = findViewById(R.id.forum_message_discipline_name)
        val MainSection = LinearLayout(this)
        sr.text = title
        val loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicatorforumMessage)
        loadingIndicator.visibility = View.VISIBLE

        myCoroutineScope.launch {
            try {
                val forumMessage: List<ForumMessage> = suspendCoroutine { continuation ->
                    if (disciplineid != null) {
                        SharedPrefManager.getForumMessageUsingRefreshToken(
                            disciplineid.toInt()
                        ) { studentForumMessage ->
                            continuation.resume(studentForumMessage)
                        }
                    }
                }
                if (disciplineid != null) {
                    createForumMessage(MainSection,disciplineid.toInt(),MainLayout,forumMessage)
                }
                loadingIndicator.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        sendMessageButton.setOnClickListener {
            myCoroutineScope.launch {
                try {
                    val forumMessage: ForumMessage = suspendCoroutine { continuation ->
                        if (disciplineid != null) {
                            SharedPrefManager.sendForumMessageUsingRefreshToken(
                                textMessage.text.toString(), disciplineid.toInt()
                            ) { studentForumMessage ->
                                continuation.resume(studentForumMessage)
                            }
                        }
                    }
                    if (disciplineid != null) {
                        addSendMessage(MainSection,MainLayout,forumMessage, disciplineid.toInt())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val backButton: ImageButton = findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            this.onBackPressed()
        }

    }

    private fun clearLayout(layout: LinearLayout)
    {
        layout.removeAllViews()
    }

    fun scrollDown() {
        val scrollThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(200)
                    this@ForumMessage.runOnUiThread(Runnable { scrollView.fullScroll(View.FOCUS_DOWN) })
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        scrollThread.start()
    }

    private fun addSendMessage(MainSection: LinearLayout, MainLayout: LinearLayout, forumMessage: ForumMessage, disciplineid: Int)
    {
        val userid = SharedPrefManager.getUserData()?.id
        val AlignSection = LinearLayout(this)
        AlignSection.orientation = LinearLayout.VERTICAL
        val ControlDotSection = LinearLayout(this)
        ControlDotSection.setBackgroundResource(R.drawable.rounded_violet)
        val w = (MainLayout.width*0.8).toInt()
        val paramscds = LinearLayout.LayoutParams(w, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
        paramscds.setMargins(0,0,0,50)
        ControlDotSection.layoutParams = paramscds
        ControlDotSection.setPadding(10, 10, 10, 10)

        ControlDotSection.orientation = LinearLayout.VERTICAL

        val textSection = LinearLayout(this)
        textSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textSection.orientation = LinearLayout.VERTICAL
        textSection.setPadding(10,10,10,10)
        val rawtitle = forumMessage.text
        val titleTextView = TextView(this)
        titleTextView.setTextColor(Color.BLACK)
        titleTextView.text = rawtitle
        textSection.addView(titleTextView)

        val paramsnsSection = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val nsSection = LinearLayout(this)
        nsSection.setBackgroundResource(R.drawable.rounded_violet)
        nsSection.orientation = LinearLayout.HORIZONTAL
        nsSection.layoutParams = paramsnsSection
        val deleteButtonLayout = LinearLayout(this)
        deleteButtonLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 85F)
        val deleteButton = Button(this)
        deleteButton.setBackgroundResource(R.drawable.baseline_delete_24)
        deleteButton.layoutParams = LinearLayout.LayoutParams(100, 100)
        deleteButton.id = forumMessage.id
        val NameSection = LinearLayout(this)
        if(forumMessage.isTeacher) {
            AlignSection.gravity = Gravity.START
        }
        else
        {
            AlignSection.gravity = Gravity.END
        }
        NameSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15F)
        NameSection.setPadding(20, 10, 20, 10)
        NameSection.orientation = LinearLayout.VERTICAL

        val userName = forumMessage.user.fio
        val userNameTextView = TextView(this)
        userNameTextView.setTextColor(Color.BLACK)
        userNameTextView.text = userName

        val messageData = forumMessage.createDate
        val formattedDate = messageData.substring(0,10)
        val messageDataTextView = TextView(this)
        messageDataTextView.setTextColor(Color.BLACK)
        messageDataTextView.setTextSize(10F)
        messageDataTextView.text = formattedDate

        NameSection.addView(userNameTextView)
        NameSection.addView(messageDataTextView)
        deleteButtonArray.add(deleteButton)
        deleteButtonLayout.addView(deleteButton)
        nsSection.addView(NameSection)
        if(userid == forumMessage.user.id) {
            nsSection.addView(deleteButtonLayout)
        }

        ControlDotSection.addView(nsSection)
        ControlDotSection.addView(textSection)
        AlignSection.addView(ControlDotSection)
        MainSection.addView(AlignSection)
        deleteButton(deleteButton, disciplineid, MainSection, MainLayout)
    }

    private fun deleteButton(deleteButton: Button, disciplineid: Int, MainSection: LinearLayout, MainLayout: LinearLayout)
    {
        deleteButton.setOnClickListener {
            clearLayout(MainSection)
            clearLayout(MainLayout)
            val updateCor = CoroutineScope(Dispatchers.IO)
            val buttonid = deleteButton.id
            val cor = myCoroutineScope.launch {
                try {
                    val deleteMessage: Unit = suspendCoroutine { continuation ->
                        SharedPrefManager.deleteForumMessage(
                            buttonid
                        ) { studentForumMessage ->
                            continuation.resume(studentForumMessage)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("deleteForumMessage_error", e.message.toString())
                }
            }
            updateCor.launch {
                try {
                    delay(700)
                    val forumMessage: List<ForumMessage> = suspendCoroutine { continuation ->
                        SharedPrefManager.getForumMessageUsingRefreshToken(
                            disciplineid
                        ) { studentForumMessage ->
                            continuation.resume(studentForumMessage)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        createForumMessage(
                            MainSection,
                            disciplineid,
                            MainLayout,
                            forumMessage
                        )
                    }
                } catch (e: Exception) {
                    Log.e("deleteForumMessage_error", e.message.toString())
                }
            }
        }
    }

    private fun createForumMessage(MainSection: LinearLayout, disciplineid: Int,MainLayout: LinearLayout,forumMessage: List<ForumMessage>)
    {

        val userid = SharedPrefManager.getUserData()?.id
        clearLayout(MainLayout)

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0,0,0,50)
        MainSection.layoutParams = params
        MainSection.orientation = LinearLayout.VERTICAL

        for(i in (0..<forumMessage.size).reversed()) {
            val AlignSection = LinearLayout(this)
            AlignSection.orientation = LinearLayout.VERTICAL
            val ControlDotSection = LinearLayout(this)
            ControlDotSection.setBackgroundResource(R.drawable.rounded_violet)
            val w = (MainLayout.width*0.8).toInt()
            val paramscds = LinearLayout.LayoutParams(w, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
            paramscds.setMargins(0,0,0,50)
            ControlDotSection.layoutParams = paramscds
            ControlDotSection.orientation = LinearLayout.VERTICAL

            val textSection = LinearLayout(this)
            textSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            textSection.orientation = LinearLayout.VERTICAL
            textSection.setPadding(10,10,10,10)
            val rawtitle = forumMessage[i].text
            val titleTextView = TextView(this)
            titleTextView.setTextColor(Color.BLACK)
            titleTextView.text = rawtitle
            textSection.addView(titleTextView)

            val paramsnsSection = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val nsSection = LinearLayout(this)
            nsSection.setBackgroundResource(R.drawable.rounded_violet)
            nsSection.orientation = LinearLayout.HORIZONTAL
            nsSection.layoutParams = paramsnsSection
            val deleteButtonLayout = LinearLayout(this)
            deleteButtonLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 85F)
            val deleteButton = Button(this)
            deleteButton.setBackgroundResource(R.drawable.baseline_delete_24)
            deleteButton.layoutParams = LinearLayout.LayoutParams(100, 100)
            deleteButton.id = forumMessage[i].id
            val NameSection = LinearLayout(this)
            if(forumMessage[i].isTeacher) {
                AlignSection.gravity = Gravity.START
            }
            else
            {
                AlignSection.gravity = Gravity.END
            }
            NameSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15F)
            NameSection.setPadding(20, 10, 20, 10)
            NameSection.orientation = LinearLayout.VERTICAL

            val userName = forumMessage[i].user.fio
            val userNameTextView = TextView(this)
            userNameTextView.setTextColor(Color.BLUE)
            userNameTextView.text = userName

            val messageData = forumMessage[i].createDate
            val formattedDate = messageData.substring(0,10)
            val messageDataTextView = TextView(this)
            messageDataTextView.setTextColor(Color.BLACK)
            messageDataTextView.setTextSize(10F)
            messageDataTextView.text = formattedDate

            NameSection.addView(userNameTextView)
            NameSection.addView(messageDataTextView)
            deleteButtonArray.add(deleteButton)
            deleteButtonLayout.addView(deleteButton)
            nsSection.addView(NameSection)
            if(userid == forumMessage[i].user.id) {
                nsSection.addView(deleteButtonLayout)
            }

            ControlDotSection.addView(nsSection)
            ControlDotSection.addView(textSection)
            AlignSection.addView(ControlDotSection)
            MainSection.addView(AlignSection)
            deleteButton(deleteButton, disciplineid, MainSection, MainLayout)
        }
        MainLayout.addView(MainSection)
        scrollDown()
    }
}