package ir.dunijet.teamgit.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.teamgit.R
import ir.dunijet.teamgit.data.model.Blog
import ir.dunijet.teamgit.ui.theme.cText2
import ir.dunijet.teamgit.util.Cache
import ir.dunijet.teamgit.util.KEY_BLOG
import ir.dunijet.teamgit.util.MyScreens
import ir.dunijet.teamgit.util.NetworkChecker

@Composable
fun SearchContent(data: List<Blog>) {
    val context = LocalContext.current
    val navigation = getNavController()

    if (!NetworkChecker(context).isInternetConnected)
        SnackBar(title = "لطفا از اتصال دستگاه خود به اینترنت مطمئن شوید")

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (noBlog, articleList) = createRefs()

        if (data.isEmpty()) {

            Column(modifier = Modifier
                .constrainAs(noBlog) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(id = R.drawable.ic_no_article),
                    contentDescription = null
                )
                Text(
                    text = "مقاله\u200Cای با این عنوان وجود ندارد",
                    style = MaterialTheme.typography.h5,
                    color = cText2
                )
            }

        } else {

            BlogList(
                modifier = Modifier.constrainAs(articleList) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                data = data
            ) {
                Cache.put(KEY_BLOG, it)
                navigation.navigate(MyScreens.BlogScreen.route)
            }

        }
    }
}
