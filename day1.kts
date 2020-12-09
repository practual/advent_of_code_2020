import java.io.File

import utils.findpair.*

var input = File(args[0]).readLines().map {num -> num.toLong()}

var pairThatSum = findPair(input, 2020L)
println(pairThatSum!!.first * pairThatSum!!.second)

val numSet = input.toSet()
var el1 = 0L
var el2 = 0L
var el3 = 0L
var rem = 0L
for (i in 0 .. input.size - 1) {
    for (j in i + 1 .. input.size - 1) {
        el1 = input[i]
        el2 = input[j]
        rem = 2020L - el1 - el2
        if (rem in numSet) {
            // Assuming unique values
            el3 = rem
            break
        }
    }
    if (el3 > 0) {
        break
    }
}
println(el1 * el2 * el3)
