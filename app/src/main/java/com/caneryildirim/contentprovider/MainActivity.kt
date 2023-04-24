package com.caneryildirim.contentprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.caneryildirim.contentprovider.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var contractList=ArrayList<String>()
    private lateinit var permiisonLauncher: ActivityResultLauncher<String>

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        registerLauncher()

        if (!isPermissionGranted()){
            permiisonLauncher.launch(Manifest.permission.READ_CONTACTS)
        }

        binding.recyclerView.layoutManager=LinearLayoutManager(this)


        binding.fabButton.setOnClickListener {
            if (isPermissionGranted()){
                getContacts()
            }else{
                Snackbar.make(view,"İzin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver",View.OnClickListener {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                        permiisonLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }else{
                        val intent=Intent()
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri=Uri.fromParts("package",this@MainActivity.packageName,null)
                        intent.setData(uri)
                        startActivity(intent)
                    }
                }).show()

            }
        }

    }

    @SuppressLint("Range")
    private fun getContacts() {
        val projection= arrayOf<String>(ContactsContract.Contacts.DISPLAY_NAME)
        val cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            projection, null,null,ContactsContract.Contacts.DISPLAY_NAME)

        cursor?.let {
            val columIx=ContactsContract.Contacts.DISPLAY_NAME
            while (it.moveToNext()){
                contractList.add(it.getString(it.getColumnIndex(columIx)))
            }
            it.close()
            val adapter=RecyclerContractAdapter(contractList)
            binding.recyclerView.adapter=adapter
        }
    }


    fun registerLauncher(){
        permiisonLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                getContacts()
            }else{
                Toast.makeText(this,"izin verilmedi",Toast.LENGTH_SHORT).show()
            }
        }
    }
}