package utils.binarystringtolong

fun binaryStringToLong(binString: String): Long {
    var long = 0L
    var exp = binString.length - 1
    for (digit in binString) {
        if (digit == "1".single()) {
            long += Math.pow(2.0, exp.toDouble()).toLong()
        }
        exp -= 1
    }
    return long
}
