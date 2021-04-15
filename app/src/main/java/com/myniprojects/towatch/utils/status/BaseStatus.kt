package com.myniprojects.towatch.utils.status

import com.myniprojects.towatch.utils.helper.Event
import com.myniprojects.towatch.utils.helper.Message

sealed class BaseStatus<out T>
{
    object Sleep : BaseStatus<Nothing>()
    object Loading : BaseStatus<Nothing>()
    data class Success<T>(val data: T) : BaseStatus<T>()
    data class Failed(val errorEvent: Event<Message>) : BaseStatus<Nothing>()
}
