package com.umbrella.ermolaevshiftapp.kaspresso.screens

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.umbrella.ermolaevshiftapp.R
import org.hamcrest.Matcher

object LoansScreen : Screen<LoansScreen>() {

    val loansList = KRecyclerView(
        builder = { withId(R.id.loans_rv) },
        itemTypeBuilder = { itemType(::LoanItem) }
    )

    class LoanItem(parent: Matcher<View>) : KRecyclerItem<LoanItem>(parent) {

        val dateOfRegistration = KTextView(parent) { withId(R.id.loan_date) }
        val amount = KTextView(parent) { withId(R.id.loan_amount) }
        val status = KTextView(parent) { withId(R.id.loan_status) }
    }
}