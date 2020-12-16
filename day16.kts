import java.io.File

var processRule = true
var processMyTicket = false
var ruleRe = """([a-z\s]+): ([0-9]+)-([0-9]+) or ([0-9]+)-([0-9]+)""".toRegex()
var rules = mutableListOf<List<String>>()
var myTicket = listOf<Int>()
var tickets = mutableListOf<List<Int>>()

fun processLine(line: String) {
    if (processRule) {
        if (line == "") {
            processRule = false
            processMyTicket = true
        } else {
            var (rule, range1l, range1u, range2l, range2u) = ruleRe.find(line)!!.destructured
            rules.add(listOf(rule, range1l, range1u, range2l, range2u))
        }
    } else if (processMyTicket) {
        if (line == "your ticket:") {
            return
        }
        if (line == "") {
            processMyTicket = false
        } else {
            myTicket = line.split(",").map {it.toInt()}
        }
    } else {
        if (line == "nearby tickets:") {
            return
        }
        tickets.add(line.split(",").map {it.toInt()})
    }
}

File(args[0]).forEachLine {processLine(it)}

fun doesValueMatchRule(value: Int, rule: List<String>): Boolean {
    if (
        (value >= rule[1].toInt() && value <= rule[2].toInt()) ||
        (value >= rule[3].toInt() && value <= rule[4].toInt())) {
        return true
    }
    return false
}

var invalids = mutableListOf<Int>()
var validTickets = mutableListOf<List<Int>>()
for (ticket in tickets) {
    var validTicket = true
    for (value in ticket) {
        var validValue = false
        for (rule in rules) {
            validValue = doesValueMatchRule(value, rule)
            if (validValue) {
                break
            }
        }
        if (!validValue) {
            invalids.add(value)
            validTicket = false
        }
    }
    if (validTicket) {
        validTickets.add(ticket)
    }
}
println(invalids.sum())

var rulePositions = mutableListOf<Set<Int>>()
var allRules = mutableSetOf<Int>()
for (i in 0..rules.size-1) {
    allRules.add(i)
}
for (i in 0..rules.size-1) {
    rulePositions.add(allRules)
}

for (ticket in validTickets) {
    for (v in 0..ticket.size-1) {
        var value = ticket[v]
        var matchingRules = mutableSetOf<Int>()
        for (r in 0..rules.size-1) {
            var rule = rules[r]
            if (doesValueMatchRule(value, rule)) {
                matchingRules.add(r)
            }
        }
        rulePositions[v] = rulePositions[v] intersect matchingRules
    }
}

var boiledDown = false
while (!boiledDown) {
    var rulesToEliminate = mutableSetOf<Int>()
    var allSingles = true
    for (validRules in rulePositions) {
        if (validRules.size == 1) {
            rulesToEliminate.add(validRules.elementAt(0))
        } else {
            allSingles = false
        }
    }
    if (allSingles) {
        boiledDown = true
    } else {
        for (i in 0..rulePositions.size-1) {
            var validRules = rulePositions[i]
            if (validRules.size != 1) {
                rulePositions[i] = rulePositions[i] subtract rulesToEliminate
            }
        }
    }
}

var checkSum = 1L
for (i in 0..rulePositions.size-1) {
    var ruleIdx = rulePositions[i].elementAt(0)
    if (rules[ruleIdx][0].take(9) == "departure") {
        checkSum *= myTicket[i].toLong()
    }
}
println(checkSum)
