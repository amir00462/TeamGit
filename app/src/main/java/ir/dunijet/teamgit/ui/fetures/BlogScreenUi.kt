package ir.dunijet.teamgit.ui.fetures

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.teamgit.data.model.Blog
import ir.dunijet.teamgit.ui.widgets.BlogToolbar
import ir.dunijet.teamgit.util.Cache
import ir.dunijet.teamgit.util.KEY_BLOG

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BlogScreenUi() {
    val navigation = getNavController()
    var showInfoDialog by remember { mutableStateOf(false) }
    val blog = Cache.get(KEY_BLOG)

    Scaffold(

        topBar = {

            BlogToolbar(
                titleArticle = blog.title ,
                onBackPressed = { navigation.popBackStack() } ,
                onInfoClicked = { showInfoDialog = true }
            )

        } ,


    ) {



    }


}