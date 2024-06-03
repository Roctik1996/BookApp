package com.books.app.ui.view.library

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.books.app.R
import com.books.app.domain.model.Book
import com.books.app.domain.model.BookState
import com.books.app.domain.model.TopBannerSlide
import com.books.app.ui.component.BookItem
import com.books.app.ui.theme.Black
import com.books.app.ui.theme.DefaultPadding
import com.books.app.ui.theme.NunitoSans
import com.books.app.ui.theme.Pink
import com.books.app.ui.theme.SmallPadding
import com.books.app.ui.theme.TitleTextStyle
import com.books.app.ui.theme.White
import com.books.app.ui.theme.WhiteAlpha20
import com.books.app.ui.view.main.MainViewModel
import com.books.app.util.Constants.DELAY
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(navController: NavHostController, viewModel: MainViewModel = koinViewModel()) {
    val bookState by viewModel.booksStateFlow.collectAsState()

    var books: Map<String, List<Book>>
    var banners: List<TopBannerSlide>

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(DefaultPadding)
    ) {
        Text(
            text = stringResource(R.string.library),
            style = TitleTextStyle,
            color = Pink
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            when (bookState) {
                is BookState.Loading -> {
                    Text(text = stringResource(R.string.loading))
                }

                is BookState.Success -> {
                    val model = (bookState as BookState.Success).libraryModel

                    books = model.books.groupBy { it.genre }
                    banners = model.topBannerSlides

                    if (banners.isNotEmpty()) {
                        BannerCarousel(banners, navController)
                        Spacer(modifier = Modifier.height(DefaultPadding))
                    }

                    books.forEach { (genre, books) ->
                        Text(
                            text = genre,
                            style = TitleTextStyle,
                            color = White,
                            modifier = Modifier.padding(vertical = SmallPadding)
                        )
                        HorizontalBookCarousel(books, navController)
                    }
                }

                is BookState.Error -> {
                    Text(
                        text = "Error loading books: ${(bookState as BookState.Error).exception.message}",
                        color = White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun BannerCarousel(banners: List<TopBannerSlide>, navController: NavController) {
    val pageCount = banners.size

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        pageCount = {
            pageCount
        })

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(banners.size)
    )

    var key by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = key) {
        launch {
            delay(DELAY)
            with(pagerState) {
                val target = if (currentPage < pageCount - 1) currentPage + 1 else 0
                animateScrollToPage(
                    page = target,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
                key = !key
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            pageSpacing = DefaultPadding,
            flingBehavior = fling,
            beyondBoundsPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val bannerIndex = page % banners.size
            Card(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            change.consume()
                            when {
                                dragAmount < 0 -> {
                                    coroutineScope.launch {
                                        if (pagerState.currentPage == banners.lastIndex) {
                                            pagerState.animateScrollToPage(
                                                0,
                                                animationSpec = tween(
                                                    durationMillis = 500,
                                                    easing = FastOutSlowInEasing
                                                )
                                            )
                                        } else {
                                            pagerState.animateScrollToPage(
                                                pagerState.currentPage + 1, animationSpec = tween(
                                                    durationMillis = 500,
                                                    easing = FastOutSlowInEasing
                                                )
                                            )
                                        }
                                    }
                                }

                                dragAmount > 0 -> {
                                    coroutineScope.launch {
                                        if (pagerState.currentPage == 0) {
                                            pagerState.animateScrollToPage(
                                                banners.lastIndex, animationSpec = tween(
                                                    durationMillis = 500,
                                                    easing = FastOutSlowInEasing
                                                )
                                            )
                                        } else {
                                            pagerState.animateScrollToPage(
                                                pagerState.currentPage - 1, animationSpec = tween(
                                                    durationMillis = 500,
                                                    easing = FastOutSlowInEasing
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        navController.navigate("details/${banners[bannerIndex].id}")
                    }
            ) {
                GlideImage(
                    model = banners[bannerIndex].cover,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = SmallPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { iteration ->
                val color =
                    if (pagerState.currentPage % banners.size == iteration) White else WhiteAlpha20
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(7.dp)
                )
            }
        }
    }
}

@Composable
fun HorizontalBookCarousel(books: List<Book>, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        LazyRow {
            items(books) { book ->
                BookItem(book) {
                    navController.navigate("details/${book.id}")
                }
            }
        }
    }
}