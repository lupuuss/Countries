package com.github.lupuuss.countries.kotlin

/** Allows to postpone variable usage if it's null */
class SafeVar<T> {

    var value: T? = null
        set(value) {
            field = value

            if (value != null){
                usages.forEach { it(value) }
                usages.clear()
            }
        }

    private val usages: MutableList<(T)->Unit> = mutableListOf()

    fun use(usage: (T)->Unit) {

        if (value != null) {

            usage(value!!)
        } else {

            usages.add(usage)
        }
    }
}