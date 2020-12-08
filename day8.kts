import java.io.File

var program = mutableListOf<Pair<String, Int>>()

fun processLine(line: String) {
    var inst = line.substring(0, 3)
    var operand = line.substring(4).toInt()
    program.add(Pair(inst, operand))
}

fun runProgram(instructionToFix: Int): Pair<Int, Boolean> {
    var acc = 0
    var pnt = 0

    var halted = false
    var fixableInstructionCount = 0

    var seenInstructions = mutableSetOf<Int>()

    while (pnt !in seenInstructions) {
        if (pnt >= program.size) {
            halted = true
            break
        }
        seenInstructions.add(pnt)
        var (inst, operand) = program[pnt]
        if (inst == "nop" || inst == "jmp") {
            if (fixableInstructionCount == instructionToFix) {
                inst = if (inst == "nop") "jmp" else "nop"
            }
            fixableInstructionCount += 1
        }
        when(inst) {
            "nop" -> pnt++
            "acc" -> {acc += operand; pnt++}
            "jmp" -> pnt += operand
        }
    }

    return Pair(acc, halted)
}

File(args[0]).forEachLine {processLine(it)}

println(runProgram(-1))

var acc =0
var halted = false
var instructionToFix = 0
while (!halted) {
    var p = runProgram(instructionToFix)
    acc = p.first
    halted = p.second
    instructionToFix += 1
}
println(acc)
