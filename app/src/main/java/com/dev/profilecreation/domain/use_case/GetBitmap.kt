package com.dev.profilecreation.domain.use_case

import android.graphics.BitmapFactory
import javax.inject.Inject

class GetBitmap @Inject constructor() {
    operator fun invoke(path: String) = try {
        BitmapFactory.decodeFile(path)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}
