package com.filiplike.powermask

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class CloudControler(context: Context){
    private  val cont = context
    private var timeList:MutableList<String> = mutableListOf()
    private val firebase = Firebase.firestore
    var user:String = R.string.userName.toString()
    val deserializer = TimeDeserializer()


    fun pushArray(array:Array<String>){
        val map = mutableMapOf<String, Any>()
        array.sortedArray().forEach {  s -> map[s] = 0}

         firebase.collection("users").document(user).set(map, SetOptions.merge())
           .addOnSuccessListener { Toast.makeText(cont,"uploaded", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(cont, "upload failed",Toast.LENGTH_SHORT).show() }


    }
    suspend fun pullData():Array<LocalDateTime?>{
          firebase.collection("users").document(user).get()
            .addOnSuccessListener {
                timeList = mutableListOf()
                var i = 0
                if (!it.data.isNullOrEmpty()) {
                    it.data?.forEach { timeList.add(i++,it.key) }
                }else{
                    Toast.makeText(cont, "No data $it",Toast.LENGTH_LONG).show()

                }
                }
            .addOnFailureListener {
                Toast.makeText(cont, "$it",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error getting documents: ", it)

            }
        return  deserializer.convertStringArray(timeList)
    }



}

