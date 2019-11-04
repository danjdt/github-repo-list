package com.danjdt.githubjavarepos.utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction


/**
 *  @autor danieljdt
 *  @date 2019-11-04
 **/
fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): org.hamcrest.Matcher<View>? {
            return null
        }


        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController, view: View) {
            val v: View = view.findViewById(id)
            v.performClick()
        }
    }
}