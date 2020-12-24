import java.io.File

var blackTiles = mutableSetOf<Pair<Int, Int>>()

fun processLine(line: String) {
    var remainingLine = line
    var x = 0
    var y = 0
    while (remainingLine.length > 0) {
        var direction = remainingLine[0].toString()
        remainingLine = remainingLine.substring(1)
        if (direction == "n" || direction == "s") {
            direction += remainingLine[0].toString()
            remainingLine = remainingLine.substring(1)
        }
        when (direction) {
            "ne" -> x++
            "sw" -> x--
            "e" -> y++
            "w" -> y--
            "se" -> {x--; y++}
            "nw" -> {x++; y--}
        }
    }
    var tile = Pair(x, y)
    if (tile in blackTiles) {
        blackTiles.remove(tile)
    } else {
        blackTiles.add(tile)
    }
}

File(args[0]).forEachLine {processLine(it)}
println(blackTiles.size)

fun getAdjacentTiles(tile: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        Pair(tile.first + 1, tile.second),
        Pair(tile.first, tile.second + 1),
        Pair(tile.first - 1, tile.second + 1),
        Pair(tile.first - 1, tile.second),
        Pair(tile.first, tile.second - 1),
        Pair(tile.first + 1, tile.second - 1)
    )
}

fun countAdjacentBlackTiles(tile: Pair<Int, Int>): Int {
    return getAdjacentTiles(tile).count {it in blackTiles}
}

for (i in 1..100) {
    var newBlackTiles = mutableSetOf<Pair<Int, Int>>()
    for (tile in blackTiles) {
        var ab = countAdjacentBlackTiles(tile)
        if (ab == 1 || ab == 2) {
            newBlackTiles.add(tile)
        }
        for (adjacentTile in getAdjacentTiles(tile)) {
            if (adjacentTile !in blackTiles && countAdjacentBlackTiles(adjacentTile) == 2) {
                newBlackTiles.add(adjacentTile)
            }
        }
    }
    blackTiles = newBlackTiles
}
println(blackTiles.size)
