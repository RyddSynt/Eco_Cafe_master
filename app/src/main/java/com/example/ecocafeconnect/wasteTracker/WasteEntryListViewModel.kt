package com.example.ecocafeconnect.wasteTracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData

class WasteEntryListViewModel(val repository: WasteEntryRepository = WasteEntryRepository()) : ViewModel() {
    val wasteEntries: LiveData<List<WasteEntry>> = repository.getWasteEntries()

    fun addWasteEntry(newWasteEntry: WasteEntry) {
        repository.addWasteEntry(newWasteEntry)
    }

    fun deleteWasteEntry(wasteEntryId: String) {
        repository.deleteWasteEntry(wasteEntryId)
    }
}
