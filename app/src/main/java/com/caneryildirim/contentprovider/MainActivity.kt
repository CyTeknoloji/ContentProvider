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
    private var contractList=ArrayList<Users>()
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
                    if (isShouldRationale(Manifest.permission.READ_CONTACTS)){
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
        val projection = arrayOf<String>(ContactsContract.Contacts.DISPLAY_NAME,
                                        ContactsContract.Contacts.HAS_PHONE_NUMBER,
                                        ContactsContract.Contacts._ID)
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            projection, null,null,ContactsContract.Contacts.DISPLAY_NAME)

        cursor?.let {c->
            contractList.clear()
            val columIxName = ContactsContract.Contacts.DISPLAY_NAME
            val columIxPhone = ContactsContract.Contacts.HAS_PHONE_NUMBER
            val columIxId = ContactsContract.Contacts._ID

            while (c.moveToNext()){
                val contractId = c.getString(c.getColumnIndex(columIxId))
                val userName = c.getString(c.getColumnIndex(columIxName))
                val hasPhoneNumber = c.getString(c.getColumnIndex(columIxPhone))
                var phoneNumber : String? = null

                if (hasPhoneNumber == "1"){
                    val phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+ contractId,null,null
                    )

                    phoneCursor?.let { pC->
                        while (pC.moveToNext()){
                            phoneNumber = pC.getString(pC.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                        pC.close()
                    }
                }
                phoneNumber?.let {pN->
                    addUser(userName,pN)
                }?:{
                    addUser(userName,"null")
                }


            }
            c.close()
            val adapter = RecyclerContractAdapter(contractList)
            binding.recyclerView.adapter = adapter
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

    private fun addUser(userName:String,phoneNumber:String){
        val user = Users(userName,phoneNumber)
        contractList.add(user)
    }
}