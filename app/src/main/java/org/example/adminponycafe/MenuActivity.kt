package org.example.adminponycafe

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.example.adminponycafe.databinding.ActivityMenuBinding
import java.text.SimpleDateFormat
import java.util.*

class MenuActivity : AppCompatActivity() {
    lateinit var binding : ActivityMenuBinding
    lateinit var ImageUri : Uri
    lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivFPicture.setOnClickListener{
            selectImage()
        }
        binding.btnSave.setOnClickListener{
            uploadImage()
        }
    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.ivFPicture.setImageURI(ImageUri)
        }
    }
    private fun uploadImage(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Subiendo imagen ...")
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("Food/$fileName")
        storageReference.putFile(ImageUri).addOnSuccessListener {
            binding.ivFPicture.setImageURI(null)
            Toast.makeText(this@MenuActivity,"Subido de manera exitosa", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@MenuActivity,"Ha fallado", Toast.LENGTH_SHORT).show()
        }

        val name = binding.etFName.text.toString()
        val desc = binding.etFDesc.text.toString()
        val cost = binding.etFCost.text.toString().toInt()

        database = FirebaseDatabase.getInstance().getReference("menu")
        val item = MenuItem(name, desc, cost, fileName)
        database.child(name).setValue(item).addOnSuccessListener {
            Toast.makeText(this, "Subido de manera exitosa",Toast.LENGTH_SHORT).show()
            binding.etFName.text = null
            binding.etFDesc.text = null
            binding.etFCost.text = null
        }.addOnFailureListener{
            Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show()
        }
    }
}