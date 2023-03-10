package com.txznet.sdk;

import com.txznet.sdk.ChatCallback;
import com.aking.aichat.database.entity.OwnerWithChats;
import com.aking.aichat.model.bean.GptText;

interface ChatInterface {

    oneway void sendMessage(String message);

    List<GptText> getOwnerWithChats();

    List<OwnerWithChats> getChatsByOwnerId();

    boolean registerCallback(in ChatCallback callback);

    boolean unregisterCallback(in ChatCallback callback);

}