package com.books.app.ui.view.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import com.books.app.R
import com.books.app.domain.model.Book
import com.books.app.domain.model.BookState
import com.books.app.ui.component.BookItem
import com.books.app.ui.theme.Black
import com.books.app.ui.theme.DefaultPadding
import com.books.app.ui.theme.DetailStatStyle
import com.books.app.ui.theme.Gray
import com.books.app.ui.theme.ItemLargeHeight
import com.books.app.ui.theme.ItemLargeWidth
import com.books.app.ui.theme.NunitoSans
import com.books.app.ui.theme.Pink
import com.books.app.ui.theme.SmallPadding
import com.books.app.ui.theme.TitleTextStyle
import com.books.app.ui.theme.White
import com.books.app.ui.theme.lblDetailStatStyle
import com.books.app.ui.view.main.MainViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun DetailScreen(
    navController: NavHostController,
    bookId: Int,
    viewModel: MainViewModel = koinViewModel()
) {
    val bookState by viewModel.booksStateFlow.collectAsState()
    val selectedBook by viewModel.selectedBookFlow.collectAsState()
    var selectedBookId by remember { mutableIntStateOf(bookId) }

    var recommendedBooks: List<Book>
    var youWillAlsoLikeBooks:List<Book>

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_back),
            tint = White,
            contentDescription = "",
            modifier = Modifier
                .padding(DefaultPadding)
                .clickable {
                    navController.popBackStack()
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            when (bookState) {
                is BookState.Loading -> {
                    Text(text = stringResource(R.string.loading))
                }

                is BookState.Success -> {
                    val model = (bookState as BookState.Success).libraryModel

                    val books = model.books
                    val youWillLike = model.youWillLikeSection

                    books.find { it.id == selectedBookId }?.let {
                        viewModel.selectBook(it)
                    }
                    recommendedBooks = books
                    youWillAlsoLikeBooks = books.filter { it.id in youWillLike }

                    RecommendedBookPager(
                        books = recommendedBooks,
                        initialBookId = selectedBookId,
                        selectedBook = selectedBook
                    ) {
                        selectedBookId = it.id
                        viewModel.selectBook(it)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp
                                )
                            )
                            .background(White)

                    ) {

                        selectedBook?.let { book ->
                            SelectedBookDetail(book = book)
                        }

                        HorizontalBookList(youWillAlsoLikeBooks) {
                            selectedBookId = it.id
                            viewModel.selectBook(it)
                        }

                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Pink
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 48.dp, vertical = 24.dp)
                                .padding(WindowInsets.navigationBars.asPaddingValues())
                        ) {
                            Text(text = stringResource(R.string.read_now))
                        }
                    }


                }

                is BookState.Error -> {
                    Text(text = "Error loading books: ${(bookState as BookState.Error).exception.message}")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendedBookPager(books: List<Book>, initialBookId: Int, selectedBook: Book?, onBookSelected: (Book) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { books.size })
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(books.size)
    )
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPaddingHorizontal = (screenWidth - ItemLargeWidth) / 2

    val initialPageIndex = books.indexOfFirst { it.id == initialBookId }

    LaunchedEffect(pagerState, selectedBook) {
        if (initialPageIndex != -1) {
            pagerState.scrollToPage(initialPageIndex)
        } else if (selectedBook != null) {
            val selectedIndex = books.indexOfFirst { it.id == selectedBook.id }
            if (selectedIndex != -1) {
                pagerState.scrollToPage(selectedIndex)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = 16.dp,
        pageSize = PageSize.Fixed(ItemLargeWidth),
        contentPadding = PaddingValues(horizontal = contentPaddingHorizontal),
        flingBehavior = fling,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .wrapContentWidth()
            .clickable(enabled = true) {
                onBookSelected.invoke(books[pagerState.currentPage])
            }
    ) { page ->

        BookItem(
            book = books[page],
            imageWidth = ItemLargeWidth,
            imageHeight = ItemLargeHeight,
            fontTitleSize = 20.sp,
            isAuthorVisible = true,
            horizontalAlignment = Alignment.CenterHorizontally,
            graphicsLayer = {
                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue

                lerp(
                    start = 0.80f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
            }, onClick = {
                onBookSelected.invoke(books[pagerState.currentPage])
            })
    }

}

@Composable
fun SelectedBookDetail(book: Book) {
    Column(
        modifier = Modifier
            .padding(DefaultPadding)

    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = DefaultPadding)
            ) {
                Text(
                    text = book.views,
                    style = DetailStatStyle,
                    color = Black
                )
                Text(
                    text = stringResource(R.string.readers),
                    style = lblDetailStatStyle,
                    color = Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = DefaultPadding)
            ) {
                Text(
                    text = book.likes,
                    style = DetailStatStyle
                )
                Text(
                    text = stringResource(R.string.likes),
                    style = lblDetailStatStyle,
                    color = Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = DefaultPadding)
            ) {
                Text(
                    text = book.quotes,
                    style = DetailStatStyle
                )
                Text(
                    text = stringResource(R.string.quotes),
                    style = lblDetailStatStyle,
                    color = Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = DefaultPadding)
            ) {
                Text(
                    text = book.genre,
                    style = DetailStatStyle
                )
                Text(
                    text = stringResource(R.string.genre),
                    style = lblDetailStatStyle,
                    color = Gray
                )
            }
        }

        HorizontalDivider(
            color = Gray,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
                .padding(vertical = DefaultPadding)
        )

        Text(
            text = stringResource(R.string.summary),
            style = TitleTextStyle,
            color = Black
        )

        Text(
            modifier = Modifier.padding(top = SmallPadding),
            text = book.summary,
            fontSize = 14.sp,
            fontFamily = NunitoSans,
            fontWeight = FontWeight.SemiBold

        )

        HorizontalDivider(
            color = Gray,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
                .padding(vertical = DefaultPadding)
        )

        Text(
            text = stringResource(R.string.you_will_also_like),
            style = TitleTextStyle,
            color = Black
        )
    }
}

@Composable
fun HorizontalBookList(books: List<Book>, onBookSelected: (Book) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = DefaultPadding)) {
        LazyRow {
            items(books) {
                Column(modifier = Modifier.padding(end = SmallPadding)) {
                    BookItem(it, textColor = Black) {
                        onBookSelected.invoke(it)
                    }
                }
            }
        }
    }
}

