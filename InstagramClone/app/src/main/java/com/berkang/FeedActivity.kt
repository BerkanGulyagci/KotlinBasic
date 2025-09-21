package com.berkang

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.berkang.adapter.FeedRecyclerAdapter
import com.berkang.instagramclone.R
import com.berkang.instagramclone.databinding.ActivityFeedBinding
import com.berkang.model.Post
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityFeedBinding
    val postArrayList: ArrayList<Post> = ArrayList()
    var adapter : FeedRecyclerAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        getDataFromFirestore()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.instamenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post) {
            val intent = Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.signout) {
            auth.signOut()
            val intent = Intent(this@FeedActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    fun getDataFromFirestore() {

        db.collection("Posts").orderBy(
            "date",
            Query.Direction.DESCENDING
        ).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            } else {

                if (snapshot != null) {
                    if (!snapshot.isEmpty) {

                        postArrayList.clear()

                        val documents = snapshot.documents
                        for (document in documents) {
                            val comment = document.get("comment") as String
                            val useremail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            //val timestamp = document.get("date") as Timestamp
                            //val date = timestamp.toDate()

                            val post = Post(useremail, comment, downloadUrl)
                            postArrayList.add(post)
                        }


                    }
                }

            }
        }
    }
}