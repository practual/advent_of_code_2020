import java.io.File
import utils.binarystringtolong.*

val memRe = """mem\[([0-9]+)\] = ([0-9]+)""".toRegex()

var memory = mutableMapOf<Long, Long>()

var isMaskApplied = 0L
var mask = 0L

fun processLine(line: String) {
    if (line.take(4) == "mask") {
        isMaskApplied = binaryStringToLong(
            line.substring(7).map {if (it == "X".single()) "0" else "1"}.joinToString("")
        )
        mask = binaryStringToLong(line.substring(7))
    } else {
        val (address, value) = memRe.find(line)!!.destructured
        var maskedValue = (isMaskApplied and mask) or (isMaskApplied.inv() and value.toLong())
        memory[address.toLong()] = maskedValue
    }
}

File(args[0]).forEachLine {processLine(it)}
println(memory.values.sum())

memory = mutableMapOf<Long, Long>()
var maskOnes = 0L
var floatingBitIndices = listOf<Int>()
var floatingMask = 0L
fun processLineV2(line: String) {
    if (line.take(4) == "mask") {
        // A mask of where all the 1s are - these will overwrite the address bits.
        maskOnes = binaryStringToLong(line.substring(7))
        // Index locations of each of the floating bits.
        floatingBitIndices = (
            line.substring(7).foldIndexed(mutableListOf<Int>()) {
                idx, acc, value -> if (value == "X".single()) acc.add(35 - idx); acc
            }
        ).reversed()
        // A mask of where the floating bits are applied.
        floatingMask = binaryStringToLong(
            line.substring(7).map {if (it == "X".single()) "1" else "0"}.joinToString("")
        )
    } else {
        val (address, value) = memRe.find(line)!!.destructured
        // Easy part - any 1s in the mask overwrite the address bits
        val addressOverwrittenByOnes = address.toLong() or maskOnes
        // If there are N floating bits, there are 2^N different addresses to write to.
        for (maskNum in 0L..(1L shl floatingBitIndices.size)-1L) {
            var floatingBitMask = 0L
            var floatingBitNum = 1L
            // The maskNum represents a number of bits to use as the mask, but we need to distribute
            // them according to the original floating bit indices (where the Xs are in the mask)
            // For each X index, see whether the corresponding bit from maskNum is set. Do this for each
            // index to build up the overall mask.
            for (i in 0..floatingBitIndices.size-1) {
                var floatingBitIdxDiff = floatingBitIndices[i] - i
                floatingBitMask = floatingBitMask or ((floatingBitNum and maskNum) shl floatingBitIdxDiff)
                floatingBitNum = floatingBitNum shl 1
            }
            var maskedAddress = (floatingMask and floatingBitMask) or (floatingMask.inv() and addressOverwrittenByOnes)
            memory[maskedAddress] = value.toLong()
        }
    }
}

File(args[0]).forEachLine {processLineV2(it)}
println(memory.values.sum())

