package com.jvr.sqlite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*




class MainActivity : AppCompatActivity(), UserAdapter.Delegate {

    var userRecyclerAdapter: UserAdapter? = null
    var fab: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var dbHandler: DatabaseHandler? = null
    var userList: List<User> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initOperations()
        //initDB()
    }

    fun initDB() {
        dbHandler = DatabaseHandler(this)
        userList = (dbHandler as DatabaseHandler).users()
        userRecyclerAdapter = UserAdapter(userList, this)
        (recyclerView as RecyclerView).adapter = userRecyclerAdapter
    }

    fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recycler_view)
        userRecyclerAdapter = UserAdapter(userList, this)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    fun initOperations() {
        fab?.setOnClickListener { view ->
            val i = Intent(applicationContext, AddOrEditActivity::class.java)
            i.putExtra("Mode", "ADD")
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete All Users")
                .setPositiveButton("YES") { dialog, i ->
                    dbHandler!!.deleteAllUser()
                    initDB()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, i ->
                    dialog.dismiss()
                }
            dialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }

    override fun onItemClick(user: User) {
        val i = Intent(applicationContext, AddOrEditActivity::class.java)
        i.putExtra("Mode", "EDIT")
        i.putExtra("Id", user.id)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}
