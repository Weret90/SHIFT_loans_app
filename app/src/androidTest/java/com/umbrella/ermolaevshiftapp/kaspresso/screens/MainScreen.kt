package com.umbrella.ermolaevshiftapp.kaspresso.screens

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.umbrella.ermolaevshiftapp.R


object MainScreen : Screen<MainScreen>() {

    val welcomeTextView = KTextView { withId(R.id.welcome_text_view) }
    val buttonCreateLoan = KButton { withId(R.id.button_create_loan) }
    val buttonNavigateToScreenWithLoansList = KButton { withId(R.id.button_my_loans) }

}