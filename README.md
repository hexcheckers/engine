# HexCheckers Engine

This library implements rules and provides simple AI for HexCheckers game.

### Examples

###### Board

```kotlin
val board = Board(Board.Size.SIZE_6X6, true)
println(board.toAscii())
```

```
       ___     ___     ___
   ___/ b \___/ b \___/ b \
6 / b \___/ b \___/ b \___/
  \___/ b \___/ b \___/ b \
5 / b \___/ b \___/ b \___/
  \___/   \___/   \___/   \
4 /   \___/   \___/   \___/
  \___/   \___/   \___/   \
3 /   \___/   \___/   \___/
  \___/ a \___/ a \___/ a \
2 / a \___/ a \___/ a \___/
  \___/ a \___/ a \___/ a \
1 / a \___/ a \___/ a \___/
  \___/   \___/   \___/
    a   b   c   d   e   f
```

###### Move

```kotlin
val move = board.buildMove("d2-c3")
board.executeMove(move)

println(board.toAscii())
```

```
       ___     ___     ___
   ___/ b \___/ b \___/ b \
6 / b \___/ b \___/ b \___/
  \___/ b \___/ b \___/ b \
5 / b \___/ b \___/ b \___/
  \___/   \___/   \___/   \
4 /   \___/   \___/   \___/
  \___/   \___/   \___/   \
3 /   \___/ a \___/   \___/
  \___/ a \___/   \___/ a \
2 / a \___/ a \___/ a \___/
  \___/ a \___/ a \___/ a \
1 / a \___/ a \___/ a \___/
  \___/   \___/   \___/
    a   b   c   d   e   f
```

###### AI

```kotlin
val ai = Ai(board)

move = ai.findBestMove(Piece.Color.B)!!
println(move)

board.executeMove(move)
println(board.toAscii())
```

```
b5-b4
       ___     ___     ___
   ___/ b \___/ b \___/ b \
6 / b \___/ b \___/ b \___/
  \___/   \___/ b \___/ b \
5 / b \___/ b \___/ b \___/
  \___/ b \___/   \___/   \
4 /   \___/   \___/   \___/
  \___/   \___/   \___/   \
3 /   \___/ a \___/   \___/
  \___/ a \___/   \___/ a \
2 / a \___/ a \___/ a \___/
  \___/ a \___/ a \___/ a \
1 / a \___/ a \___/ a \___/
  \___/   \___/   \___/
    a   b   c   d   e   f
```

### Simple game

```kotlin
import com.hexcheckers.engine.*
import java.util.Scanner

fun main() {
    println("Welcome to HexCheckers 6x6 game!")
    val board = Board(Board.Size.SIZE_6X6, true)
    println(board.toAscii())

    val ai = Ai(board)
    var currentTurn = Piece.Color.A
    val scanner = Scanner(System.`in`)
    while (true) {
        val availableMoves = board.getAvailableMoves(currentTurn)
        if (availableMoves.isEmpty()) {
            println("${getInvertedColor(currentTurn)} win!")
            break
        }

        var move: Move? = null
        if (currentTurn == Piece.Color.A) {
            while (move == null) {
                print("👨 Enter your move: ")
                val input = scanner.nextLine()
                try {
                    move = board.buildMove(input)
                } catch (e: Exception) {
                    println("❌ Incorrect input")
                }
            }
        } else {
            print("🤖 Ai move: ...")
            move = ai.findBestMove(currentTurn)
            println("\b\b\b$move")
        }
        board.executeMove(move!!)
        println(board.toAscii())

        currentTurn = getInvertedColor(currentTurn)
    }
}
```

![Game demo](https://raw.github.com/hexcheckers/engine/main/.res/hexcheckers-6x6-game-demo.gif)