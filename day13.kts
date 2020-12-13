import java.io.File

var f = File(args[0]).bufferedReader()
var earliestTimestamp = f.readLine().toInt()
var busIds = f.readLine().split(",")
f.close()

var nextBusses = busIds.filter {it != "x"}.map {it.toInt()}.map {it to it - earliestTimestamp % it}
var minWait = Float.POSITIVE_INFINITY.toInt()
var busId = 0
for (bus in nextBusses) {
    if (bus.second < minWait) {
        minWait = bus.second
        busId = bus.first
    }
}
println(busId * minWait)

var idWithOffset = mutableListOf<Pair<Long, Long>>()
for (i in 0..busIds.size-1) {
    if (busIds[i] == "x") {
        continue
    }
    var id = busIds[i].toLong()
    idWithOffset.add(Pair(id, Math.floorMod(-i.toLong(), id)))
}
idWithOffset.sortByDescending {it.first}

var currentMod = idWithOffset[0].first
var currentVal = idWithOffset[0].second
for (i in 1..idWithOffset.size-1) {
    var nextMod = idWithOffset[i].first
    var nextVal = idWithOffset[i].second
    while (Math.floorMod(currentVal, nextMod) != nextVal) {
        currentVal += currentMod
    }
    currentMod *= nextMod
}
println(currentVal)
