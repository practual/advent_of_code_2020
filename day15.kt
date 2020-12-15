import java.io.File

fun getNum(args: Array<String>, target: Int): Int {
    var input = File(args[0]).readLines()[0].split(",").map {it.toInt()}

    var lastSeen = mutableMapOf<Int, Int>()
    for (i in 0..input.size-1) {
        lastSeen[input[i]] = i
    }

    var nextVal = 0 // Assuming input vals are distinct
    var currentVal = 0
    var i = input.size
    while (i <= target-1) {
        currentVal = nextVal
        nextVal = if (currentVal in lastSeen) i - lastSeen[currentVal]!! else 0
        lastSeen[currentVal] = i
        i++
    }
    return currentVal
}

fun main(args: Array<String>) {
    println(getNum(args, 2020))
    println(getNum(args, 30000000))
}
