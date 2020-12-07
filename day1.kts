import java.io.File

var input = File(args[0]).readLines().map {num -> num.toInt()}.sorted()

var a = 0
var b = input.size - 1
var el1 = 0
var el2 = 0

while (a < b) {
	el1 = input[a]
	el2 = input[b]
	if (el1 + el2 == 2020) {
	    break
	} else if (el1 + el2 < 2020) {
	    a += 1
	} else {
		b -= 1
	}
}
println(el1 * el2)

val numSet = input.toSet()
el1 = 0
el2 = 0
var el3 = 0
var rem = 0
for (i in 0 .. input.size - 1) {
	for (j in i + 1 .. input.size - 1) {
		el1 = input[i]
		el2 = input[j]
		rem = 2020 - el1 - el2
		if (rem in numSet) {
			// Assuming unique values
			el3 = rem
			break
		}
	}
	if (el3 > 0) {
		break
	}
}
println(el1 * el2 * el3)
