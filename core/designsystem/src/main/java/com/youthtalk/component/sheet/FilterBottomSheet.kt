package com.youthtalk.component.sheet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.EducationType
import com.youthtalk.model.typeenum.EmploymentType
import com.youthtalk.model.typeenum.MarriageType
import com.youthtalk.model.typeenum.Region
import com.youthtalk.model.typeenum.SpecializedType
import com.youthtalk.model.typeenum.toRegionName
import com.youthtalk.util.SpecializedUtils.changeSpecialized
import com.youthtalk.util.SpecializedUtils.etc
import com.youthtalk.util.SpecializedUtils.getAllList
import com.youthtalk.util.SpecializedUtils.isChecked
import com.youthtalk.util.SpecializedUtils.job
import com.youthtalk.util.SpecializedUtils.weeks
import kotlin.math.ceil
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    searchFilter: SearchFilter,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClick: (SearchFilter) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = startIndex) { FilterType.entries.size }
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val types = FilterType.entries.toList()
    var filter by remember {
        mutableStateOf(searchFilter)
    }
    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        sheetState = sheetState,
        containerColor = Color(0xFDFFFFFF),
        contentColor = Color(0xFDFFFFFF),
        onDismissRequest = onDismiss
    ) {
        Header(
            currentPage = pagerState.currentPage,
            types = types,
            lazyListState = lazyListState,
            onClickTab = {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )

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
                FilterType.POLICY_TYPE -> PolicyType(
                    category = filter.category,
                    onClick = { categories ->
                        filter = filter.copy(
                            category = categories
                        )
                    }
                )

                FilterType.REGION -> RegionType(
                    regions = filter.region,
                    onClick = { regions ->
                        filter = filter.copy(
                            region = regions
                        )
                    }
                )

                FilterType.EDUCATION -> {
                    val recruits = EducationType.entries
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        recruits.forEach { recruit ->
                            RoundChip(
                                text = recruit.educationName,
                                isSelected = when (recruit) {
                                    EducationType.UNRESTRICTED -> filter.education == null
                                    else -> filter.education?.contains(recruit) == true
                                },
                                onClick = {
                                    when (recruit) {
                                        EducationType.UNRESTRICTED -> filter = filter.copy(education = null)

                                        else -> {
                                            val newList = getNewList(filter.education ?: listOf(), recruit)
                                            filter = filter.copy(
                                                education = if (newList.isEmpty() || newList.size == recruits.size - 1) null else newList
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                FilterType.RECRUIT -> {
                    val employees = EmploymentType.entries
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        employees.forEach { employee ->
                            RoundChip(
                                text = employee.employmentName,
                                isSelected = when (employee) {
                                    EmploymentType.UNRESTRICTED -> filter.education == null
                                    else -> filter.employment?.contains(employee) == true
                                },
                                onClick = {
                                    when (employee) {
                                        EmploymentType.UNRESTRICTED -> filter = filter.copy(employment = null)

                                        else -> {
                                            val currentList = filter.employment ?: listOf()
                                            val newList = getNewList(currentList, employee)
                                            filter = filter.copy(
                                                employment = if (newList.isEmpty() || newList.size == employees.size - 1) null else newList
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                FilterType.SPECIALIZED -> Serialized(
                    specials = filter.specialization,
                    marriage = filter.marriage,
                    onClickSpecial = { specials ->
                        Timber.e("specials : $specials")
                        filter = filter.copy(
                            specialization = specials
                        )
                    },
                    onClickMarriage = { marriageType ->
                        filter = filter.copy(
                            marriage = marriageType
                        )
                    }
                )

                FilterType.AGE_EARN -> AgeEarn(
                    maxEarn = filter.maxEarn ?: 5000,
                    minEarn = filter.minEarn ?: 0,
                    age = filter.age,
                    onChangeAge = { age -> filter = filter.copy(age = age.ifEmpty { null }) },
                    onChangeEarn = { min, max ->
                        val allRange = min == 0 && max == 5000
                        filter = filter.copy(
                            minEarn = if (allRange) null else min,
                            maxEarn = if (allRange) null else max
                        )
                    }
                )
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
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        filter = searchFilter
                    }
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
                textColor = if (filter == searchFilter) gray70 else gray10,
                backgroundColor = if (filter == searchFilter) gray30 else MaterialTheme.colorScheme.primary,
                onClick = {
                    if (filter != searchFilter) {
                        onClick(filter)
                        onDismiss()
                    }
                }
            )
        }
    }
}

private fun <T> getNewList(currentList: List<T>, value: T) = if (currentList.contains(value)) {
    currentList - value
} else {
    currentList + value
}

@Composable
fun Header(currentPage: Int, types: List<FilterType>, lazyListState: LazyListState, onClickTab: (Int) -> Unit) {
    val tabPositions = remember { mutableStateMapOf<Int, Pair<Float, Float>>() }
    val targetX = tabPositions[currentPage]?.first ?: 0f
    val targetWidth = tabPositions[currentPage]?.second ?: 0f
    val animatedX by animateDpAsState(targetValue = with(LocalDensity.current) { targetX.toDp() })
    val animatedWidth by animateDpAsState(targetValue = with(LocalDensity.current) { targetWidth.toDp() })
    Text(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        text = "필터",
        style = MaterialTheme.typography.titleLarge
    )

    TabRowComponent(
        currentPage = currentPage,
        types = types,
        lazyListState = lazyListState,
        onClickTab = {
            onClickTab(it)
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

        Box(
            modifier = Modifier
                .offset(x = animatedX)
                .width(animatedWidth)
                .height(2.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                .align(Alignment.BottomStart)
        )
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
            val title = types[it].title

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
fun PolicyType(modifier: Modifier = Modifier, category: List<Category>?, onClick: (List<Category>?) -> Unit) {
    val exceptionAllCategories = Category.entries.filter { c -> c != Category.ALL }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RoundChip(
            text = "전체 선택",
            isSelected = category == null,
            onClick = { onClick(null) }
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            exceptionAllCategories.forEach {
                RoundChip(
                    text = it.categoryName.split(" ").first(),
                    isSelected = category?.size != exceptionAllCategories.size &&
                        (category?.contains(it) == true),
                    onClick = {
                        val newCategory = if (category?.contains(it) == true) {
                            category - it
                        } else {
                            (category ?: listOf()) + it
                        }
                        onClick(
                            if (newCategory.isEmpty() || newCategory.size == exceptionAllCategories.size) null else newCategory
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegionType(modifier: Modifier = Modifier, regions: List<String>?, onClick: (List<String>?) -> Unit) {
    val exceptionAllRegion = Region.entries.filter { region -> region != Region.ALL }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RoundChip(
            text = "전체지역",
            isSelected = regions == null,
            onClick = { onClick(null) }
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            exceptionAllRegion.forEach { region ->
                RoundChip(
                    text = region.toRegionName(),
                    isSelected = regions?.contains(region.region) == true,
                    onClick = {
                        val newRegions = if (regions?.contains(region.region) == true) {
                            regions - region.region
                        } else {
                            (regions ?: listOf()) + region.region
                        }
                        onClick(
                            if (newRegions.isEmpty() || newRegions.size == exceptionAllRegion.size) null else newRegions
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Serialized(
    modifier: Modifier = Modifier,
    specials: List<SpecializedType>?,
    marriage: MarriageType?,
    onClickSpecial: (List<SpecializedType>?) -> Unit,
    onClickMarriage: (MarriageType?) -> Unit
) {
    Column(
        modifier = modifier
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
                getAllList(job).forEach { item ->
                    RoundChip(
                        text = item.specialName,
                        isSelected = isChecked(job, item, specials),
                        onClick = {
                            onClickSpecial(changeSpecialized(job, item, specials))
                        }
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
                getAllList(weeks).forEach { item ->
                    RoundChip(
                        text = item.specialName,
                        isSelected = isChecked(weeks, item, specials),
                        onClick = {
                            onClickSpecial(changeSpecialized(weeks, item, specials))
                        }
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
                getAllList(etc).forEach { item ->
                    RoundChip(
                        text = item.specialName,
                        isSelected = isChecked(etc, item, specials),
                        onClick = {
                            onClickSpecial(changeSpecialized(etc, item, specials))
                        }
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
                MarriageType.entries.forEach { type ->
                    RoundChip(
                        text = type.marriageName,
                        isSelected = when (type) {
                            MarriageType.UNRESTRICTED -> marriage == null
                            else -> marriage == type
                        },
                        onClick = {
                            onClickMarriage(
                                when (type) {
                                    MarriageType.UNRESTRICTED -> null
                                    else -> type
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AgeEarn(
    modifier: Modifier = Modifier,
    minEarn: Int,
    maxEarn: Int,
    age: String?,
    onChangeAge: (String) -> Unit,
    onChangeEarn: (Int, Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val money = listOf(
        0, 1200, 1400, 1600,
        1800, 2000, 2100, 2200,
        2300, 2400, 2500, 2750,
        3000, 3250, 3500, 3750,
        4000, 4250, 4500, 4750, 5000
    )
    var sliderPosition by remember {
        mutableStateOf(money.indexOf(minEarn).toFloat()..money.indexOf(maxEarn).toFloat().coerceAtMost(19.1f))
    }
    var start = money[ceil(sliderPosition.start.toDouble()).toInt()]
    var end = money[ceil(sliderPosition.endInclusive.toDouble()).toInt()]
    var count by remember {
        mutableStateOf(getEarnString(start, end))
    }
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
                    start = money[ceil(it.start.toDouble()).toInt()]
                    end = money[ceil(it.endInclusive.toDouble()).toInt()]
                    Timber.e("startIndex :${it.start.toDouble()}, endIndex: ${it.endInclusive.toDouble()}")
                    count = getEarnString(start, end)
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
                    onChangeEarn(money[ceil(sliderPosition.start.toDouble()).toInt()], money[ceil(sliderPosition.endInclusive.toDouble()).toInt()])
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
                    value = age ?: "",
                    onValueChange = onChangeAge,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        textAlign = TextAlign.End
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                ) { innerTextField ->
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (age.isNullOrEmpty()) {
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

fun getEarnString(minEarn: Int, maxEarn: Int): String {
    return if (minEarn == maxEarn) {
        "${maxEarn}만"
    } else {
        "${if (minEarn != 0) "${minEarn}만" else ""} ${if (maxEarn != 0) "~ ${maxEarn}만" else ""}"
    } + if (maxEarn == 5000) " 이상" else ""
}
