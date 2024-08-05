package com.example.policydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youth.app.feature.policydetail.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50

@Composable
fun PolicyDetail(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(color = Color.White),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SummaryCheck()
        ApplyUser()
        ApplyMethod()
        MoreScreen()
    }
}

@Composable
private fun MoreScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PolicyIconTitle(
            icon = painterResource(id = R.drawable.add_icon),
            title = "더 자세한 정보를 알려주세요",
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "기타 정보",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = gray50,
                ),
            )

            Text(
                text = "기타 정보란",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                ),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "주관기간 및 운영기관",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = gray50,
                ),
            )

            Text(
                text = "-주관 기간: 국토교통부\n-운영기관: 국토교통부",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                ),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "참고 사이트",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = gray50,
                ),
            )

            Text(
                text = "-사업관련 참고 사이트1",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                ),
            )

            Text(
                text = "    ${Typography.middleDot} https://nhuf.molit.go.kr/FP/FP07/FP0701/FP07010301.jsp",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                    lineBreak = LineBreak.Paragraph,
                ),
            )

            Text(
                text = "-사업관련 참고 사이트 2",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                ),
            )
            Text(
                text = "    ${"\u00B7"} https://www.korea.kr/news/policyNewsView.do?newsId=148926066",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                    lineBreak = LineBreak.Paragraph,
                ),
            )
        }
    }
}

@Composable
private fun ApplyMethod() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PolicyIconTitle(
            icon = painterResource(id = R.drawable.question_icon),
            title = "신청방법이 궁금해요",
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "신청절차",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = gray50,
                ),
            )

            Text(
                text = "심사 및 발표",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = gray50,
                ),
            )

            Text(
                text = "신청 사이트",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = gray50,
                ),
            )

            Text(
                text = "제출 서류",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = gray50,
                ),
            )
        }
    }
}

@Composable
private fun ApplyUser() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PolicyIconTitle(
            icon = painterResource(id = R.drawable.person_icon),
            title = "누구를 위한 정책인가요",
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            DetailedConditions(
                title = "연령",
                content = "만 19세 ~ 34세",
            )

            DetailedConditions(
                title = "거주지 및 소득",
                content = "청년 주택드림 청약통장 소득 기준: 연 5,000만원 이하",
            )

            DetailedConditions(
                title = "학력",
                content = "제한없음",
            )

            DetailedConditions(
                title = "전공",
                content = "제한없음",
            )

            DetailedConditions(
                title = "취업상태",
                content = "제한없음",
            )

            DetailedConditions(
                title = "특화분야",
                content = "제한없음",
            )

            DetailedConditions(
                title = "참여제한",
                content = "제한없음",
            )

            DetailedConditions(
                title = "추가조항",
                content = "제한없음",
            )
        }
    }
}

@Composable
private fun SummaryCheck() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PolicyIconTitle(
            icon = painterResource(id = R.drawable.apply_icon),
            title = "한눈에 보는 정책 요약",
        )

        PolicyContent(
            contentTitle = "지원내용",
            contentDetail = "222222222222",
        )

        PolicyContent(
            contentTitle = "신청기간",
            contentDetail = "2024년 02월 21일 ~ 2024년 12월 31일",
        )

        PolicyContent(
            modifier = Modifier.padding(bottom = 9.dp),
            contentTitle = "운영기간",
            contentDetail = "-",
        )
    }
}

@Composable
private fun DetailedConditions(modifier: Modifier = Modifier, title: String, content: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = gray50,
            ),
        )

        Text(
            modifier = Modifier.weight(3f),
            text = content,
            style = MaterialTheme.typography.headlineLarge.copy(
                lineHeight = 20.sp,
            ),
        )
    }
}

@Composable
private fun PolicyIconTitle(modifier: Modifier = Modifier, icon: Painter, title: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = icon,
            contentDescription = "정책 요약 아이콘",
            tint = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun PolicyContent(modifier: Modifier = Modifier, contentTitle: String, contentDetail: String) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = contentTitle,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = gray50,
            ),
        )

        Text(
            text = contentDetail,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview
@Composable
private fun PolicyDetailPreview() {
    YongProjectTheme {
        PolicyDetail()
    }
}
