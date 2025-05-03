package com.feature.policydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feature.policydetail.screen.isMeasure
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray90

fun LazyListScope.policyContent(
    screenHeight: Dp,
    animatedHeight: Dp,
    contentHeight: Dp,
    isExpanded: Boolean,
    measuredOnce: Boolean,
    measureHeight: (Int) -> Unit,
    onClickExpanded: () -> Unit,
) {
    item {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .isMeasure(measuredOnce, animatedHeight)
                    .onGloballyPositioned { coordinates ->
                        measureHeight(coordinates.size.height)
                    }
                    .padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                PolicySpecContent(
                    title = "신청자격",
                    contents = listOf(
                        "만 15세~만 75세",
                        "전국",
                        "참여 제한 대상 참고",
                        "현직 공무원, 사립학교 교직원, 졸업예정자 이외 재학생, 연 매출 4억원 이상의 자영업자, 월 임금 300만원 이상인 대규모기업종사자(45세 미만)," +
                            " 월 소득 500만원 이상의 특수형태근로종사자 등 국민내일배움카드 발급이 불가능한 자는 제외",
                    ),
                )

                PolicySpecContent(
                    title = "지원내용",
                    contents = listOf(
                        "□ 국민내일배움카드 훈련비 지원 한도(5년간 300~500만원) 외 50만원(지급 후 1년 한도)의 크레딧 추가 지급\n" +
                            "※ 추가 지급된 크레딧은 정해진 K-디지털 기초역량훈련(K-Digital Credit) 훈련과정 수강에만 사용 가능하며, 훈련생 개인은 훈련비의 10퍼센트를 부담해야 함",
                    ),
                )

                PolicySpecContent(
                    title = "신청방법",
                    contents = listOf(
                        "국민내일배움카드 발급 후 고용센터 방문 또는 고용24 홈페이지(www.work24.go.kr)를 통해 수강신청",
                        "국민내일배움카드 발급 신청 - 고용센터 심사 후 카드 발급 - 훈련 과정 수강신청- 훈련 기관 훈련생 선발 후 개별 안내",
                        "https://www.work24.go.kr",
                    ),
                )
            }

            if (!isExpanded && contentHeight > screenHeight * 0.4f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White),
                                startY = 0f,
                                endY = with(LocalDensity.current) { 20.dp.toPx() },
                            ),
                        ),
                )
            }
        }

        if (contentHeight > screenHeight * 0.4f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 30.dp, top = 20.dp)
                    .border(
                        width = 1.dp,
                        color = gray40,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        onClickExpanded()
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (isExpanded) "접기" else "공고 상세보기",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30,
        )
    }
}

@Composable
fun PolicySpecContent(modifier: Modifier = Modifier, title: String, contents: List<String>) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            contents.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = gray90,
                    ),
                )
            }
        }
    }
}
