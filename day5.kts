import java.io.File

var minId = Float.POSITIVE_INFINITY.toInt()
var maxId = 0
var seats = mutableSetOf<Int>()

fun binaryStringToInt(binString: String): Int {
    var int = 0
    var exp = binString.length - 1
    for (digit in binString) {
        if (digit == "1".single()) {
            int += Math.pow(2.0, exp.toDouble()).toInt()
        }
        exp -= 1
    }
    return int
}

fun processLine(line: String) {
    var row = binaryStringToInt(line.take(7).replace("F", "0").replace("B", "1"))
    var col = binaryStringToInt(line.takeLast(3).replace("L", "0").replace("R", "1"))
    var seatId = row * 8 + col
    minId = Math.min(minId, seatId)
    maxId = Math.max(maxId, seatId)
    seats.add(seatId)
}

File(args[0]).forEachLine {processLine(it)}
println(maxId)

for (seat in minId..maxId) {
    if (seat !in seats) {
        println(seat)
        break
    }
}
