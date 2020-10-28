package com.xauth.core.types

class OidcUserParam (
    var accessToken: String
) {
    fun build(): OidcUserParam {
        return this
    }
}
