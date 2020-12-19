import java.io.File

var conwaySpace = mutableSetOf<Triple<Int, Int, Int>>()

var ranges = mutableListOf(
    Float.POSITIVE_INFINITY.toInt(),
    Float.NEGATIVE_INFINITY.toInt(),
    Float.POSITIVE_INFINITY.toInt(),
    Float.NEGATIVE_INFINITY.toInt(),
    0,
    0
)

fun updateRange(oldVals: MutableList<Int>, newVals: List<Int>): MutableList<Int> {
    for (v in 0..newVals.size-1) {
        if (v % 2 != 0) {
            oldVals[v] = Math.max(oldVals[v], newVals[v])
        } else {
            oldVals[v] = Math.min(oldVals[v], newVals[v])
        }
    }
    return oldVals
}

fun makeCube(vals: List<Int>): Triple<Int, Int, Int> {
    return Triple(vals[0], vals[1], vals[2])
}

fun processLine(i: Int, line: String, extraD: Int) {
    for (j in 0..line.length-1) {
        if (line[j] == "#".single()) {
            var cube = mutableListOf(j, i)
            var newRanges = mutableListOf(j, j, i, i)
            for (d in 1..extraD) {
                cube.addAll(listOf(0))
                newRanges.addAll(listOf(0, 0))
            }
            conwaySpace.add(makeCube(cube))
            ranges = updateRange(ranges, newRanges)
        }
    }
}


File(args[0]).readLines().mapIndexed {i, line -> processLine(i, line, 1)}

fun countNeighbours(x: Int, y: Int, z: Int, space: Set<Triple<Int, Int, Int>>): Int {
    var numNeighbours = 0
    for (i in listOf(x-1, x, x+1)) {
        for (j in listOf(y-1, y, y+1)) {
            for (k in listOf(z-1, z, z+1)) {
                if (i==x && j==y && k==z) {
                    continue
                }
                if (makeCube(listOf(i, j, k)) in space) {
                    numNeighbours++
                }
            }
        }
    }
    return numNeighbours
}

var currentSpace = conwaySpace
var newSpace = mutableSetOf<Triple<Int, Int, Int>>()
for (c in 1..6) {
    for (x in ranges[0]-1..ranges[1]+1) {
        for (y in ranges[2]-1..ranges[3]+1) {
            for (z in ranges[4]-1..ranges[5]+1) {
                var n = countNeighbours(x, y, z, currentSpace)
                var newCube = makeCube(listOf(x, y, z))
                if ((newCube in currentSpace && (n==2 || n==3)) || (newCube !in currentSpace && n==3)) {
                    newSpace.add(Triple(x, y, z))
                    ranges = updateRange(ranges, listOf(x, x, y, y, z, z))
                }
            }
        }
    }
    currentSpace = newSpace
    newSpace = mutableSetOf<Triple<Int, Int, Int>>()
}

println(currentSpace.size)

var conwaySpace4D = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

var ranges4D = mutableListOf(
    Float.POSITIVE_INFINITY.toInt(),
    Float.NEGATIVE_INFINITY.toInt(),
    Float.POSITIVE_INFINITY.toInt(),
    Float.NEGATIVE_INFINITY.toInt(),
    0,
    0,
    0,
    0
)

fun makeCube4D(vals: List<Int>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    return Pair(Pair(vals[0], vals[1]), Pair(vals[2], vals[3]))
}

fun processLine4D(i: Int, line: String, extraD: Int) {
    for (j in 0..line.length-1) {
        if (line[j] == "#".single()) {
            var cube = mutableListOf(j, i)
            var newRanges = mutableListOf(j, j, i, i)
            for (d in 1..extraD) {
                cube.addAll(listOf(0))
                newRanges.addAll(listOf(0, 0))
            }
            conwaySpace4D.add(makeCube4D(cube))
            ranges4D = updateRange(ranges4D, newRanges)
        }
    }
}


File(args[0]).readLines().mapIndexed {i, line -> processLine4D(i, line, 2)}

fun countNeighbours4D(x: Int, y: Int, z: Int, w: Int, space: Set<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
    var numNeighbours = 0
    for (i in listOf(x-1, x, x+1)) {
        for (j in listOf(y-1, y, y+1)) {
            for (k in listOf(z-1, z, z+1)) {
                for (l in listOf(w-1, w, w+1)) {
                    if (i==x && j==y && k==z && l==w) {
                        continue
                    }
                    if (makeCube4D(listOf(i, j, k, l)) in space) {
                        numNeighbours++
                    } 
                }
            }
        }
    }
    return numNeighbours
}

var currentSpace4D = conwaySpace4D
var newSpace4D = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
for (c in 1..6) {
    for (x in ranges4D[0]-1..ranges4D[1]+1) {
        for (y in ranges4D[2]-1..ranges4D[3]+1) {
            for (z in ranges4D[4]-1..ranges4D[5]+1) {
                for (w in ranges4D[6]-1..ranges4D[7]+1) {
                    var n = countNeighbours4D(x, y, z, w, currentSpace4D)
                    var newCube = makeCube4D(listOf(x, y, z, w))
                    if ((newCube in currentSpace4D && (n==2 || n==3)) || (newCube !in currentSpace4D && n==3)) {
                        newSpace4D.add(makeCube4D(listOf(x, y, z, w)))
                        ranges4D = updateRange(ranges4D, listOf(x, x, y, y, z, z, w, w))
                    }
                }
            }
        }
    }
    currentSpace4D = newSpace4D
    newSpace4D = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
}

println(currentSpace4D.size)


