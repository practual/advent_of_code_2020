import java.io.File

var unionYes = 0
var intersectYes = 0
var unionForGroup: Set<Char> = setOf()
var intersectForGroup: Set<Char> = setOf()
var firstPerson = true

fun processLine(line: String) {
    if (line == "") {
        unionYes += unionForGroup.size
        unionForGroup = setOf()

        intersectYes += intersectForGroup.size
        intersectForGroup = setOf()
        firstPerson = true
    } else {
        var personSet = line.toSet()
        unionForGroup = unionForGroup union personSet
        if (firstPerson) {
            intersectForGroup = personSet
        } else {
            intersectForGroup = intersectForGroup intersect personSet
        }
        firstPerson = false
    }
}

File(args[0]).forEachLine {processLine(it)}
unionYes += unionForGroup.size
intersectYes += intersectForGroup.size

println(unionYes)
println(intersectYes)
