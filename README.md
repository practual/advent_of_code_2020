# Advent of Code 2020

Solutions to https://adventofcode.com/2020, used as a chance to learn Kotlin.

Scripts run with e.g. `kotlinc -script day2.kts day2_input`

Where util functions are needed, add the classpath, `kotlinc -classpath utils.jar -script day9.kts day9_input`

Jar compiled with `kotlinc utils/* -d utils.jar`

Day 15 needed to be compiled and run as a jar to avoid memory issues:
`kotlinc day15.kt -include-runtime -d day15.jar`
and run:
`java -jar day15.jar day15_input`
