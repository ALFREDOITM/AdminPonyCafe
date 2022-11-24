package org.example.adminponycafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.example.adminponycafe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //pa leer datos
    private lateinit var bindingMain : ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindingMain=ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        bindingMain.btnSearch.setOnClickListener{
            val userName : String= bindingMain.etUser.text.toString()
            if(userName.isNotEmpty()){
                readData(userName)
            }else{
                Toast.makeText(this,"Ingresa un usuario por favor.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readData(userName: String) {
        database=FirebaseDatabase.getInstance().getReference("users")
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