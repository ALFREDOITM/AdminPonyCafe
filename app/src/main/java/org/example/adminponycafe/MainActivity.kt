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
    private var userTemp = ""
    private var pc=""
    private  var pcNow=0
    private var sumaPC=0
    private var temppc="1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindingMain=ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        bindingMain.btnSearch.setOnClickListener{
            val userName : String= bindingMain.etUser.text.toString()
            userTemp= bindingMain.etUser.text.toString()
            if(userName.isNotEmpty()){
                readData(userName)
            }else{
                Toast.makeText(this,"Ingresa un usuario por favor.", Toast.LENGTH_SHORT).show()
            }
            pcNow=temppc.toInt()+20
            Toast.makeText(this,"xd"+pcNow.toString(),Toast.LENGTH_SHORT).show()

        }

        bindingMain.btnAdd.setOnClickListener{
            //val userName=bindingMain.etUser.text.toString()
            Toast.makeText(this,userTemp,Toast.LENGTH_SHORT).show()
            //sumaPC=pcNow

            pc=bindingMain.etCredit.text.toString()
            addPonyCredits(userTemp,pc,pcNow)


        }
    }

    private fun addPonyCredits(userTemp: String, pc: String, pcNow:Int) {
        //Toast.makeText(this,pc,Toast.LENGTH_SHORT).show()
        database=FirebaseDatabase.getInstance().getReference("users")
        val credits= mapOf<String,String>(
            "ponycreditos" to pc
        )
        database.child(userTemp).updateChildren(credits).addOnSuccessListener {

            bindingMain.etCredit.text.clear()
            Toast.makeText(this,"Se añadieron "+pc+" créditos",Toast.LENGTH_SHORT).show()
            readData(userTemp)
        }.addOnFailureListener{
            Toast.makeText(this,"Fallo el agregar creditos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData(userName: String) {
        database=FirebaseDatabase.getInstance().getReference("users")
        database.child(userName).get().addOnSuccessListener {
            if (it.exists()){
                val ponycreditos=it.child("ponycreditos").value
                val name=it.child("name").value
                val email=it.child("email").value
                bindingMain.etUser.text.clear()
                bindingMain.tvNameRead.text=name.toString()
                bindingMain.tvEmailRead.text=email.toString()
                bindingMain.tvPonyCreditsRead.text=ponycreditos.toString()

                temppc=ponycreditos.toString()
            }else {
                Toast.makeText(this,"El usuario no existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Fallo", Toast.LENGTH_SHORT).show()
        }

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