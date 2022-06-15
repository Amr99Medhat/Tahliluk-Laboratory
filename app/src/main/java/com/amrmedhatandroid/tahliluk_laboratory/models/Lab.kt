package com.amrmedhatandroid.tahliluk_laboratory.models

import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import java.io.Serializable

class Lab : Serializable {
    var labId: String = Constants.KEY_EMPTY
    var labImage: String = Constants.KEY_EMPTY
    var labName: String = Constants.KEY_EMPTY
    var labPhoneNumber: String = Constants.KEY_EMPTY
    var labPassword: String = Constants.KEY_EMPTY
    var labVerifiedState: String = Constants.KEY_EMPTY
    var labLatitude: String = Constants.KEY_EMPTY
    var labLongitude: String = Constants.KEY_EMPTY
    var address:String=Constants.KEY_EMPTY
}