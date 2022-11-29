package org.example.adminponycafe

import android.app.ProgressDialog
import android.content.Intent
import android.icu.number.IntegerWidth
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import org.example.adminponycafe.databinding.ActivityMenuBinding
import java.text.SimpleDateFormat
import java.util.*

class MenuActivity : AppCompatActivity() {
    lateinit var binding : ActivityMenuBinding
    lateinit var imageUri : Uri
    lateinit var database : DatabaseReference
    lateinit var dbref : DatabaseReference
    lateinit var foodList: ArrayList<MenuItem>


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
        val mimeTypes = arrayOf("image/jpeg", "image/jpg")
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            binding.ivFPicture.setImageURI(imageUri)
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
        storageReference.putFile(imageUri).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {
                uploadData(it.toString())
            }
            binding.ivFPicture.setImageDrawable(null)
            //Toast.makeText(this@MenuActivity,"Subido con exito", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@MenuActivity,"Ha fallado", Toast.LENGTH_SHORT).show()
        }

        //val storeRef = FirebaseStorage.getInstance().reference.child("Food/$fileName.jpeg")
        //val img = storeRef.bucket


    }

    private fun uploadData(i: String) {
        val name = binding.etFName.text.toString()
        val desc = binding.etFDesc.text.toString()
        val cost = binding.etFCost.text.toString().toInt()

        database = FirebaseDatabase.getInstance().getReference("menu")

        val item = MenuItem(name, desc, cost, i)

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