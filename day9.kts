import java.io.File

import utils.findpair.*

var input = File(args[0]).readLines().map {num -> num.toLong()}

var target = 0L

for (num in 25..input.size - 1) {
    if (findPair(input.subList(num-25, num), input[num]) == null) {
        target = input[num]
        break
    }
}

println(target)

var first = 0
var last = 0
var runningSum = 0L
while (runningSum != target) {
    if (runningSum < target) {
        runningSum += input[last]
        last++
    } else {
        runningSum -= input[first]
        first++
    }
}
var continguousRange = input.subList(first, last)
println(continguousRange.minOrNull()!! + continguousRange.maxOrNull()!!)
