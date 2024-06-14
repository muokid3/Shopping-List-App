package com.berxley.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.berxley.shoppinglist.composables.LocationSelectionScreen
import com.berxley.shoppinglist.composables.ShoppingListApp
import com.berxley.shoppinglist.ui.theme.ShoppingListTheme
import com.berxley.shoppinglist.utils.LocationUtils
import com.berxley.shoppinglist.viewModels.LocationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }


    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        val context = LocalContext.current
        val locationUtils = LocationUtils(context)
        val viewModel: LocationViewModel = viewModel()

        NavHost(navController = navController, startDestination = "shoppinglistscreen") {
            composable("shoppinglistscreen") {
                ShoppingListApp(
                    locationUtils = locationUtils,
                    viewModel = viewModel,
                    navController = navController,
                    context = context,
                    address = viewModel.address.value.firstOrNull()?.formatted_address
                        ?: "No Address"
                )
            }

            dialog("locationscreen") { backstack ->
                viewModel.location.value?.let { locationdata ->
                    LocationSelectionScreen(locationData = locationdata, onLocationSelected = {
                        viewModel.fetchAddress("${it.lat},${it.lng}")
                        navController.popBackStack()
                    })
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun AppPreview() {
        Navigation()
    }
}
