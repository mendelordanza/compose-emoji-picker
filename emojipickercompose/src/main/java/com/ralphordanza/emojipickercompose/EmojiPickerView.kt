package com.ralphordanza.emojipickercompose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chrynan.emoji.core.Emoji
import com.chrynan.emoji.core.EmojiCategory
import com.chrynan.emoji.core.allEmojis
import com.chrynan.emoji.repo.map.KotlinMapEmojiRepository
import com.chrynan.emoji.repo.map.init
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 * @param onEmojiSelect - triggers when an emoji is selected. It passes a unicode format String.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun EmojiPickerView(modifier: Modifier = Modifier, onEmojiSelect: (unicode: String) -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val repo = remember {
        KotlinMapEmojiRepository()
    }
    var searchKeyword by remember {
        mutableStateOf("")
    }
    var emojiCategories by remember {
        mutableStateOf(listOf<EmojiCategory>())
    }
    var searchResults by remember {
        mutableStateOf(listOf<Emoji>())
    }

    LaunchedEffect(key1 = Unit) {
        repo.init()
        val categories = repo.getAllCategoryNames()
        emojiCategories = categories.map {
            repo.getCategoryByName(it)
        }
    }

    LaunchedEffect(key1 = searchKeyword) {
        val allEmojis = repo.getAll()
        searchResults = allEmojis.filter {
            it.name.contains(searchKeyword)
        }.toList()
    }

    if (emojiCategories.isNotEmpty()) {
        Scaffold(
            topBar = {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    contentColor = Color.Blue,
                    backgroundColor = Color.White,
                ) {
                    emojiCategories.forEachIndexed { index, tab ->
                        val isSelected = pagerState.currentPage == index
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Tab(
                                selected = isSelected,
                                onClick = {
                                    scope.launch {
                                        if (searchKeyword.isNotEmpty()) {
                                            searchKeyword = ""
                                        }
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                            ) {
                                val code: Emoji.Unicode = tab.icon as Emoji.Unicode
                                Text(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    text = code.character,
                                )
                            }
                        }
                    }
                }
            },
        ) { paddingValues ->
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    value = searchKeyword,
                    onValueChange = {
                        searchKeyword = it
                    },
                )
                if (searchKeyword.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Search results",
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(50.dp),
                            contentPadding = PaddingValues(
                                top = 16.dp,
                                bottom = 16.dp
                            ),
                            content = {
                                items(
                                    items = searchResults,
                                    key = {
                                        it.key.toString()
                                    },
                                ) { emoji ->
                                    val code: Emoji.Unicode = emoji as Emoji.Unicode
                                    Text(modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            val modifiedCode = emoji.character
                                            onEmojiSelect(modifiedCode)
                                        },
                                        text = code.character)
                                }
                            },
                        )
                    }
                } else {
                    HorizontalPager(
                        modifier = modifier.padding(paddingValues = paddingValues),
                        state = pagerState,
                        count = emojiCategories.size,
                    ) { page ->
                        val emojis = emojiCategories[page].allEmojis()
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = emojiCategories[page].name ?: "",
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(50.dp),
                                contentPadding = PaddingValues(
                                    top = 16.dp,
                                    bottom = 16.dp
                                ),
                                content = {
                                    items(
                                        items = emojis,
                                        key = {
                                            it.key.toString()
                                        },
                                    ) { emoji ->
                                        val code: Emoji.Unicode = emoji as Emoji.Unicode

                                        Text(modifier = Modifier
                                            .padding(16.dp)
                                            .clickable {
                                                val modifiedCode = emoji.character
                                                onEmojiSelect(modifiedCode)
                                            },
                                            text = code.character)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

