package com.amrmedhatandroid.tahliluk_laboratory.models

import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import java.io.Serializable

class Lab : Serializable {
    var id: String = Constants.KEY_EMPTY
    var image: String = Constants.KEY_EMPTY
    var analytics: ArrayList<Analytics>? = null
    var labName: String = Constants.KEY_EMPTY
    var phoneNumber: String = Constants.KEY_EMPTY
    var password: String = Constants.KEY_EMPTY
    var labVerifiedState: String = Constants.KEY_EMPTY
    var latitude: String = Constants.KEY_EMPTY
    var longitude: String = Constants.KEY_EMPTY
    var address: String = Constants.KEY_EMPTY
}