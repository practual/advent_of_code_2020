package utils.findpair

fun findPair(list: List<Long>, sum: Long): Pair<Long, Long>? {
    var sorted = list.sorted()
    var a = 0
    var b = sorted.size - 1
    var el1 = 0L
    var el2 = 0L
    var found = false

    while (a < b) {
        el1 = sorted[a]
        el2 = sorted[b]
        if (el1 + el2 == sum) {
            found = true
            break
        } else if (el1 + el2 < sum) {
            a += 1
        } else {
            b -= 1
        }
    }

    return if (found) Pair(el1, el2) else null
}
