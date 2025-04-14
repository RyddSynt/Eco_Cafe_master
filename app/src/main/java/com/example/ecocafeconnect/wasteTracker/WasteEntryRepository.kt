package com.example.ecocafeconnect.wasteTracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class WasteEntryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val wasteEntriesRef = db.collection("wasteEntries")

    fun getWasteEntries(): LiveData<List<WasteEntry>> {
        val wasteEntries = MutableLiveData<List<WasteEntry>> ()
        wasteEntriesRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("WasteEntryRepository", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val wasteEntriesList = snapshot.toObjects(WasteEntry::class.java)
                wasteEntries.value = wasteEntriesList
            } else {
                Log.d("WasteEntryRepository", "Current data: null")
            }
        }
        return wasteEntries
    }

    fun addWasteEntry(wasteEntry: WasteEntry) {
        wasteEntry.id = wasteEntriesRef.document().id
        wasteEntriesRef.document(wasteEntry.id).set(wasteEntry)
    }

    fun deleteWasteEntry(wasteEntryId: String) {
        wasteEntriesRef.document(wasteEntryId).delete().addOnFailureListener { e ->
            Log.w("WasteEntryRepository", "Error deleting waste entry", e)
        }
    }
}