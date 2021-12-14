package com.umbrella.ermolaevshiftapp.kaspresso.screens

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.umbrella.ermolaevshiftapp.R

object AuthorizationScreen : Screen<AuthorizationScreen>() {

    val loginInput = KEditText { withId(R.id.name_input_field) }
    val passwordInput = KEditText { withId(R.id.password_input_field) }
    val registerButton = KButton { withId(R.id.button_to_registration_fragment) }
    val enterButton = KButton { withId(R.id.button_enter) }
}