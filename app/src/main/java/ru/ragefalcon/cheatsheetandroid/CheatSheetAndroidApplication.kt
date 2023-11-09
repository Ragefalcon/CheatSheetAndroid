package ru.ragefalcon.cheatsheetandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import leakcanary.LogcatSharkLog.Companion.install

@HiltAndroidApp
class CheatSheetAndroidApplication: Application() {
    companion object{
//        lateinit var appComponent: AppComponent
    }
    override fun onCreate() {
        super.onCreate()
//        initializeDagger()
        install()
    }

/*
    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .commonModule(CommonModule(this))
            .build()
    }
*/

}