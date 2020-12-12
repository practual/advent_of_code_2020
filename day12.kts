import java.io.File
import kotlin.math.abs

var input = File(args[0]).readLines()

var position = Pair(0, 0) // N, E
var direction = 0 // E

for (line in input) {
    var instruction = line.take(1)
    var operand = line.substring(1).toInt()
    if (instruction == "F") {
        when (direction) {
            0 -> instruction = "E"
            90 -> instruction = "S"
            180 -> instruction = "W"
            270 -> instruction = "N"
        }
    }
    when (instruction) {
        "N" -> position = Pair(position.first + operand, position.second)
        "S" -> position = Pair(position.first - operand, position.second)
        "E" -> position = Pair(position.first, position.second + operand)
        "W" -> position = Pair(position.first, position.second - operand)
        "L" -> direction = (direction - operand + 360) % 360
        "R" -> direction = (direction + operand + 360) % 360
    }
}

println(abs(position.first) + abs(position.second))

var boatPosition = Pair(0, 0) // N, E
var waypointPosition = Pair(1, 10)

for (line in input) {
    var instruction = line.take(1)
    var operand = line.substring(1).toInt()
    if (instruction == "L") {
        instruction = "R"
        operand = 360 - operand
    }
    if (instruction == "R") {
        operand = (operand + 360) % 360
    }
    when (instruction) {
        "N" -> waypointPosition = Pair(waypointPosition.first + operand, waypointPosition.second)
        "S" -> waypointPosition = Pair(waypointPosition.first - operand, waypointPosition.second)
        "E" -> waypointPosition = Pair(waypointPosition.first, waypointPosition.second + operand)
        "W" -> waypointPosition = Pair(waypointPosition.first, waypointPosition.second - operand)
        "R" -> {
            when (operand) {
                90 -> waypointPosition = Pair(-waypointPosition.second, waypointPosition.first)
                180 -> waypointPosition = Pair(-waypointPosition.first, -waypointPosition.second)
                270 -> waypointPosition = Pair(waypointPosition.second, -waypointPosition.first)
            }
        }
        "F" -> boatPosition = Pair(
            boatPosition.first + operand * waypointPosition.first,
            boatPosition.second + operand * waypointPosition.second
        )
    }
}

println(abs(boatPosition.first) + abs(boatPosition.second))
