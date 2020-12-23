import java.io.File

fun buildMap(cupList: List<Int>): MutableMap<Int, Int> {
    var cupMap = mutableMapOf<Int, Int>()
    for (i in 0..cupList.size-2) {
        cupMap[cupList[i]] = cupList[i+1]
    }
    cupMap[cupList[cupList.size-1]] = cupList[0]
    return cupMap
}

var cupList = File(args[0]).readLines()[0].map {it.toString().toInt()}.toMutableList()
var maxCup = cupList.maxOrNull()!!
var cups = buildMap(cupList)
var currentCup = cupList[0]

fun shuffle(cups: MutableMap<Int, Int>, currentCup: Int): Int {
    var cupAfterThree = cups[currentCup]!!
    var cupsInThree = mutableListOf<Int>()
    for (i in 1..3) {
        cupsInThree.add(cupAfterThree)
        cupAfterThree = cups[cupAfterThree]!!
    }
    var destinationCup = currentCup - 1
    while (destinationCup in cupsInThree || destinationCup < 1) {
        destinationCup--
        if (destinationCup < 1) {
            destinationCup = maxCup
        }
    }
    cups[cupsInThree[2]] = cups[destinationCup]!!
    cups[destinationCup] = cups[currentCup]!!
    cups[currentCup] = cupAfterThree
    return cupAfterThree
}

for (i in 1..100) {
    currentCup = shuffle(cups, currentCup)
}

var orderAfterOne = ""
var cup = cups[1]
while (cup != 1) {
    orderAfterOne += cup.toString()
    cup = cups[cup]
}
println(orderAfterOne)

cupList = File(args[0]).readLines()[0].map {it.toString().toInt()}.toMutableList()
for (c in cupList.maxOrNull()!!+1..1000000) {
    cupList.add(c)
}
cups = buildMap(cupList)
maxCup = 1000000
currentCup = cupList[0]

for (i in 1..10000000) {
    currentCup = shuffle(cups, currentCup)
}
println(cups[1]!!.toLong() * cups[cups[1]!!]!!.toLong())
