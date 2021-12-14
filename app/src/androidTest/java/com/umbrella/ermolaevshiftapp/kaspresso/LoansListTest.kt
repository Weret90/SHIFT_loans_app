package com.umbrella.ermolaevshiftapp.kaspresso

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agoda.kakao.common.utilities.getResourceString
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.kaspresso.screens.AuthorizationScreen
import com.umbrella.ermolaevshiftapp.kaspresso.screens.LoansScreen
import com.umbrella.ermolaevshiftapp.kaspresso.screens.MainScreen
import com.umbrella.ermolaevshiftapp.presentation.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoansListTest : KTestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    @TestCase(name = "Test-3", description = "check screen with loans list")
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

            step("Pass main screen") {
                MainScreen {
                    buttonNavigateToScreenWithLoansList {
                        click()
                    }
                }
            }

            Thread.sleep(5000)

            step("Check loans recycler view content") {
                checkLoansRecyclerView(
                    Loan(
                        String.format(getResourceString(R.string.loan_date), "13.12.21 06:38"),
                        String.format(getResourceString(R.string.loan_status), "REGISTERED"),
                        String.format(getResourceString(R.string.loan_amount_in_item), "2000")
                    ),
                    Loan(
                        String.format(getResourceString(R.string.loan_date), "11.12.21 14:24"),
                        String.format(getResourceString(R.string.loan_status), "REJECTED"),
                        String.format(getResourceString(R.string.loan_amount_in_item), "20000")
                    ),
                    Loan(
                        String.format(getResourceString(R.string.loan_date), "11.12.21 14:13"),
                        String.format(getResourceString(R.string.loan_status), "APPROVED"),
                        String.format(getResourceString(R.string.loan_amount_in_item), "30000")
                    )
                )
            }
        }
    }

    class Loan(val date: String, val status: String, val amount: String)

    private fun checkLoansRecyclerView(vararg loans: Loan) {
        loans.forEachIndexed { index, loan ->
            LoansScreen {
                loansList {
                    childAt<LoansScreen.LoanItem>(index) {
                        amount {
                            isDisplayed()
                            hasText(loan.amount)
                        }

                        status {
                            isDisplayed()
                            hasText(loan.status)
                        }

                        dateOfRegistration {
                            isDisplayed()
                            hasText(loan.date)
                        }
                    }
                }
            }
        }
    }
}