package com.books.app.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.books.app.domain.model.Book
import com.books.app.ui.theme.ItemNormalHeight
import com.books.app.ui.theme.ItemNormalWidth
import com.books.app.ui.theme.NunitoSans
import com.books.app.ui.theme.SmallPadding
import com.books.app.ui.theme.White
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookItem(
    book: Book,
    textColor: Color = White,
    fontTitleSize: TextUnit = 16.sp,
    isAuthorVisible: Boolean = false,
    imageWidth: Dp = ItemNormalWidth,
    imageHeight: Dp = ItemNormalHeight,
    padding: Dp = SmallPadding,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    graphicsLayer: (GraphicsLayerScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {

    val authorVisibilityState by remember { mutableStateOf(isAuthorVisible) }

    Column(modifier = Modifier
        .width(imageWidth)
        .wrapContentHeight()
        .padding(end = padding)
        .graphicsLayer {
            graphicsLayer?.invoke(this)
        }
        .clickable { onClick?.invoke() },
        horizontalAlignment = horizontalAlignment
    ) {

        Card(
            modifier = Modifier
                .width(imageWidth)
                .height(imageHeight)
                .clip(RoundedCornerShape(16.dp))
        ) {
            GlideImage(
                model = book.coverUrl,
                contentScale = ContentScale.FillBounds,
                contentDescription = ""
            )
        }

        Text(
            text = book.name,
            fontSize = fontTitleSize,
            fontFamily = NunitoSans,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            color = textColor,
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 4.dp)
        )

        if (authorVisibilityState) {
            Text(
                text = book.author,
                fontSize = 14.sp,
                fontFamily = NunitoSans,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                textAlign = TextAlign.Center,
                color = White,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}