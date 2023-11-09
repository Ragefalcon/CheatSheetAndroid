package ru.ragefalcon.cheatsheetandroid.compose.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ru.ragefalcon.cheatsheetandroid.R
import ru.ragefalcon.cheatsheetandroid.compose.lifecycles.LifecyclesCheatList
import ru.ragefalcon.cheatsheetandroid.compose.lifecycles.LifecyclesTab
import ru.ragefalcon.cheatsheetandroid.compose.settings.ColorsTab
import ru.ragefalcon.cheatsheetandroid.di.OrientationState
import ru.ragefalcon.cheatsheetandroid.viewmodels.SettingViewModel


enum class CheatSheetPages(
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
    @DrawableRes val iconOutResId: Int
) {
    LIFECYCLES(
        R.string.title_page_lifecycles,
        R.drawable.vi_baseline_attractions_24,
        R.drawable.vi_outline_attractions_24
    ),
    COMPOSE(
        R.string.title_page_compose,
        R.drawable.vi_baseline_rocket_launch_24,
        R.drawable.vi_outline_rocket_launch_24
    ),
    SETTINGS(R.string.title_page_settings, R.drawable.vi_baseline_palette_24, R.drawable.vi_outline_palette_24);

    fun getFilledIcon(filled: Boolean): Int = if (filled) iconResId else iconOutResId
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomePage(
    orientationState: OrientationState,
    onLifecycleCheatClick: (LifecyclesCheatList) -> Unit
) {
    val pagerState = rememberPagerState() { CheatSheetPages.entries.size }
    Scaffold {
        HomePagerScreen(
            orientationState = orientationState,
            onLifecycleCheatClick = onLifecycleCheatClick,
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
    orientationState: OrientationState,
    onLifecycleCheatClick: (LifecyclesCheatList) -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    pages: Array<CheatSheetPages> = CheatSheetPages.entries.toTypedArray(),
    settingsViewModel: SettingViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    Row(modifier) {
        if (orientationState.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            NavigationRail(
                modifier = Modifier.fillMaxHeight(),
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                pages.forEachIndexed { index, page ->
                    val name = stringResource(page.titleResId)
                    NavigationRailItem(
                        selected = index == pagerState.currentPage,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        label = { Text(name) },
                        icon = {
                            Icon(
                                painter = painterResource(page.getFilledIcon(index == pagerState.currentPage)),
                                contentDescription = name
                            )
                        },
                        colors = NavigationRailItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary,
                        )
                    )

                }
            }
        }
        Column {
            if (orientationState.orientation == Configuration.ORIENTATION_PORTRAIT)
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {
                    pages.forEachIndexed { index, page ->
                        val name = stringResource(page.titleResId)
                        NavigationBarItem(
                            selected = index == pagerState.currentPage,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            label = { Text(name) },
                            icon = {
                                Icon(
                                    painter = painterResource(page.getFilledIcon(index == pagerState.currentPage)),
                                    contentDescription = name
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.secondary,
                                unselectedTextColor = MaterialTheme.colorScheme.secondary,
                            )
                        )
                    }
                }
            /*
                            TabRow(
                                containerColor = MaterialTheme.colorScheme.background,
                                divider = {},
                                selectedTabIndex = pagerState.currentPage
                            ) {
                                pages.forEachIndexed { index, page ->
                                    val name = stringResource(page.titleResId)
                                    Tab(
                                        selected = index == pagerState.currentPage,
                                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                                        text = { Text(name) },
                                        icon = {
                                            Icon(painter = painterResource(page.getFilledIcon(index == pagerState.currentPage)),
                                                contentDescription = name)
                                        },
                                        selectedContentColor = MaterialTheme.colorScheme.onBackground,
                                        unselectedContentColor = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
            */

            HorizontalPager(
//            modifier = Modifier,
                state = pagerState,
//            pageSpacing = 0.dp,
//            userScrollEnabled = true,
//            reverseLayout = false,
//            contentPadding = PaddingValues(2.dp),
//            beyondBoundsPageCount = 0,
//            pageSize = PageSize.Fill,
//            flingBehavior = PagerDefaults.flingBehavior(state = state),
//            key = null,
//            pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
//                Orientation.Horizontal
//            )
            ) { index ->
//                Surface(
//                    shadowElevation = 2.dp,
//                    shape = RoundedCornerShape(10.dp),
//                    modifier = Modifier.padding(6.dp).fillMaxSize()
//                ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (pages[index]) {
                        CheatSheetPages.LIFECYCLES -> {
                            LifecyclesTab(onLifecycleCheatClick)
                        }

                        CheatSheetPages.COMPOSE -> {
                            Text("COMPOSE")
                        }

                        CheatSheetPages.SETTINGS -> {
                            ColorsTab(orientationState)
                        }
                    }
                }
//                }
            }
        }
    }
}