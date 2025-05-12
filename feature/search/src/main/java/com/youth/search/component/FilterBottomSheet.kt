package com.youth.search.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.component.button.Round6Button
import com.youthtalk.component.chip.RoundChip
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray60
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.FilterType
import kotlin.math.ceil
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(modifier: Modifier = Modifier, sheetState: SheetState, onDismiss: () -> Unit) {
    val pagerState = rememberPagerState { FilterType.entries.size }
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val types = FilterType.entries.toList()

    val tabPositions = remember { mutableStateMapOf<Int, Pair<Float, Float>>() }

    val targetX = tabPositions[pagerState.currentPage]?.first ?: 0f
    val targetWidth = tabPositions[pagerState.currentPage]?.second ?: 0f

    val animatedX by animateDpAsState(targetValue = with(LocalDensity.current) { targetX.toDp() })
    val animatedWidth by animateDpAsState(targetValue = with(LocalDensity.current) { targetWidth.toDp() })
    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        sheetState = sheetState,
        containerColor = gray10,
        onDismissRequest = onDismiss
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            text = "필터",
            style = MaterialTheme.typography.titleLarge
        )

        TabRowComponent(
            currentPage = pagerState.currentPage,
            types = types,
            lazyListState = lazyListState,
            onClickTab = {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            },
            onMeasuredWidth = { index, pair ->
                tabPositions[index] = pair
            }
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = gray40
            )

            // 인디케이터
            Box(
                modifier = Modifier
                    .offset(x = animatedX)
                    .width(animatedWidth)
                    .height(2.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomStart)
            )
        }

        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState
        ) {
            val focusManager = LocalFocusManager.current
            LaunchedEffect(pagerState.currentPage) {
                focusManager.clearFocus()
                lazyListState.animateScrollToItem(pagerState.currentPage)
            }
            when (types[it]) {
                FilterType.POLICY_TYPE -> PolicyType()

                FilterType.REGION -> RegionType()

                FilterType.RECRUIT -> {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        stringArrayResource(R.array.recruits).forEach {
                            RoundChip(
                                text = it
                            )
                        }
                    }
                }

                FilterType.EDUCATION -> {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        stringArrayResource(R.array.educations).forEach {
                            RoundChip(
                                text = it
                            )
                        }
                    }
                }

                FilterType.SPECIALIZED -> Serialized()

                FilterType.AGE_EARN -> AgeEarn()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 26.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "초기화",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = gray70
                    )
                )

                Image(
                    painter = painterResource(R.drawable.refresh),
                    contentDescription = "새로고침",
                    colorFilter = ColorFilter.tint(color = gray70)
                )
            }

            Round6Button(
                modifier = Modifier.weight(1f),
                text = "적용하기",
                textColor = gray70,
                backgroundColor = gray30,
                onClick = {}
            )
        }
    }
}

@Composable
fun TabRowComponent(
    modifier: Modifier = Modifier,
    currentPage: Int,
    lazyListState: LazyListState,
    types: List<FilterType>,
    onClickTab: (Int) -> Unit,
    onMeasuredWidth: (Int, Pair<Float, Float>) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(40.dp),
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            count = types.size
        ) {
            val title = when (types[it]) {
                FilterType.POLICY_TYPE -> "정책분야"
                FilterType.REGION -> "지역"
                FilterType.RECRUIT -> "취업상태"
                FilterType.EDUCATION -> "학력"
                FilterType.SPECIALIZED -> "특화 분야"
                FilterType.AGE_EARN -> "연령 및 소득"
            }

            Text(
                modifier = Modifier
                    .clickable {
                        onClickTab(it)
                    }
                    .onGloballyPositioned { layoutCoordinates ->
                        val bounds = layoutCoordinates.boundsInParent()
                        onMeasuredWidth(it, bounds.left to bounds.width)
                    },
                text = title,
                style = MaterialTheme.typography.displayLarge.copy(
                    color = if (currentPage == it) MaterialTheme.colorScheme.primary else gray70
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PolicyType(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RoundChip(
            text = "전체지역"
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stringArrayResource(R.array.categories).forEach {
                RoundChip(
                    text = it
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegionType(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RoundChip(
            text = "전체지역"
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stringArrayResource(R.array.categories).forEach {
                RoundChip(
                    text = it
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Serialized(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "직업/산업",
                style = MaterialTheme.typography.displayLarge
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stringArrayResource(R.array.categories).forEach {
                    RoundChip(
                        text = it
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "취약계층",
                style = MaterialTheme.typography.displayLarge
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stringArrayResource(R.array.weaks).forEach {
                    RoundChip(
                        text = it
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "기타",
                style = MaterialTheme.typography.displayLarge
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stringArrayResource(R.array.etc).forEach {
                    RoundChip(
                        text = it
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "혼인 여부",
                style = MaterialTheme.typography.displayLarge
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stringArrayResource(R.array.marriages).forEach {
                    RoundChip(
                        text = it
                    )
                }
            }
        }
    }
}

@Composable
fun AgeEarn(modifier: Modifier = Modifier) {
    var sliderPosition by remember {
        mutableStateOf(0f..0f)
    }
    var text by remember {
        mutableStateOf("")
    }
    var count by remember {
        mutableStateOf("0")
    }
    val salaries = stringArrayResource(R.array.salaries).toList()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "연소득",
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    text = count,
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            RangeSlider(
                modifier = Modifier.padding(top = 10.dp, bottom = 6.dp),
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                },
                colors = SliderDefaults.colors(
                    thumbColor = gray10,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = gray50
                ),
                steps = 19,
                valueRange = 0f..19.1f,
                onValueChangeFinished = {
                    val start = salaries[ceil(sliderPosition.start.toDouble()).toInt()]
                    val end = salaries[ceil(sliderPosition.endInclusive.toDouble()).toInt()]
                    count = "${if (start != "0") start else ""} ${if (end != "0") "~ $end" else "0"}"
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "0원",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray70
                    )
                )
                Text(
                    text = "2500만원",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray70
                    )
                )
                Text(
                    text = "최대",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray70
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "연령",
                style = MaterialTheme.typography.displayLarge
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "만",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = gray90
                    )
                )

                BasicTextField(
                    modifier = Modifier
                        .padding(start = 6.dp, end = 4.dp)
                        .border(
                            width = 1.dp,
                            color = gray50,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    value = text,
                    onValueChange = { text = it },
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        textAlign = TextAlign.End
                    )
                ) { innerTextField ->
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "20",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = gray60
                                )
                            )
                        }
                        innerTextField()
                    }
                }

                Text(
                    text = "세",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = gray90
                    )
                )
            }
        }
    }
}
