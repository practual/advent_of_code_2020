import java.io.File

var re = """([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)""".toRegex()

fun policy1(input: String): Boolean {
	val (lower, upper, char, password) = re.find(input)!!.destructured
	val count = password.count { it == char.single() }
	return lower.toInt() <= count && count <= upper.toInt()
}

fun policy2(input: String): Boolean {
	val (first, second, char, password) = re.find(input)!!.destructured
	return (password[first.toInt() - 1] == char.single()) xor (password[second.toInt() - 1] == char.single())
}

println(File(args[0]).readLines().count { policy1(it) })
println(File(args[0]).readLines().count { policy2(it) })
