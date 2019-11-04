package com.danjdt.githubjavarepos

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 *  @autor danieljdt
 *  @date 2019-11-04
 **/

class TestApp : Application()

class TestAppJUnitRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}
