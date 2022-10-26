package com.android.demo.nlpdemo.resolver

interface IResolver {

    fun resolve(message: String): String?
}