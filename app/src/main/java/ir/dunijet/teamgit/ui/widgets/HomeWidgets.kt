package ir.dunijet.teamgit.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.teamgit.R
import ir.dunijet.teamgit.data.model.Blog
import ir.dunijet.teamgit.ui.theme.cBackground
import ir.dunijet.teamgit.ui.theme.cError
import ir.dunijet.teamgit.ui.theme.cText2
import ir.dunijet.teamgit.ui.theme.cText5
import ir.dunijet.teamgit.util.Cache
import ir.dunijet.teamgit.util.FadeInOutWidget
import ir.dunijet.teamgit.util.KEY_BLOG
import ir.dunijet.teamgit.util.MyScreens
import ir.dunijet.teamgit.util.NetworkChecker
import kotlinx.coroutines.delay

@Composable
fun SnackBar(title: String) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(true) { isVisible = true }

    FadeInOutWidget(isVisible) {

        Snackbar(
            modifier = Modifier.padding(16.dp),
            backgroundColor = cError
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center
            )
        }

        LaunchedEffect(true) {
            delay(2500)
            isVisible = false
        }
    }
}

@Composable
fun HomeContent(data: List<Blog>, onRequestRefresh: () -> Unit) {
    val navigation = getNavController()
    val context = LocalContext.current

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
                    text = "مقاله ای برای نمایش وجود ندارد",
                    style = MaterialTheme.typography.h5,
                    color = cText2
                )
                TextButton(onClick = { onRequestRefresh.invoke() }) {
                    Text(
                        text = "بارگذاری مجدد",
                        style = MaterialTheme.typography.caption,
                        color = cText5,
                    )
                }
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

@Composable
fun HomeToolbar(
    onDrawerClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier.run {
            fillMaxWidth()
                .height(72.dp)
                .background(cBackground)
        }
    ) {

        val (search, drawer, imgMain) = createRefs()

        Image(
            modifier = Modifier.constrainAs(imgMain) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            painter = painterResource(id = R.drawable.ic_dunijet),
            contentDescription = "dunijet pic"
        )

        MainButton(modifier = Modifier.constrainAs(search) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end, 16.dp)
        }, R.drawable.ic_search) {
            onSearchClicked.invoke()
        }

        MainButton(modifier = Modifier.constrainAs(drawer) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start, 16.dp)
        }, R.drawable.ic_menu) {
            onDrawerClicked.invoke()
        }
    }

}