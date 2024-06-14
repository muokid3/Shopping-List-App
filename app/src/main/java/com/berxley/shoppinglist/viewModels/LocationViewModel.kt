package com.berxley.shoppinglist.viewModels

import android.Manifest
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berxley.shoppinglist.interfaces.RetrofitClient
import com.berxley.shoppinglist.models.GeocodingResult
import com.berxley.shoppinglist.models.LocationData
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address: State<List<GeocodingResult>> = _address


    fun updateLocation(newLocationData: LocationData) {
        _location.value = newLocationData
    }

    fun fetchAddress(latlng: String) {
        try {
            viewModelScope.launch {
                val result = RetrofitClient.create().getAddressFromCoordinates(
                    latlng,
                    "AIzaSyAr1wRHL4uP47rBgE_TB4R2q35DRQDeEIw"
                )
                _address.value = result.results
            }

        } catch (e: Exception) {
            Log.e("Response:", "${e.cause} and ${e.message}")
        }

    }

}