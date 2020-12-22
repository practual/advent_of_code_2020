import java.io.File

var rules = mutableMapOf<Int, String>()
var inputs = mutableListOf<String>()

var processingRules = true
var rulesRe = """([0-9]+): (.+)""".toRegex()
fun processLine(line: String) {
    if (line == "") {
        processingRules = false
        return
    }
    if (processingRules) {
        var (ruleNum, ruleText) = rulesRe.find(line)!!.destructured
        rules[ruleNum.toInt()] = ruleText
    } else {
        inputs.add(line)
    }
}

File(args[0]).forEachLine {processLine(it)}

fun matchAll(input: String, ruleText: String): List<String> {
    var matches = mutableListOf("")
    for (rule in ruleText.split(" ")) {
        var newMatches = mutableListOf<String>()
        for (prefix in matches) {
            var remaining = input.substring(prefix.length)
            for (match in checkRule(remaining, rule.toInt())) {
                newMatches.add(prefix + match)
            }
        }
        if (newMatches.size == 0) {
            return listOf<String>()
        }
        matches = newMatches
    }
    return matches
}

fun checkRule(input: String, ruleNum: Int): List<String> {
    var ruleText = rules[ruleNum]
    if (input == "") {
        return listOf<String>()
    }
    if (ruleText == "\"a\"" || ruleText == "\"b\"") {
        if (input[0] == ruleText.substring(1, 2).single()) {
            return listOf(input[0].toString())
        }
        return listOf<String>()
    }
    if ('|' in ruleText!!) {
        var matches = mutableListOf<String>()
        var twoHalves = ruleText.split("|").map {it.trim {it == ' '}}
        matches.addAll(matchAll(input, twoHalves[0]))
        matches.addAll(matchAll(input, twoHalves[1]))
        return matches
    }
    return matchAll(input, ruleText)
}

fun checkRuleMatchesFullInput(input: String, ruleNum: Int): Boolean {
    for (prefix in checkRule(input, ruleNum)) {
        if (prefix == input) {
            return true
        }
    }
    return false
}

println(inputs.count {checkRuleMatchesFullInput(it, 0)})

rules[8] = "42 | 42 8"
rules[11] = "42 31 | 42 11 31"
println(inputs.count {checkRuleMatchesFullInput(it, 0)})
