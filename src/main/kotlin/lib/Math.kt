package lib

fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if(minOf(a, b) == 0L) maxOf(a, b) else greatestCommonDivisor(b, a % b)
}

fun leastCommonMultiple(vals: List<Long>): Long {
    return vals.fold(1L){lcm, a -> lcm * (a / greatestCommonDivisor(lcm, a))}
}