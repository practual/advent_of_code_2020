import java.io.File

var idRe = """[0-9]+""".toRegex()

var id = 0
var tiles = mutableMapOf<Int, List<String>>()
var tile = mutableListOf<String>()
fun processLine(line: String) {
    var idMatch = idRe.find(line)
    if (idMatch != null) {
        id = idMatch.value.toInt()
    } else if (line == "") {
        tiles[id] = tile
        tile = mutableListOf<String>()
    } else {
        tile.add(line)
    }
}

File(args[0]).forEachLine {processLine(it)}

fun getEdges(tile: List<String>): List<String> {
    var edges = mutableListOf<String>()
    edges.add(tile[0])
    edges.add(tile[0].reversed())
    edges.add(tile[tile.size-1])
    edges.add(tile[tile.size-1].reversed())
    var left = ""
    var right = ""
    for (row in tile) {
        left += row[0].toString()
        right += row[row.length-1].toString()
    }
    edges.add(left)
    edges.add(left.reversed())
    edges.add(right)
    edges.add(right.reversed())
    return edges
}

var edgeMap = mutableMapOf<String, Set<Int>>()
var connections = mutableMapOf<Int, Set<Pair<Int, String>>>()
for (tile in tiles) {
    connections[tile.key] = mutableSetOf<Pair<Int, String>>()
    for (edge in getEdges(tile.value)) {
        if (edge !in edgeMap) {
            edgeMap[edge] = mutableSetOf(tile.key)
        } else {
            for (connectedTile in edgeMap[edge]!!) {
                connections[tile.key] = connections[tile.key]!! union setOf(Pair(connectedTile, edge))
                connections[connectedTile] = connections[connectedTile]!! union setOf(Pair(tile.key, edge))
            }
            edgeMap[edge] = edgeMap[edge]!! union setOf(tile.key)
        }
    }
}

var corners = mutableListOf<Int>()
for (tile in connections) {
    if (tile.value.size == 4) {
        corners.add(tile.key)
    }
}
println(corners.fold(1L) {acc, sum -> acc * sum.toLong()})

fun getOppositeEdge(tile: List<String>, edge: String): String {
    var tileEdges = getEdges(tile)
    var edgeIndex = 0
    for (i in 0..tileEdges.size-1) {
        if (tileEdges[i] == edge) {
            edgeIndex = i
            break
        }
    }
    return when (edgeIndex) {
        0 -> tileEdges[2]
        1 -> tileEdges[3]
        2 -> tileEdges[0]
        3 -> tileEdges[1]
        4 -> tileEdges[6]
        5 -> tileEdges[7]
        6 -> tileEdges[4]
        7 -> tileEdges[5]
        else -> ""
    }
}

fun orientate(tile: List<String>, edge: String, edgeId: Int): Int {
    // edgeId: 1 -> right, 2 -> bottom
    // orientationIds:
    //     0 -> no change
    //     1 -> rotate right once
    //     2 -> rotate right twice
    //     3 -> rotate right thrice
    //     4 -> flip (horizontally)
    //     5 -> flip + rotate right once
    //     6 -> flip + rotate right twice
    //     7 -> flip + rotate right thrice
    var edges = getEdges(tile)
    for (i in 0..edges.size-1) {
        if (edges[i] == edge) {
            if (i == 0 && edgeId == 1) {
                return 1
            } else if (i == 0 && edgeId == 2) {
                return 6
            } else if (i == 1 && edgeId == 1) {
                return 5
            } else if (i == 1 && edgeId == 2) {
                return 2
            } else if (i == 2 && edgeId == 1) {
                return 7
            } else if (i == 2 && edgeId == 2) {
                return 0
            } else if (i == 3 && edgeId == 1) {
                return 3
            } else if (i == 3 && edgeId == 2) {
                return 4
            } else if (i == 4 && edgeId == 1) {
                return 4
            } else if (i == 4 && edgeId == 2) {
                return 3
            } else if (i == 5 && edgeId == 1) {
                return 2
            } else if (i == 5 && edgeId == 2) {
                return 5
            } else if (i == 6 && edgeId == 1) {
                return 0
            } else if (i == 6 && edgeId == 2) {
                return 7
            } else if (i == 7 && edgeId == 1) {
                return 6
            } else if (i == 7 && edgeId == 2) {
                return 1
            }
        }
    }
    return -1
}

var topLeftId = corners[0]
var topLeftTile = tiles[topLeftId]!!
var imageIds = mutableListOf(mutableListOf(topLeftId))
var placedTiles = mutableSetOf(topLeftId)

// Arbitrarily choose a connected tile as 'to the right' and the other as 'to the bottom'
var rightConn = connections[corners[0]]!!.elementAt(0)
var orientations = mutableMapOf(topLeftId to orientate(topLeftTile, rightConn.second, 1))
var bottomConn = rightConn
for (connectedTile in connections[topLeftId]!!) {
    if (connectedTile.first != rightConn.first) {
        bottomConn = connectedTile
        // Check for consistent orientations.
        if (orientate(topLeftTile, bottomConn.second, 2) == orientations[topLeftId]) {
            break
        }
    }
}

var row = 0
var hasBottom = true
while (hasBottom) {
    if (row != 0) {
        var tileId = bottomConn.first
        var tile = tiles[tileId]!!
        var bottomEdge = getOppositeEdge(tile, bottomConn.second)
        imageIds.add(mutableListOf(tileId))
        placedTiles.add(tileId)
        orientations[tileId] = orientate(tile, bottomEdge, 2)
        hasBottom = false
        for (connectedTile in connections[tileId]!!) {
            if (connectedTile.second == bottomEdge) {
                hasBottom = true
                bottomConn = connectedTile
            }
        }
        for (connectedTile in connections[tileId]!!) {
            if (connectedTile.first !in placedTiles &&
                connectedTile.first != bottomConn.first &&
                orientate(tile, connectedTile.second, 1) == orientations[tileId]) {
                rightConn = connectedTile
            }
        }
    }
    var hasRight = true
    while (hasRight) {
        var tileId = rightConn.first
        var tile = tiles[tileId]!!
        var rightEdge = getOppositeEdge(tile, rightConn.second)
        imageIds[row].add(tileId)
        placedTiles.add(tileId)
        orientations[tileId] = orientate(tile, rightEdge, 1)
        hasRight = false
        for (connectedTile in connections[tileId]!!) {
            if (connectedTile.second == rightEdge) {
                hasRight = true
                rightConn = connectedTile
            }
        }
    }
    row++
}

fun reverseTile(tile: List<String>): List<String> {
    var newTile = mutableListOf<String>()
    for (r in 0..tile.size-1) {
        newTile.add(tile[r].reversed())
    }
    return newTile
}

fun rotateTile(tile: List<String>): List<String> {
    var newTile = mutableListOf<String>()
    for (r in 0..tile.size-1) {
        var row = ""
        for (c in 0..tile.size-1) {
            row += tile[tile.size-1-c][r].toString()
        }
        newTile.add(row)
    }
    return newTile
}

fun performOrientation(tile: List<String>, orientationId: Int): List<String> {
    var newTile = tile
    if (orientationId >= 4) {
        newTile = reverseTile(newTile)
    }
    var numRotations = Math.floorMod(orientationId, 4)
    for (r in 1..numRotations) {
        newTile = rotateTile(newTile)
    }
    return newTile
}

var finalImage = mutableListOf<String>()
for (ir in 0..imageIds.size-1) {
    for (tileId in imageIds[ir]) {
        var orientatedTile = performOrientation(tiles[tileId]!!, orientations[tileId]!!)
        for (r in 1..orientatedTile.size-2) {
            var imageRowNum = (orientatedTile.size - 2) * ir + r - 1
            var imageStr = orientatedTile[r].substring(1, orientatedTile[r].length-1)
            if (finalImage.size <= imageRowNum) {
                finalImage.add(imageStr)
            } else {
                finalImage[imageRowNum] += imageStr
            }
        }
    }
}

var flatImage = finalImage.joinToString("")
// Given a 96x96 image.
var monsterRe = """(?=.{18}#.{77}#.{4}##.{4}##.{4}###.{77}#.{2}#.{2}#.{2}#.{2}#.{2}#.{3})""".toRegex()
for (o in 0..8) {
    var flatImage = performOrientation(finalImage, o).joinToString("")
    var numMatches = 0
    for (match in monsterRe.findAll(flatImage)) {
        numMatches++
    }
    if (numMatches != 0) {
        var monsterSpaces = numMatches * 15
        var occupied = flatImage.count {it == '#'}
        println(occupied - monsterSpaces)
        break
    }
}
