package com.aking.openai.util

import okio.ByteString.Companion.decodeBase64


/**
 * Created by Rick on 2023-02-25  15:02.
 * Description:
 */
object Constants {
    private const val API_KEY_GPT = "c2stUjVNbVpsbWRnb3N2Nm9Cb0UwUExUM0JsYmtGSlJSbWVKenZrZGNucGNaNXMxdldM"

    const val DATABASE_CHAT = "room_chat_db"

    fun getAPIKey() = API_KEY_GPT.decodeBase64()?.utf8()

}