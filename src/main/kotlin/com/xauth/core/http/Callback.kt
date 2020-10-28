package com.xauth.core.http

import com.xauth.core.graphql.GraphQLResponse.ErrorInfo

interface Callback<T> {
    fun onSuccess(result: T)
    fun onFailure(error: ErrorInfo?)
}