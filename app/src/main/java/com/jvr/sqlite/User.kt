package com.jvr.sqlite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by OA-JomRafa on 24/02/2020.
 */

@Parcelize
data class User(var id: Int, var name: String?, var age: String?, var username: String?, var password: String?)
    : Parcelable {
        constructor() : this(0, "", "", "", "")
        constructor(a: User) : this(a.id, a.name, a.age, a.username, a.password)
    }