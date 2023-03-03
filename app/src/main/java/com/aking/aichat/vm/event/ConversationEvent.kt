package com.aking.aichat.vm.event

import com.aking.aichat.database.entity.OwnerWithChats

/**
 * Created by Rick on 2023-03-03  16:10.
 * Description: 会话EventBus
 */
class ConversationEvent(val message: OwnerWithChats)