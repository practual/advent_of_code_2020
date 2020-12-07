import java.io.File

var containerRe = """([a-z\s]+) bags contain (.*)""".toRegex()
var contentsRe = """([0-9]+) ([a-z\s]+) bag""".toRegex()
var bagMapOut: MutableMap<String, MutableList<String>> = mutableMapOf()
var bagMapIn: MutableMap<String, MutableList<Pair<Int, String>>> = mutableMapOf()

var canContainGold = mutableSetOf<String>()
var numContainedBags = 0

fun processLine(line: String) {
    val containerRuleMatch = containerRe.find(line)
    val container = containerRuleMatch!!.groups[1]!!.value
    val contentsRuleMatches = contentsRe.findAll(containerRuleMatch.groups[2]!!.value)
    for (contentMatch in contentsRuleMatches) {
        var numberOfBags = contentMatch.groups[1]!!.value.toInt()
        var containedBag = contentMatch.groups[2]!!.value

        if (!bagMapOut.contains(containedBag)) {
            bagMapOut[containedBag] = mutableListOf(container)
        } else {
            bagMapOut[containedBag]!!.add(container)
        }

        if (!bagMapIn.contains(container)) {
            bagMapIn[container] = mutableListOf(Pair(numberOfBags, containedBag))
        } else {
            bagMapIn[container]!!.add(Pair(numberOfBags, containedBag))
        }
    }
}

fun getContainers(target: String) {
    if (!bagMapOut.contains(target)) {
        return
    }
    for (container in bagMapOut[target]!!) {
        canContainGold.add(container)
        getContainers(container)
    }
}

fun countBags(target: String, multiplier: Int) {
    if (!bagMapIn.contains(target)) {
        return
    }
    for (contents in bagMapIn[target]!!) {
        numContainedBags += contents.first * multiplier
        countBags(contents.second, contents.first * multiplier)
    }
}

File(args[0]).forEachLine {processLine(it)}

getContainers("shiny gold")
println(canContainGold.size)

countBags("shiny gold", 1)
println(numContainedBags)
