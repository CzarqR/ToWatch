package com.myniprojects.towatch.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myniprojects.towatch.R
import com.myniprojects.towatch.utils.const.PASSWD_MIN_LENGTH
import com.myniprojects.towatch.utils.ext.makeEvent
import com.myniprojects.towatch.utils.helper.Event
import com.myniprojects.towatch.utils.helper.Message
import com.myniprojects.towatch.utils.status.EventMessageStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
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

        val e = email?.trim()
        val p = passwd?.trim()

        if (e.isNullOrBlank()) // empty email
        {
            send(EventMessageStatus.Failed(Event(Message(R.string.empty_email))))
            close()
        }
        else if (p.isNullOrBlank() || p.length < PASSWD_MIN_LENGTH) // too short passwd
        {
            send(
                EventMessageStatus.Failed(
                    Event(
                        Message(
                            R.string.too_short_passwd,
                            listOf(PASSWD_MIN_LENGTH)
                        )
                    )
                )
            )
            close()
        }
        else
        {
            auth.signInWithEmailAndPassword(e, p)
                .addOnSuccessListener {
                    launch {
                        send(EventMessageStatus.Success(Event(Message(R.string.logged_in_correctly))))
                        close()
                    }
                }
                .addOnFailureListener {
                    launch {
                        Timber.d("$it")
                        send(EventMessageStatus.Failed(Event(Message(R.string.wrong_email_or_passwd))))
                        close()
                    }
                }
            awaitClose()
        }
    }

    @ExperimentalCoroutinesApi
    fun registerUser(
        email: String?,
        passwd: String?,
        passwdConf: String?
    ): Flow<EventMessageStatus> = channelFlow {
        send(EventMessageStatus.Loading)

        val e = email?.trim()
        val p = passwd?.trim()
        val pc = passwdConf?.trim()

        if (e.isNullOrBlank()) // empty email
        {
            send(EventMessageStatus.Failed(Event(Message(R.string.empty_email))))
            close()
        }
        else if (p.isNullOrBlank() || p.length < PASSWD_MIN_LENGTH) // too short passwd
        {
            send(
                EventMessageStatus.Failed(
                    Event(
                        Message(
                            R.string.too_short_passwd,
                            listOf(PASSWD_MIN_LENGTH)
                        )
                    )
                )
            )
            close()
        }
        else if (p != pc) //passwords are different
        {
            send(EventMessageStatus.Failed(Event(Message(R.string.diff_passwd))))
            close()
        }
        else // user can be registered
        {
            auth.createUserWithEmailAndPassword(e, p)
                .addOnSuccessListener {
                    Timber.d("Successfully registered")
                    launch {
                        send(EventMessageStatus.Success(Event(Message(R.string.successfully_registered))))
                        close()
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.d("Registration failed")
                    launch {
                        send(
                            EventMessageStatus.Failed(
                                exception.makeEvent
                            )
                        )
                        close()
                    }
                }
        }

        awaitClose()
    }

    // endregion
}