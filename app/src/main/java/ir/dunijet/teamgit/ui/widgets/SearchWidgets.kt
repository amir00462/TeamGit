package ir.dunijet.teamgit.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.teamgit.R
import ir.dunijet.teamgit.data.model.Blog
import ir.dunijet.teamgit.ui.theme.cPrimary
import ir.dunijet.teamgit.ui.theme.cText1
import ir.dunijet.teamgit.ui.theme.cText2
import ir.dunijet.teamgit.ui.theme.cTextFieldBackground
import ir.dunijet.teamgit.ui.theme.cTextFieldContent
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

@Composable
fun SearchToolbar(
    edtValue: String,
    isFilteringEnabled: Boolean,
    onEdtChanged: (String) -> Unit,
    onFilterClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    ConstraintLayout(
        modifier = Modifier.run {
            fillMaxWidth()
                .height(72.dp)
        }
    ) {
        val (backIcon, edt) = createRefs()

        IconButton(modifier = Modifier.constrainAs(backIcon) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
        }, onClick = { onBackPressed.invoke() }) {
            Icon(
                modifier = Modifier.rotate(180f),
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = null
            )
        }

        TextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            textStyle = MaterialTheme.typography.button,
            placeholder = {
                Text(
                    color = cTextFieldContent,
                    text = "عنوان مقاله را جستجو کنید",
                    style = MaterialTheme.typography.button
                )
            },
            value = edtValue,
            singleLine = true,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(18.dp),
                    tint = cTextFieldContent,
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            },
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(47.dp)
                        .padding(end = 3.dp)
                        .background(
                            if (isFilteringEnabled) cPrimary else Color.White,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onFilterClicked.invoke()
                        }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        tint = if(isFilteringEnabled) Color.White else cText1,
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                    )
                }
            },
            onValueChange = onEdtChanged,
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .height(52.dp)
                .constrainAs(edt) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = cText1,
                backgroundColor = cTextFieldBackground,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp),
        )
    }

}
