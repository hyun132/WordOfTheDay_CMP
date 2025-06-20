package org.hyun.projectkmp.core.presentation

import org.hyun.projectkmp.core.domain.DataError
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.error_bad_request
import wordoftheday.composeapp.generated.resources.error_disk_full
import wordoftheday.composeapp.generated.resources.error_no_internet
import wordoftheday.composeapp.generated.resources.error_request_timeout
import wordoftheday.composeapp.generated.resources.error_serialization
import wordoftheday.composeapp.generated.resources.error_too_many_requests
import wordoftheday.composeapp.generated.resources.error_unauthorized
import wordoftheday.composeapp.generated.resources.error_unknown

fun DataError.toUiText():UiText{
    val stringRes = when(this){
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.BAD_REQUEST -> Res.string.error_bad_request
        DataError.Remote.UNAUTHORIZED -> Res.string.error_unauthorized
        DataError.Remote.FORBIDDEN -> Res.string.error_unauthorized
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }

    return UiText.StringResourceId(stringRes)
}