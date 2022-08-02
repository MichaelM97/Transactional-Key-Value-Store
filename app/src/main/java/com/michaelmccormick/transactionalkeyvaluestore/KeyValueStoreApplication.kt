package com.michaelmccormick.transactionalkeyvaluestore

import android.app.Application

class KeyValueStoreApplication : Application() {
    val appComponent: AppComponent = AppComponent()
}
