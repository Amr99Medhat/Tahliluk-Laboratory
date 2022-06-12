package com.amrmedhatandroid.tahliluk_laboratory.utilities

class Constants {

    companion object {
        const val KEY_COLLECTION_LABS = "labss"
        const val KEY_COLLECTION_PATIENTS = "patients"
        const val KEY_LAB_NAME = "labName"
        const val KEY_LAB_PHONE_NUMBER = "phoneNumber"
        const val KEY_PASSWORD = "password"
        const val KEY_PREFERENCE_NAME = "chatAppPreference"
        const val KEY_IS_SIGNED_IN = "isSignedIn"
        const val KEY_LAB_ID = "labId"
        const val KEY_IMAGE = "image"
        const val KEY_LUNCH_STATE = "lunchState"
        const val KEY_LUNCH_STATE_FIRST_TIME = "firstTimeState"
        const val KEY_LUNCH_STATE_FORGOT_PASSWORD = "forgotPasswordState"
        const val KEY_FCM_TOKEN = "fcmToken"
        const val KEY_PATIENT = "patient"
        const val KEY_COLLECTION_CHAT = "chat"
        const val KEY_SENDER_ID = "senderId"
        const val KEY_RECEIVER_ID = "receiverId"
        const val KEY_MESSAGE = "message"
        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_COLLECTION_CONVERSATIONS = "conversations"
        const val KEY_SENDER_NAME = "senderName"
        const val KEY_RECEIVER_NAME = "receiverName"
        const val KEY_SENDER_IMAGE = "senderImage"
        const val KEY_RECEIVER_IMAGE = "receiverImage"
        const val KEY_LAST_MESSAGE = "lastMessage"
        const val KEY_AVAILABILITY = "availability"
        private const val REMOTE_MSG_AUTHORIZATION = "Authorization"
        private const val REMOTE_MSG_CONTENT_TYPE = "Content-Type"
        const val REMOTE_MSG_DATA = "data"
        const val REMOTE_MSG_REGISTRATION_IDS = "registration_ids"
        const val KEY_APP_LANGUAGE = "appLanguage"
        const val KEY_LANGUAGE_ENGLISH = "en"
        const val KEY_LANGUAGE_ARABIC = "ar"
        const val LOCATION_REQUEST_CODE = 1
        const val KEY_SELECT_LAB_CHAT = "chat"
        const val KEY_APP_DARK_MODE_STATE = "appDarkModeState"
        const val KEY_DARK_MODE = "darkMode"
        const val KEY_LIGHT_MODE = "lightMode"
        const val KEY_CURRENT_LATITUDE = "currentLatitude"
        const val KEY_CURRENT_LONGITUDE = "currentLongitude"
        const val KEY_LOCATION_RESULT = "locationResult"
        const val KEY_LOCATION_RESULT_CODE = 100
        const val KEY_TRUE_RETURN = "true"
        const val KEY_FALSE_RETURN = "false"
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
        const val KEY_LAB_VERIFICATION_STATE = "labVerifiedState"
        const val KEY_LAB_VERIFIED = "Verified"
        const val KEY_LAB_UNVERIFIED = "Unverified"
        const val KEY_STRING = "String"
        const val KEY_EMPTY = ""
        const val KEY_STRING_ZERO = "0"
        const val KEY_STRING_ONE = "1"
        const val KEY_STRING_MINUS_ONE = "-1"
        const val KEY_DATA = "data"
        const val KEY_ZERO = 0
        const val KEY_ONE = 1
        const val KEY_FAILURE = "failure"
        const val KEY_ERROR = "error"
        const val KEY_NOTIFICATION_SENT_SUCCESSFULLY = "Notification sent successfully"
        const val KEY_RESPONSE_FAILURE = "Response failure"
        const val KEY_SEND_MESSAGE_FAILED = "Send message failed"
        const val TAG_CHANGE_PASSWORD = "changePassword"
        const val TAG_CHANGE_PHONE = "changePhone"
        const val KEY_COLLECTION_RESERVATION = "reservations"
        const val Reservation = "reservation"
        const val IMAGE = "image"
        const val LAB_NAME ="labName"
        const val RESERVATION_RESULT= "results"
        const val ORDER_STATE ="orderState"
        const val ORDER_STATE_COMPLETED = "Completed"
        const val LAB_ANALYTICS = "analytics"
        const val ANALYSIS_NAME="analysis_name"
        const val ANALYSIS_PRICE = "analysis_price"


        var remoteMsgHeaders: HashMap<String, String>? = null

        @JvmName("getRemoteMsgHeaders1")
        fun getRemoteMsgHeaders(): HashMap<String, String> {
            if (remoteMsgHeaders == null) {
                remoteMsgHeaders = HashMap()
                remoteMsgHeaders!![REMOTE_MSG_AUTHORIZATION] =
                    "key=AAAARQKYzCc:APA91bHL2FAi1ReNqnYL1uSejyg-iAa7jDghbo6rgMnBf36Mf48lgIJ6gYGZLyMF8GbH45lSt4iVDqds6ho7Lxq2Yevh9-IU023jIJu6muYrTelpL0nAgWYqjAAEsvv1Be2d09SwU7SZ"
                remoteMsgHeaders!![REMOTE_MSG_CONTENT_TYPE] = "application/json"
            }
            return remoteMsgHeaders as HashMap<String, String>
        }

    }
}