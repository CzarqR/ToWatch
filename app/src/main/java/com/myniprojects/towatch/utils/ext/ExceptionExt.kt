package com.myniprojects.towatch.utils.ext

import com.myniprojects.towatch.R
import com.myniprojects.towatch.utils.helper.Event
import com.myniprojects.towatch.utils.helper.Message

val Exception.requireMessage: String
    get() = localizedMessage ?: message ?: ""

val Exception.makeEvent: Event<Message>
    get() = Event(Message(R.string.something_went_wrong, listOf(requireMessage)))