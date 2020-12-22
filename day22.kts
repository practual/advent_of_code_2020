import java.io.File

var p1Deck = mutableListOf<Int>()
var p2Deck = mutableListOf<Int>()
var process1 = true
fun processLine(line: String) {
    var deck = if (process1) p1Deck else p2Deck
    if (line == "") {
        process1 = false
    } else if (line.take(6) != "Player") {
        deck.add(line.toInt())
    }
}

File(args[0]).forEachLine {processLine(it)}

fun getScore(deck: ArrayDeque<Int>): Int {
    return deck.foldIndexed(0) {idx, acc, el -> acc + (deck.size - idx) * el}
}

var p1Deque = ArrayDeque(p1Deck)
var p2Deque = ArrayDeque(p2Deck)

while (p1Deque.size > 0 && p2Deque.size > 0) {
    var p1 = p1Deque.removeFirst()
    var p2 = p2Deque.removeFirst()
    if (p1 > p2) {
        p1Deque.addAll(listOf(p1, p2))
    } else {
        p2Deque.addAll(listOf(p2, p1))
    }
}

var winningDeck = if (p1Deque.size > 0) p1Deque else p2Deque
println(getScore(winningDeck))

fun copyDeck(deck: ArrayDeque<Int>, size: Int): ArrayDeque<Int> {
    var newDeque = ArrayDeque<Int>()
    for (i in 0..size-1) {
        newDeque.add(deck[i])
    }
    return newDeque
}

fun playGame(p1Deque: ArrayDeque<Int>, p2Deque: ArrayDeque<Int>): Int {
    var seenConfigs = mutableSetOf<Pair<ArrayDeque<Int>, ArrayDeque<Int>>>()
    while (p1Deque.size > 0 && p2Deque.size > 0) {
        var newConfig = Pair(p1Deque, p2Deque)
        if (newConfig in seenConfigs) {
            return 0
        }
        seenConfigs.add(newConfig)

        var p1 = p1Deque.removeFirst()
        var p2 = p2Deque.removeFirst()
        var roundWinner = 0
        if (p1 <= p1Deque.size && p2 <= p2Deque.size) {
            roundWinner = playGame(copyDeck(p1Deque, p1), copyDeck(p2Deque, p2))
        } else {
            roundWinner = if (p1 > p2) 0 else 1
        }
        if (roundWinner == 0) {
            p1Deque.addAll(listOf(p1, p2))
        } else {
            p2Deque.addAll(listOf(p2, p1))
        }
    }
    return if (p1Deque.size > 0) 0 else 1
}

p1Deque = ArrayDeque(p1Deck)
p2Deque = ArrayDeque(p2Deck)

playGame(p1Deque, p2Deque)

winningDeck = if (p1Deque.size > 0) p1Deque else p2Deque
println(getScore(winningDeck))
