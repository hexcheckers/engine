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