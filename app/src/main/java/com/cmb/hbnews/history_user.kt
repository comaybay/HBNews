package com.cmb.hbnews

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_history_user.*
import kotlinx.android.synthetic.main.activity_reading_news.*
import kotlinx.android.synthetic.main.activity_reading_news.image_back_btn
import userData


class history_user : AppCompatActivity() {
    lateinit var searchText : EditText
    lateinit var recycleView: RecyclerView
    lateinit var database:DatabaseReference
    lateinit var userArraylist: ArrayList<userData>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private var userID: String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_user)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.orange)
        }
        image_back_btn.setOnClickListener {
            onBackPressed()
        }
        searchText = findViewById(R.id.searchhistory_Item)
        recycleView = findViewById(R.id.list_View_history)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        userArraylist = arrayListOf<userData>()
        firebaseAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        userID = firebaseAuth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("History").child(userID)
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for (userSnapshot in snapshot.children)
                    {
                        val user = userSnapshot.getValue(userData::class.java)
                        userArraylist.add(user!!)
                    }
                    recycleView.adapter = userAdapter(userArraylist)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        clear_all_history.setOnClickListener {
            val builder = AlertDialog.Builder(this@history_user)
            builder.setTitle("⚠️Xóa Tất Cả")
            builder.setMessage("Thao tác sẽ xóa hết tất cả lịch sử đọc của bạn.")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists())
                            {
                                snapshot.ref.removeValue()
                                Toast.makeText(this@history_user, "Đã xóa thành công", Toast.LENGTH_SHORT).show()

                            }
                        for (userSnapshot in snapshot.children)
                        {
                            val user = userSnapshot.getValue(userData::class.java)
                            userArraylist.remove(user!!)
                        }
                        recycleView.adapter = userAdapter(userArraylist)

                        }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }
    }

}