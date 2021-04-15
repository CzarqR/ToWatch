package com.myniprojects.towatch.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myniprojects.towatch.R
import com.myniprojects.towatch.utils.helper.Event
import com.myniprojects.towatch.utils.helper.Message
import com.myniprojects.towatch.utils.status.EventMessageStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor()
{
    // region logged user

    private val auth = Firebase.auth

    private val _loggedUser = MutableStateFlow(auth.currentUser)
    val loggedUser = _loggedUser.asStateFlow()

    /**
     * probably this will newer throw nullPointerException in [com.myniprojects.towatch.ui.activities.MainActivity]
     * when [_loggedUser] becomes null, [com.myniprojects.towatch.ui.activities.MainActivity] should be closed
     */
    val requireUser: FirebaseUser
        get() = _loggedUser.value!!

    init
    {
        auth.addAuthStateListener {
            Timber.d("Auth state changed to $it")
            _loggedUser.value = it.currentUser
        }
    }

    // endregion


    // region sign in/register

    @ExperimentalCoroutinesApi
    fun loginUser(email: String?, passwd: String?): Flow<EventMessageStatus> = channelFlow {
        send(EventMessageStatus.Loading)
    }

    @ExperimentalCoroutinesApi
    fun registerUser(
        email: String?,
        passwd: String?,
        passwdConf: String?
    ): Flow<EventMessageStatus> = channelFlow {
        send(EventMessageStatus.Failed(Event(Message(R.string.app_name))))
    }

    // endregion
}