package com.aking.openai.util

import okio.ByteString.Companion.decodeBase64


/**
 * Created by Rick on 2023-02-25  15:02.
 * Description:
 */
object Constants {
    const val API_KEY_GPT = "c2stazkyRkVLenNSSlBremdnMnVGU3o="
    const val API_KEY_GPT_APPEND = "MUMwNTUwMUYzMzdFQThCM0UyRTBERDlFOEVFNzREQTI="

    const val DATABASE_CHAT = "room_chat_db"

    fun getAPIKey() = (API_KEY_GPT + API_KEY_GPT_APPEND).decodeBase64().toString()

}