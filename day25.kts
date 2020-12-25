import java.io.File

var publicKeys = File(args[0]).readLines().map {it.toLong()}

fun findLoopSize(publicKey: Long): Long {
    var subjectNumber = 7L
    var value = 1L
    var loopSize = 0L
    while (value != publicKey) {
        value *= subjectNumber
        value %= 20201227L
        loopSize++
    }
    return loopSize
}

fun generateKey(subjectNumber: Long, loopSize: Long): Long {
    var value = 1L
    for (l in 1..loopSize) {
        value *= subjectNumber
        value %= 20201227L
    }
    return value
}

println(generateKey(publicKeys[0], findLoopSize(publicKeys[1])))
