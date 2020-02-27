package com.jvr.sqlite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.toast
import java.util.*




class MainActivity : AppCompatActivity(), UserAdapter.Delegate {

    var disposable = CompositeDisposable()

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

        disposable.add((dbHandler as DatabaseHandler).users()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
                    userList = result
                    userRecyclerAdapter = UserAdapter(userList, this)
                    (recyclerView as RecyclerView).adapter = userRecyclerAdapter
                },
                {error-> toast("Error ${error.localizedMessage}")}
            ))

        /*userList = (dbHandler as DatabaseHandler).users()
        userRecyclerAdapter = UserAdapter(userList, this)
        (recyclerView as RecyclerView).adapter = userRecyclerAdapter*/
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

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val user: List<User> = userList.filter { it.name!!.contains(s.toString(), true) }
                userRecyclerAdapter?.filterUser(user)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
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
