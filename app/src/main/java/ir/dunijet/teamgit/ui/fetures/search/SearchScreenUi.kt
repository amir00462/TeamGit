package ir.dunijet.teamgit.ui.fetures.search

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import ir.dunijet.teamgit.ui.theme.cBackground
import ir.dunijet.teamgit.ui.widgets.HomeContent
import ir.dunijet.teamgit.ui.widgets.HomeDrawer
import ir.dunijet.teamgit.ui.widgets.HomeToolbar
import ir.dunijet.teamgit.util.NO_FILTER
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreenUi() {
    val viewModel = getNavViewModel<SearchViewModel>()
    val navigation = getNavController()
    var showFilterDialog by remember { mutableStateOf(false) }

    val data by viewModel.blogs.collectAsState()
    val categoryList by viewModel.categoryList.collectAsState()
    val authorList by viewModel.authors.collectAsState()
    val searchedQuery by viewModel.searchQuery.collectAsState()
    val filtering by viewModel.filtering.collectAsState()

    var isFilterEnabled by remember { mutableStateOf(filtering != NO_FILTER) }

    Scaffold(
        topBar = {
            // SearchToolbar()
        },
        modifier = Modifier.fillMaxSize(),
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            // SearchContent()

            if (showFilterDialog) {
                // SearchDialog()
            }

        }
    }
}