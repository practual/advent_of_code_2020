import java.io.File

var foodRe = """([a-z\s]+) \(contains ([a-z,\s]+)\)""".toRegex()

var allergenMap = mutableMapOf<String, Set<String>>()
var allIngreds = mutableListOf<String>()
fun processLine(line: String) {
    var (ingredients, allergens) = foodRe.find(line)!!.destructured
    var ingredSet = ingredients.split(" ").toSet()
    allIngreds.addAll(ingredients.split(" "))
    for (allergen in allergens.split(", ")) {
        if (allergen in allergenMap) {
            allergenMap[allergen] = allergenMap[allergen]!! intersect ingredSet
        } else {
            allergenMap[allergen] = ingredSet
        }
    }
}

File(args[0]).forEachLine {processLine(it)}

while (allergenMap.count {it.value.size == 1} != allergenMap.size) {
    var toRemove = setOf<String>()
    for (allergen in allergenMap) {
        if (allergen.value.size == 1) {
            toRemove = toRemove union allergen.value
        }
    }
    for (allergen in allergenMap) {
        if (allergen.value.size != 1) {
            allergenMap[allergen.key] = allergen.value subtract toRemove
        }
    }
}

var allergicIngreds = setOf<String>()
for (allergen in allergenMap) {
    allergicIngreds = allergicIngreds union allergen.value
}
var nonAllergicIngreds = allIngreds.toSet() subtract allergicIngreds
println(allIngreds.count {it in nonAllergicIngreds})
println(
    allergenMap.keys.sorted().fold(
        mutableListOf<String>()
    ) {acc, el -> acc.add(allergenMap[el]!!.elementAt(0)); acc}.joinToString(",")
)
