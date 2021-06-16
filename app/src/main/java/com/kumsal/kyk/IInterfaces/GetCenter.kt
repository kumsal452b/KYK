package com.kumsal.kyk.IInterfaces

import java.util.ArrayList

interface GetCenter<Model> {
    fun getUsers(blocked: ArrayList<Model>,blocker:ArrayList<Model>,currentUserLikeList:ArrayList<Model>)
}