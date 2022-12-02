package org.example.adminponycafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.example.adminponycafe.databinding.ActivityMainBinding
import android.view.GestureDetector
import android.view.MotionEvent
import java.lang.Math.abs

enum class ProviderType{
    BASIC
}

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    //pa leer datos
    private lateinit var bindingMain : ActivityMainBinding
    private lateinit var database: DatabaseReference
    private var userTemp = ""
    private var pc=""
    private  var pcNow=0
    private var sumaPC=0
    private var temppc="1"

    //gestos
    lateinit var gestureDetector: GestureDetector
    private var x2:Float =0.0f
    private var x1:Float =0.0f
    private var y2:Float =0.0f
    private var y1:Float =0.0f

    companion object{
        const val MIN_DISTANCE=150
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindingMain=ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        val bundle =intent.extras
        val email =bundle?.getString("email")
        val provider =bundle?.getString("provider")
        setup()
        gestureDetector= GestureDetector(this,this)
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

    private fun setup() {
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val start = Intent(this, LoginActivity::class.java)
            startActivity(start)
            finish()
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        when(event?.action){

            //inicio swipe
            0->{
                x1=event.x
                y1=event.y
            }

            //fin swipe
            1->{
                x2=event.x
                y2=event.y

                val valueX:Float=x2-x1
                val valueY:Float=y2-y1

                if (abs(valueX)> MIN_DISTANCE){
                    if(x2>x1){
                        val start = Intent(this, MenuActivity::class.java)
                        startActivity(start)//Toast.makeText(this,"derecha",Toast.LENGTH_SHORT).show()

                    }else{
                        //Toast.makeText(this,"izq",Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        val start = Intent(this, LoginActivity::class.java)
                        startActivity(start)
                        finish()
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }

    //Gestos
    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {

    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }
}