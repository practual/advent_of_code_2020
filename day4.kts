import java.io.File

var passport: MutableMap<String, String> = mutableMapOf()
var validPassports = 0

var re = """([a-z]+):([a-z0-9#]+)""".toRegex()
var hclRe = """#[abcdef0-9]{6}""".toRegex()
var pidRe = """[0-9]{9}""".toRegex()

fun isValid(passport: MutableMap<String, String>): Boolean {
	return listOf("ecl", "pid", "eyr", "hcl", "byr", "iyr", "hgt").count {passport.contains(it)} == 7
}

fun isValidStrict(passport: MutableMap<String, String>): Boolean {
	if (!isValid(passport)) {
		return false
	}

	val byr = passport["byr"]
	if (byr!!.length != 4 || byr.toInt() < 1920 || byr.toInt() > 2002) {
		return false
	}

	val iyr = passport["iyr"]
	if (iyr!!.length != 4 || iyr.toInt() < 2010 || iyr.toInt() > 2020) {
		return false
	}

	val eyr = passport["eyr"]
	if (eyr!!.length != 4 || eyr.toInt() < 2020 || eyr.toInt() > 2030) {
		return false
	}

	val hgt = passport["hgt"]
	if (hgt!!.takeLast(2) != "cm" && hgt.takeLast(2) != "in") {
		return false
	}

	if (hgt.takeLast(2) == "cm") {
		if (hgt.length != 5 || hgt.take(3).toInt() < 150 || hgt.take(3).toInt() > 193) {
			return false
		}
	} else {
		if (hgt.length != 4 || hgt.take(2).toInt() < 59 || hgt.take(2).toInt() > 76) {
			return false
		}
	}

	val hcl = passport["hcl"]
	if (!hcl!!.matches(hclRe)) {
		return false
	}

	val ecl = passport["ecl"]
	if (ecl !in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")) {
		return false
	}

	val pid = passport["pid"]
	if (!pid!!.matches(pidRe)) {
		return false
	}

	return true
}

fun processLine(line: String, isStrict: Boolean) {
	if (line == "") {
		if (isStrict) {
			if (isValidStrict(passport)) {
				validPassports += 1
			}
		} else if (isValid(passport)) {
			validPassports += 1
		}
		passport = mutableMapOf()
	} else {
		for (match in re.findAll(line)) {
			val (key, value) = match.destructured
			passport[key] = value
		}
	}
}

File(args[0]).forEachLine {processLine(it, false)}
if (isValid(passport)) {
	validPassports += 1
}

println(validPassports)

passport = mutableMapOf()
validPassports = 0
File(args[0]).forEachLine {processLine(it, true)}
if (isValidStrict(passport)) {
	validPassports += 1
}

println(validPassports)
