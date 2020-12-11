import java.io.File

var input = mutableListOf<List<Char>>()
File(args[0]).forEachLine {input.add(it.toList())}

var ySize = input.size
var xSize = input[0].size

var seatMap = input.flatten().toMutableList()

val occupied = "#".single()
val empty = "L".single()
val floor = ".".single()

fun countAdjacentOccupied(seatNum: Int): Int {
    val isFirstRow = seatNum < xSize
    val isLastRow = seatNum > seatMap.size - xSize - 1
    val isFirstCol = seatNum % xSize == 0
    val isLastCol = (seatNum + 1) % xSize == 0
    var count = 0
    if (!isFirstRow && !isFirstCol && seatMap[seatNum - xSize - 1] == occupied) {
        count++
    }
    if (!isFirstRow && seatMap[seatNum - xSize] == occupied) {
        count++
    }
    if (!isFirstRow && !isLastCol && seatMap[seatNum - xSize + 1] == occupied) {
        count++
    }
    if (!isFirstCol && seatMap[seatNum - 1] == occupied) {
        count++
    }
    if (!isLastCol && seatMap[seatNum + 1] == occupied) {
        count++
    }
    if (!isLastRow && !isFirstCol && seatMap[seatNum + xSize - 1] == occupied) {
        count++
    }
    if (!isLastRow && seatMap[seatNum + xSize] == occupied) {
        count++
    }
    if (!isLastRow && !isLastCol && seatMap[seatNum + xSize + 1] == occupied) {
        count++
    }
    return count
}

fun updateSeats(): Boolean {
    var seatsToFlip = mutableListOf<Int>()
    for (seatNum in 0..seatMap.size-1) {
        if (seatMap[seatNum] == floor) {
            continue
        }
        var count = countAdjacentOccupied(seatNum)
        if (seatMap[seatNum] == empty && count == 0) {
            seatsToFlip.add(seatNum)
        } else if (seatMap[seatNum] == occupied && count >= 4) {
            seatsToFlip.add(seatNum)
        }
    }
    for (seatToFlip in seatsToFlip) {
        seatMap[seatToFlip] = if (seatMap[seatToFlip] == occupied) empty else occupied
    }
    return seatsToFlip.size > 0
}

while (updateSeats()) {}
println(seatMap.count {it == occupied})

var seatMapPart2 = input.flatten().toMutableList()

fun look(seatNum: Int, x: Int, y: Int): Char {
    val isFirstRow = seatNum < xSize
    val isLastRow = seatNum > seatMapPart2.size - xSize - 1
    val isFirstCol = seatNum % xSize == 0
    val isLastCol = (seatNum + 1) % xSize == 0

    if ((x == -1 && isFirstCol) || (x == 1 && isLastCol) || (y == -1 && isFirstRow) || (y == 1 && isLastRow)) {
        return floor
    }

    val nextSeatNum = seatNum + y * xSize + x

    if (seatMapPart2[nextSeatNum] != floor) {
        return seatMapPart2[nextSeatNum]
    }

    return look(nextSeatNum, x, y)
}

fun countVisibleOccupied(seatNum: Int): Int {
    var count = 0
    if (look(seatNum, -1, -1) == occupied) {
        count++
    }
    if (look(seatNum, 0, -1) == occupied) {
        count++
    }
    if (look(seatNum, 1, -1) == occupied) {
        count++
    }
    if (look(seatNum, -1, 0) == occupied) {
        count++
    }
    if (look(seatNum, 1, 0) == occupied) {
        count++
    }
    if (look(seatNum, -1, 1) == occupied) {
        count++
    }
    if (look(seatNum, 0, 1) == occupied) {
        count++
    }
    if (look(seatNum, 1, 1) == occupied) {
        count++
    }
    return count
}

fun updateSeatsPart2(): Boolean {
    var seatsToFlip = mutableListOf<Int>()
    for (seatNum in 0..seatMapPart2.size-1) {
        if (seatMapPart2[seatNum] == floor) {
            continue
        }
        var count = countVisibleOccupied(seatNum)
        if (seatMapPart2[seatNum] == empty && count == 0) {
            seatsToFlip.add(seatNum)
        } else if (seatMapPart2[seatNum] == occupied && count >= 5) {
            seatsToFlip.add(seatNum)
        }
    }
    for (seatToFlip in seatsToFlip) {
        seatMapPart2[seatToFlip] = if (seatMapPart2[seatToFlip] == occupied) empty else occupied
    }
    return seatsToFlip.size > 0
}

while (updateSeatsPart2()) {}
println(seatMapPart2.count {it == occupied})
