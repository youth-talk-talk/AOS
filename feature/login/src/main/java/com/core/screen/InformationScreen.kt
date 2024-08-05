package com.core.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.component.DropDownComponent
import com.core.login.LoginViewModel
import com.youth.app.feature.login.R
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun InformationScreen(viewModel: LoginViewModel) {
    LaunchedEffect(Unit) {
    }

    InformationScreen(
        onClickSign = viewModel::postSign,
    )
}

@Composable
fun InformationScreen(onClickSign: (String, String) -> Unit) {
    var location by remember {
        mutableStateOf("전체지역")
    }

    var text by remember {
        mutableStateOf("")
    }

    val onValueChange: (String) -> Unit = { value ->
        if (value.length <= 8) {
            text = value
        }
    }

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InformationTitleScreen()

        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 58.dp)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NickNameScreen(
                text,
                onValueChange,
            )
            LocationSelectScreen(
                stringArrayResource(id = R.array.regions).toList(),
                location,
                dropDownClick = {
                    location = it
                },
            )
        }

        val isEnabled = text.matches("""[가-힣][가-힣\s]*""".toRegex()) && location != "전체지역"

        RoundButton(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .padding(horizontal = 17.dp),
            text = "시작하기",
            color = if (isEnabled) MaterialTheme.colorScheme.primary else Color(0xFFE3E5E5),
            enabled = isEnabled,
            onClick = {
                onClickSign(text, location)
            },
        )
    }
}

@Composable
private fun LocationSelectScreen(regions: List<String>, selectedRegion: String, dropDownClick: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Text(
            modifier = Modifier.padding(start = 17.dp),
            text = "지역 설정",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600),
        )
        DropDownComponent(
            modifier = Modifier.padding(horizontal = 17.dp),
            dropDownList = regions,
            dropDownClick = dropDownClick,
            selectedRegion = selectedRegion,
        )
    }
}

@Composable
private fun NickNameScreen(text: String, onValueChange: (String) -> Unit) {
    Column {
        Text(
            modifier = Modifier.padding(start = 17.dp),
            text = "닉네임 설정",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600),
        )

        Text(
            modifier = Modifier.padding(start = 17.dp),
            text = "원하는 닉네임이 있는 경우 직접 설정 가능해요!(단, 한글8자 이내)",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFA5A5A5)),
        )

        Spacer(modifier = Modifier.height(12.dp))
        BasicTextField(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp)
                .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = Color(0xFFCDCFD0)),
            value = text,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700),
            onValueChange = onValueChange,
            singleLine = true,
        ) { innerTextField ->

            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, top = 10.dp, bottom = 10.dp),
            ) {
                innerTextField()
            }
        }
    }
}

@Composable
private fun InformationTitleScreen() {
    Text(
        modifier =
        Modifier
            .padding(top = 35.dp),
        text = "청년톡톡과 함께할 정보를 입력해주세요",
        style =
        MaterialTheme.typography.displayLarge
            .copy(fontWeight = FontWeight.W700),
    )
}

@Preview
@Composable
private fun InformationScreenPreview() {
    val loginViewModel: LoginViewModel = hiltViewModel()

    YongProjectTheme {
        InformationScreen(
            loginViewModel,
        )
    }
}
