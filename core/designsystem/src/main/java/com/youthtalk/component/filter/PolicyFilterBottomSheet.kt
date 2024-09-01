package com.youthtalk.component.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.component.CategoryButton
import com.youthtalk.component.RoundButton
import com.youthtalk.model.EmploymentCode
import com.youthtalk.model.FilterInfo

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PolicyFilterBottomSheet(
    filterInfo: FilterInfo? = null,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClickEmploy: (EmploymentCode) -> Unit,
    onClickFinished: (Boolean) -> Unit,
    onClickReset: () -> Unit,
    onChangeAge: (String) -> Unit,
    onClickApply: () -> Unit,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(1f),
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = null,
            onDismissRequest = onDismiss,
        ) {
            FilterTopBar(
                onDismiss = onDismiss,
            )
            FilterInfo(
                filterInfo,
                onClickEmploy = onClickEmploy,
                onClickFinished = onClickFinished,
                ageChange = onChangeAge,
            )
            FilterCheckButton(
                onClickReset = onClickReset,
                onClickApply = onClickApply,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun FilterCheckButton(onClickReset: () -> Unit, onClickApply: () -> Unit, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                )
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null,
                ) { onClickReset() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.refresh_icon),
                contentDescription = "새로고침",
            )
            Text(
                text = "초기화",
                style = MaterialTheme.typography.displaySmall,
            )
        }

        RoundButton(
            modifier = Modifier.fillMaxWidth(),
            text = "적용하기",
            color = MaterialTheme.colorScheme.primary,
            onClick = {
                onClickApply()
                onDismiss()
            },
        )
    }
}

@Composable
private fun FilterTopBar(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "상세조건",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp)
                .clickable { onDismiss() },
            painter = painterResource(R.drawable.close_icon),
            contentDescription = "닫기",
            tint = MaterialTheme.colorScheme.onSecondary,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "상세조건은 모든 카테고리에 적용됩니다.",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
    }
}

@Composable
fun ColumnScope.FilterInfo(
    filterInfo: FilterInfo?,
    onClickEmploy: (EmploymentCode) -> Unit,
    onClickFinished: (Boolean) -> Unit,
    ageChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState()),
    ) {
        FilterCategoryAge(
            age = filterInfo?.age,
            onValueChange = ageChange,
        )
        FilterCategoryRecruit(
            filterInfo?.employmentCodeList,
            onClick = onClickEmploy,
        )
        FilterCategoryIsEnd(
            filterInfo?.isFinished,
            onClick = onClickFinished,
        )
    }
}

@Composable
private fun FilterCategoryIsEnd(isFinished: Boolean?, onClick: (Boolean) -> Unit) {
    val finish = isFinished ?: false
    FilterCategoryTitle(
        title = "마감여부",
    )

    Row(
        modifier = Modifier.padding(horizontal = 17.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        CategoryButton(
            modifier = Modifier.weight(1f),
            title = "전체 선택",
            isSelected = !finish,
            onClick = { onClick(false) },
        )
        CategoryButton(
            modifier = Modifier.weight(1f),
            title = "진행중인 정책",
            isSelected = finish,
            onClick = { onClick(true) },
        )
    }
}

@Composable
private fun FilterCategoryRecruit(employmentCodes: List<EmploymentCode>?, onClick: (EmploymentCode) -> Unit) {
    FilterCategoryTitle(
        title = "취업상태",
    )

    val list = stringArrayResource(id = R.array.category)
        .map { name -> EmploymentCode.entries.find { it.employName == name } ?: EmploymentCode.ALL }
    NonlazyGrid(
        modifier = Modifier.padding(
            start = 11.5.dp,
            end = 11.5.dp,
            top = 8.dp,
            bottom = 20.dp,
        ),
        columns = 2,
        itemCount = list.size,
    ) { index ->
        val employmentCode = list[index]
        CategoryButton(
            modifier = Modifier.padding(
                horizontal = 5.5.dp,
                vertical = 4.dp,
            ),
            title = employmentCode.employName,
            isSelected = checkEmploy(index, employmentCodes, employmentCode.employName),
            onClick = {
                onClick(employmentCode)
            },
        )
    }
}

private fun checkEmploy(index: Int, employmentCodes: List<EmploymentCode>?, name: String) = (index == 0 && employmentCodes.isNullOrEmpty()) ||
    (employmentCodes?.any { it.employName == name } ?: false)

@Composable
private fun FilterCategoryAge(age: Int?, onValueChange: (String) -> Unit) {
    FilterCategoryTitle(
        title = "연령",
    )

    Row(
        modifier = Modifier.padding(start = 17.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "만",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onTertiary,
            ),
        )
        BasicTextField(
            value = age?.toString() ?: "",
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Go,
            ),
            textStyle = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Center,
            ),
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .size(width = 140.dp, height = 45.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onTertiary,
                        shape = RoundedCornerShape(10.dp),
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                innerTextField()
            }
        }
        Text(
            text = "세",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onTertiary,
            ),
        )
    }
}

@Composable
fun FilterCategoryTitle(title: String) {
    Row(
        modifier = Modifier
            .padding(
                start = 17.dp,
                top = 24.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "검색아이콘",
            tint = Color.Black,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
