package ir.dunijet.teamgit.ui.fetures

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun HomeScreenUi() {

    val viewModel = getNavViewModel<HomeViewModel>()
    val navigation = getNavController()

    val data by viewModel.blogs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Log.v("testData" , data.toString())

}