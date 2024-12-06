package lib

import kotlin.math.*

fun Int.wrapAt(limit: Int): Int {
    return if (this > -1) {
        this % limit
    } else {
        limit + (this % limit)
    }
}

fun Long.wrapAt(limit: Int): Int {
    return if (this > -1) {
        (this % limit.toLong()).toInt()
    } else {
        (limit + (this % limit.toLong())).toInt()
    }
}

fun Int.pow(p: Double): Double {
    return this.toDouble().pow(p)
}

fun Int.pow(p: Int): Double {
    return this.toDouble().pow(p)
}

fun Int.log(b: Double): Double {
    return log(this.toDouble(), b)
}

fun Long.log(b: Double): Double {
    return log(this.toDouble(), b)
}