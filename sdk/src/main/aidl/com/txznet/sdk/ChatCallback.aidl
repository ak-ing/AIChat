package com.txznet.sdk;

import com.aking.aichat.model.bean.GptResponse;
import com.aking.aichat.model.bean.Message;

interface ChatCallback {
    oneway void onAIReplies(in GptResponse<Message> pgtResponse);
}