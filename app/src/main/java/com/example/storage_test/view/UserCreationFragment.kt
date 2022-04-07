package com.example.storage_test.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.storage_test.R
import com.example.storage_test.databinding.UserCreationFragmentLayoutBinding
import com.example.storage_test.model.UserProfile
import com.example.storage_test.model.local.UserCredentials
import com.example.storage_test.model.local.UserTable

class UserCreationFragment : Fragment() {

    lateinit var binding: UserCreationFragmentLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserCreationFragmentLayoutBinding.inflate(inflater, container, false)


        binding.btnSave.setOnClickListener {
            readUser()
        }
        return binding.root
    }

    @SuppressLint("Range")
    fun readUser() {

        val readableDB = UserCredentials(
            requireContext().applicationContext,
            "User_Credentials_DB",
            1
        ).readableDatabase
        val cursorResponse =
            readableDB.query(
                UserTable.TABLE_NAME,
                arrayOf(
                    UserTable.COLUMN_FIRST,
                    UserTable.COLUMN_LAST,
                    UserTable.COLUMN_ADDRESS,
                    UserTable.COLUMN_PHONE
                ),
                null,
                null,
                null,
                null,
                null
            )
        val result = mutableListOf<UserProfile>()
        while (cursorResponse.moveToNext()){
            val first = cursorResponse.getString(cursorResponse.getColumnIndexOrThrow(UserTable.COLUMN_FIRST))
            val last = cursorResponse.getString(cursorResponse.getColumnIndexOrThrow(UserTable.COLUMN_LAST))
            val address = cursorResponse.getString(cursorResponse.getColumnIndexOrThrow(UserTable.COLUMN_ADDRESS))
            val phone = cursorResponse.getString(cursorResponse.getColumnIndexOrThrow(UserTable.COLUMN_PHONE))
            result.add(UserProfile(first, last, address, phone))
        }
        dialog(result)
    }

    fun dialog(result: MutableList<UserProfile>) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Results")
        dialog.setMessage(
            result.map {
                "${it.firstName} ${it.lastName} \n ${it.address} \n ${it.phone}"
            }.joinToString("\n")
        )
        dialog.setPositiveButton("Yes") { dialog, _ ->

            dialog.dismiss()
        }
        dialog.show()
    }

    fun storeDB(userProfile: UserProfile){
        val writable = UserCredentials(
            requireContext().applicationContext,
            "User_Credentials_DB",
            1
        ).writableDatabase

        val transactionRow =
        writable.insert(UserTable.TABLE_NAME, null, ContentValues().apply {
            put(UserTable.COLUMN_FIRST, userProfile.firstName)
            put(UserTable.COLUMN_LAST, userProfile.lastName)
            put(UserTable.COLUMN_ADDRESS, userProfile.address)
            put(UserTable.COLUMN_PHONE, userProfile.phone)
        })

        if (transactionRow > 0){
            Toast.makeText(requireContext(), "User Created", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), "Error: User Creation Failed", Toast.LENGTH_SHORT).show()
        }
    }

}