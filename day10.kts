import java.io.File

var input = File(args[0]).readLines().map {num -> num.toInt()}.sorted().toMutableList()

// Add the built-in device adaptor
input.add(input[input.size - 1] + 3)

var jumps = mutableMapOf<Int, Int>()
var joltage = 0

for (adaptor in input) {
    var jump = adaptor - joltage
    if (jump in jumps) {
        jumps[jump] = jumps[jump]!! + 1
    } else {
        jumps[jump] = 1
    }
    joltage = adaptor
}

println(jumps[1]!! * jumps[3]!!)

// For part 2, we assume that all adaptors are 1 or 3 jumps above the previous one,
// though this was not guaranteed by the puzzle rules.

val tribonacci = mapOf(1 to 1, 2 to 2, 3 to 4, 4 to 7, 5 to 13, 6 to 24, 7 to 44)

var combinations = mutableListOf<Long>()
joltage = 0
var offset = 0
var adaptorIdx = 0
for (adaptorIdx in 0..input.size.toInt() - 1) {
    var adaptor = input[adaptorIdx]
    var jump = adaptor - joltage
    if (jump == 3) {
        if (adaptorIdx != 0 && offset + jump != adaptor) {
            combinations.add(tribonacci[input[adaptorIdx - 1] - offset]!!.toLong())
        }
        offset = adaptor
    }
    joltage = adaptor
}
println(combinations.reduce {acc, el -> acc * el})
