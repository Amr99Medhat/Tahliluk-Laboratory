package com.amrmedhatandroid.tahliluk_laboratory.models

import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import java.io.Serializable

class Patient : Serializable {
    var id: String = Constants.KEY_EMPTY
    var firstName: String = Constants.KEY_EMPTY
    var lastName: String = Constants.KEY_EMPTY
    var image: String = Constants.KEY_EMPTY
    var password: String = Constants.KEY_EMPTY
    var phoneNumber: String = Constants.KEY_EMPTY
    var fcmToken: String = Constants.KEY_EMPTY
    var gender: String = Constants.KEY_EMPTY
}