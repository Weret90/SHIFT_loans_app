package com.umbrella.ermolaevshiftapp.kaspresso

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.kaspresso.screens.AuthorizationScreen
import com.umbrella.ermolaevshiftapp.kaspresso.screens.MainScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.umbrella.ermolaevshiftapp.presentation.MainActivity

@RunWith(AndroidJUnit4::class)
class MainTest : KTestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    @TestCase(name = "Test-1", description = "Check authorization screen")
    fun checkAuthorizationScreenFieldsAndButtons() {
        run {
            step("Check authorization screen input fields and buttons") {
                AuthorizationScreen {
                    loginInput {
                        hasHint(R.string.hint_name_input_field)
                    }
                    passwordInput {
                        hasHint(R.string.hint_password_input_field)
                    }
                    registerButton {
                        hasText(R.string.btn_to_register_text)
                    }
                    enterButton {
                        hasText(R.string.btn_enter_text)
                    }
                }
            }
        }
    }

    @Test
    @TestCase(name = "Test-2", description = "Check main screen")
    fun checkMainScreenViews() {
        run {
            step("Pass authorization screen") {
                AuthorizationScreen {
                    loginInput {
                        typeText("weret")
                    }
                    passwordInput {
                        typeText("1")
                    }
                    enterButton {
                        click()
                    }
                }
            }

            Thread.sleep(5000) //flakySafetyParams.timeoutMs = 5000L почему то не сработал, не успел разобраться потому сделал так

            step("Check welcome text view and buttons") {
                MainScreen {
                    welcomeTextView {
                        hasText("Welcome, weret!")
                        isDisplayed()
                    }
                    buttonCreateLoan {
                        hasText(R.string.button_create_loan)
                        isDisplayed()
                    }
                    buttonNavigateToScreenWithLoansList {
                        hasText(R.string.button_my_loans)
                        isDisplayed()
                    }
                }
            }
        }
    }
}