package com.jvr.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_add_or_edit.*

class AddOrEditActivity : AppCompatActivity() {

    private var dbHandler: DatabaseHandler? = null
    private var isEditMode = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        btn_delete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "EDIT") {
            isEditMode = true
            val user: User = dbHandler!!.getUser(intent.getIntExtra("Id",0))
            edt_name.setText(user.name)
            edt_age.setText(user.age)
            edt_username.setText(user.username)
            edt_password.setText(user.password)
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener {
            val success: Boolean
            val user = User()
            user.name = edt_name.text.toString()
            user.age = edt_age.text.toString()
            user.username = edt_username.text.toString()
            user.password = edt_password.text.toString()

            if (!isEditMode) {
                success = dbHandler?.addUser(user) as Boolean
            } else {
                user.id = intent.getIntExtra("Id", 0)
                success = dbHandler?.updateUser(user) as Boolean
            }

            if (success)
                finish()
        }

        btn_delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete the User.")
                .setPositiveButton("YES") { dialog, i ->
                    val success = dbHandler?.deleteUser(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, i ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
