package com.myniprojects.towatch.utils.status

import com.myniprojects.towatch.utils.helper.Event
import com.myniprojects.towatch.utils.helper.Message

/**
 * class similar to [BaseStatus] but Success result
 * can only be message
 */
sealed class EventMessageStatus
{
    object Sleep : EventMessageStatus()
    object Loading : EventMessageStatus()
    data class Success(val successEvent: Event<Message>) : EventMessageStatus()
    data class Failed(val errorEvent: Event<Message>) : EventMessageStatus()
}
