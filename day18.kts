import java.io.File

var expressionRe = """\([^()]+\)""".toRegex()
var operatorRe = """([0-9]+) (\+|\*) ([0-9]+)""".toRegex()
var additionRe = """([0-9]+) (\+) ([0-9]+)""".toRegex()
var multiplicationRe = """([0-9]+) (\*) ([0-9]+)""".toRegex()

var expressionSum = 0L

fun processExpression(expr: String, additionFirst: Boolean): String {
    var currRe = if (additionFirst) additionRe else operatorRe
    var currExpr = expr
    var result = currRe.find(currExpr)
    if (result == null && additionFirst) {
        currRe = multiplicationRe
        result = currRe.find(currExpr)
    }
    while (result != null) {
        var (operand1, operator, operand2) = result.destructured
        var value = if (operator == "*") operand1.toLong() * operand2.toLong() else operand1.toLong() + operand2.toLong()
        currExpr = currExpr.substring(0, result.range.first) + value.toString() + currExpr.substring(result.range.last+1)
        result = currRe.find(currExpr)
        if (result == null && additionFirst) {
            currRe = multiplicationRe
            result = currRe.find(currExpr)
        }
    }
    return currExpr
}

fun processLine(line: String, additionFirst: Boolean) {
    var currLine = line
    var result = expressionRe.find(currLine)
    while (result != null) {
        var value = processExpression(result!!.value.substring(1, result!!.value.length-1), additionFirst)
        currLine = currLine.substring(0, result!!.range.first) + value + currLine.substring(result!!.range.last+1)
        result = expressionRe.find(currLine)
    }
    expressionSum += processExpression(currLine, additionFirst).toLong()
}

File(args[0]).forEachLine {processLine(it, false)}
println(expressionSum)

expressionSum = 0L
File(args[0]).forEachLine {processLine(it, true)}
println(expressionSum)
