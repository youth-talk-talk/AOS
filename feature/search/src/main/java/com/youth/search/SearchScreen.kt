package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.youth.app.feature.search.R
import com.youth.search.model.SearchResultUiEvent
import com.youth.search.model.SearchResultUiState
import com.youth.search.model.SearchState
import com.youth.search.model.SearchUiEvent
import com.youth.search.model.SearchUiState
import com.youth.search.viewmodel.SearchResultViewModel
import com.youth.search.viewmodel.SearchViewModel
import com.youthtalk.component.PolicyCard
import com.youthtalk.component.PostCard
import com.youthtalk.component.SearchChip
import com.youthtalk.component.filter.FilterComponent
import com.youthtalk.component.filter.PolicyFilterBottomSheet
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50
import com.youthtalk.model.Category
import com.youthtalk.model.EmploymentCode
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import com.youthtalk.model.Post
import com.youthtalk.util.clickableSingle

@Composable
fun SearchScreen(
    type: String,
    viewModel: SearchViewModel = hiltViewModel(),
    onClickDetailPolicy: (String) -> Unit,
    onClickDetailPost: (Long) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is SearchUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is SearchUiState.Success -> {
            val state = uiState as SearchUiState.Success

            Search(
                type = type,
                recentlyList = state.recentList,
                onClickRecently = { viewModel.uiEvent(SearchUiEvent.ClickRecently(it)) },
                onDeleteRecently = { viewModel.uiEvent(SearchUiEvent.DeleteRecently(it)) },
                onClickDetailPolicy = onClickDetailPolicy,
                onClickDetailPost = onClickDetailPost,
                onBack = onBack,
            )
        }
    }
}

@Composable
private fun Search(
    type: String,
    recentlyList: List<String>,
    onClickRecently: (String) -> Unit,
    onDeleteRecently: (String) -> Unit,
    onClickDetailPolicy: (String) -> Unit,
    onClickDetailPost: (Long) -> Unit,
    onBack: () -> Unit,
) {
    val ime = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondaryContainer),
    ) {
        SearchBar(
            text = text,
            onChangeText = { text = it },
            searchDone = {
                if (text.isNotEmpty()) {
                    navController.navigate(SearchState.SEARCH.name) {
                        restoreState = true
                        launchSingleTop = true
                    }
                    onClickRecently(text)
                }
                ime?.hide()
                focus.clearFocus()
            },
            onBack = { if (navController.currentDestination?.route == SearchState.NONE.name) onBack() else navController.popBackStack() },
            initText = { text = "" },
        )
        NavHost(
            navController = navController,
            startDestination = SearchState.NONE.name,
        ) {
            composable(
                route = SearchState.NONE.name,
            ) {
                RecentlySearchScreen(
                    recentlyList = recentlyList,
                    searchClick = { search ->
                        navController.navigate(SearchState.SEARCH.name) {
                            restoreState = true
                            launchSingleTop = true
                        }
                        text = search
                        onClickRecently(search)
                        ime?.hide()
                    },
                    onDeleteRecently = { onDeleteRecently(it) },
                )
            }

            composable(
                route = SearchState.SEARCH.name,
            ) {
                SearchResultScreen(
                    search = text,
                    type = type,
                    onClickDetailPolicy = onClickDetailPolicy,
                    onClickDetailPost = onClickDetailPost,
                )
            }
        }
    }
}

@Composable
fun SearchResultScreen(
    viewModel: SearchResultViewModel = hiltViewModel(),
    search: String,
    type: String,
    onClickDetailPolicy: (String) -> Unit,
    onClickDetailPost: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isRefresh by rememberSaveable {
        mutableStateOf(false)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        if (type == "home") {
            viewModel.uiEvent(SearchResultUiEvent.GetPolicies(search))
        } else {
            viewModel.uiEvent(SearchResultUiEvent.GetPost(type, search))
        }
    }

    when (uiState) {
        is SearchResultUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is SearchResultUiState.Success -> {
            val state = uiState as SearchResultUiState.Success
            val policies = state.policies.collectAsLazyPagingItems()
            val posts = state.posts.collectAsLazyPagingItems()

            LifecycleResumeEffect(viewModel) {
                if (isRefresh) {
                    posts.refresh()
                    policies.refresh()
                    isRefresh = false
                }

                onPauseOrDispose {
                    viewModel.clearData()
                    isRefresh = true
                }
            }
            when (type) {
                "home" -> SearchPolicies(
                    policies,
                    state.count,
                    state.policyScrapMap,
                    state.filterInfo,
                    onClickDetailPolicy = onClickDetailPolicy,
                    onClickScrap = { postId, scrap -> viewModel.uiEvent(SearchResultUiEvent.PostPolicyScrap(postId, scrap)) },
                    postFilterInfo = { filterInfo -> viewModel.uiEvent(SearchResultUiEvent.PostFilterInfo(filterInfo)) },
                    getFilterInfo = { viewModel.uiEvent(SearchResultUiEvent.GetFilterInfo) },
                    onClickFilterApply = { viewModel.uiEvent(SearchResultUiEvent.FilterApply(search)) },
                )
                else -> SearchPosts(
                    posts,
                    state.count,
                    type,
                    map = state.postScrapMap,
                    onClickDetailPost = onClickDetailPost,
                    onClickScrap = { postId, scrap -> viewModel.uiEvent(SearchResultUiEvent.PostPostScrap(postId, scrap)) },
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchPolicies(
    policies: LazyPagingItems<Policy>,
    count: Int,
    map: Map<String, Boolean>,
    filterInfo: FilterInfo,
    onClickDetailPolicy: (String) -> Unit,
    onClickScrap: (String, Boolean) -> Unit,
    postFilterInfo: (FilterInfo) -> Unit,
    getFilterInfo: () -> Unit,
    onClickFilterApply: () -> Unit,
) {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        item {
            FilterComponent(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 17.dp)
                    .padding(top = 15.dp)
                    .clickableSingle {
                        showBottomSheet = true
                        getFilterInfo()
                    },
            )
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(12.dp),
            )
            SpecPolicyInfo(
                count = count,
                title = "정책",
            )
        }

        items(
            count = policies.itemCount,
            key = { index -> policies.peek(index)?.policyId ?: "" },
        ) { index ->
            policies[index]?.let { policy ->
                PolicyCard(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp),
                    policy = policy.copy(scrap = map.getOrDefault(policy.policyId, policy.scrap)),
                    onClickDetailPolicy = onClickDetailPolicy,
                    onClickScrap = onClickScrap,
                )
            }
        }
    }

    PolicyFilterBottomSheet(
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        onDismiss = { showBottomSheet = false },
        filterInfo = filterInfo,
        onClickEmploy = {
            val newEmployList =
                if (filterInfo.employmentCodeList == null) {
                    listOf(it)
                } else {
                    filterInfo.employmentCodeList?.let { employList ->
                        if (it == EmploymentCode.ALL) {
                            null
                        } else if (employList.contains(it)) {
                            (employList - it).ifEmpty { null }
                        } else {
                            if ((employList + it).size == EmploymentCode.entries.size - 1) null else (employList + it)
                        }
                    }
                }
            postFilterInfo(filterInfo.copy(employmentCodeList = newEmployList))
        },
        onClickFinished = { postFilterInfo(filterInfo.copy(isFinished = it)) },
        onClickReset = { postFilterInfo(FilterInfo(null, null, null)) },
        onChangeAge = { postFilterInfo(filterInfo.copy(age = it.toInt())) },
        onClickApply = onClickFilterApply,
    )
}

@Composable
fun SearchPosts(
    posts: LazyPagingItems<Post>,
    count: Int,
    type: String,
    map: Map<Long, Boolean>,
    onClickDetailPost: (Long) -> Unit,
    onClickScrap: (Long, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),

    ) {
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(12.dp),
            )
            SpecPolicyInfo(
                count = count,
                title = if (type == "post") "게시글" else "후기",
            )
        }

        items(
            count = posts.itemCount,
            key = { index -> posts.peek(index)?.postId ?: 0 },
        ) { index ->
            posts[index]?.let { post ->
                val newPost = if (map.containsKey(post.postId)) {
                    post.copy(
                        scrap = map[post.postId] ?: false,
                        scraps = if (map[post.postId] == true) {
                            post.scraps + 1
                        } else {
                            post.scraps - 1
                        },
                    )
                } else {
                    post
                }

                PostCard(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .clickableSingle { onClickDetailPost(newPost.postId) },
                    policyTitle = newPost.policyTitle,
                    title = newPost.title,
                    scraps = newPost.scraps,
                    comments = newPost.comments,
                    scrap = newPost.scrap,
                    onClickScrap = { scrap -> onClickScrap(newPost.postId, scrap) },
                )
            }
        }
    }
}

fun getPolicies(): List<Policy> {
    return listOf(
        Policy(
            policyId = "R2024062424307",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424308",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424309",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424310",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424311",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424312",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424313",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424314",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
    )
}

@Composable
private fun SpecPolicyInfo(modifier: Modifier = Modifier, count: Int, title: String) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            )
            .padding(horizontal = 17.dp)
            .padding(top = 15.dp, bottom = 17.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "총 ${count}건의 정책이 있어요",
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        Icon(
            modifier = Modifier.padding(end = 30.dp),
            painter = painterResource(R.drawable.smile_icon),
            contentDescription = "스파일 아이콘",
            tint = MaterialTheme.colorScheme.onSecondary,
        )
    }
}

@Composable
private fun RecentlySearchScreen(recentlyList: List<String>, searchClick: (String) -> Unit, onDeleteRecently: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(start = 17.dp),
                text = "최근 검색",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary,
                ),
            )
        }

        items(
            count = recentlyList.size,
            key = { index -> recentlyList[index] },
        ) { index ->
            SearchChip(
                modifier = Modifier
                    .padding(8.dp)
                    .clickableSingle { searchClick(recentlyList[index]) },
                shape = RoundedCornerShape(43.dp),
                text = recentlyList[index],
                onDeleteRecently = onDeleteRecently,
            )
        }
    }
}

@Composable
private fun SearchBar(text: String, onChangeText: (String) -> Unit, searchDone: () -> Unit, onBack: () -> Unit, initText: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableSingle { onBack() },
                painter = painterResource(id = R.drawable.left_icon),
                contentDescription = "뒤로가기",
                tint = Color.Black,
            )
            BasicTextField(
                value = text,
                onValueChange = onChangeText,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = gray50,
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = { searchDone() },
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text,
                ),
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 6.dp),
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "검색 아이콘",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            initText()
                        },
                        painter = painterResource(id = R.drawable.close_icon),
                        contentDescription = "초기화",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    YongProjectTheme {
        SearchScreen(
            type = "main",
            onClickDetailPolicy = {},
            onClickDetailPost = {},
            onBack = {},
        )
    }
}
