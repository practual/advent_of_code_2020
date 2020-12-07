import java.io.File

var input = File(args[0]).readLines()

var xsize = input[0].length

fun trees_on_slope(x_step: Int, y_step: Int): Long {
	var x = 0
	var y = 0
	var trees = 0L
	while (y < input.size) {
		trees += if (input[y][x] == '#') 1 else 0
		x = (x + x_step) % xsize
		y += y_step
	}
	return trees
}

println(trees_on_slope(3, 1))

println(trees_on_slope(1, 1) * trees_on_slope(3, 1) * trees_on_slope(5, 1) * trees_on_slope(7, 1) * trees_on_slope(1, 2))
