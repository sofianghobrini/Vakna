package com.app.vakna.vue

import android.app.Application
import android.content.Context

class VaknaApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }
}
