package org.example.adminponycafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.admin_menu_interface -> {
                lanzarMenuInterface()
                true
            }
            R.id.admin_user_interface -> {
                //lanzarUserInterface()
                true
            }
            R.id.admin_order_interface -> {
                //lanzarOrderInterface()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun lanzarMenuInterface(){
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
    }
}