import java.io.File
import utils.binarystringtolong.*

var minId = Float.POSITIVE_INFINITY.toLong()
var maxId = 0L
var seats = mutableSetOf<Long>()

fun processLine(line: String) {
    var row = binaryStringToLong(line.take(7).replace("F", "0").replace("B", "1"))
    var col = binaryStringToLong(line.takeLast(3).replace("L", "0").replace("R", "1"))
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
