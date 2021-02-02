package com.kumsal.kyk.interfaces

import java.util.ArrayList

interface Users<Model> {
    fun getUsers(array: ArrayList<Model>)
}