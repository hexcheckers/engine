package com.hexcheckers.engine.helper

import com.hexcheckers.engine.*

/**
 * Build Board from HC-FEN string representation (based on FEN)
 * @param stringRepresentation String
 * @return Board
 */
fun makeBoardFromHcFen(stringRepresentation: String): Board {
    if (!isHcFenStringValid(stringRepresentation)) {
        throw IllegalArgumentException("Invalid board representation \"${stringRepresentation}\".")
    }

    val size = getBoardSizeFromHcFen(stringRepresentation)
        ?: throw IllegalArgumentException("Invalid board representation \"${stringRepresentation}\".")

    val sizeMarkerLength = getBoardSizeStringLength(size);
    val board = Board(size)
    var row = 1
    var col = 'a'
    for (i in sizeMarkerLength + 1 until stringRepresentation.length) {
        val c = stringRepresentation[i]
        if (c == '-') {
            col++
        } else if (c == '/') {
            row++
            col = 'a'
        } else if (listOf('a', 'A', 'b', 'B').contains(c)) {
            val color = if (listOf('a', 'A').contains(c)) Piece.Color.A else Piece.Color.B
            val isKing = listOf('A', 'B').contains(c)
            board.addPiece(board.cells["" + col + row]!!, color, isKing)
            col++
        } else {
            col++
        }
    }
    return board
}

fun isHcFenStringValid(stringRepresentation: String): Boolean {
    val size = getBoardSizeFromHcFen(stringRepresentation) ?: return false
    val regex = when (size) {
        Board.Size.SIZE_6X6 -> """^6x6/([aAbB-]{0,6}/){5}[aAbB-]{0,6}$""".toRegex()
        Board.Size.SIZE_6X8 -> """^6x8/([aAbB-]{0,6}/){7}[aAbB-]{0,6}$""".toRegex()
        Board.Size.SIZE_8X6 -> """^8x6/([aAbB-]{0,8}/){5}[aAbB-]{0,8}$""".toRegex()
        Board.Size.SIZE_8X8 -> """^8x8/([aAbB-]{0,8}/){7}[aAbB-]{0,8}$""".toRegex()
        Board.Size.SIZE_8X10 -> """^8x10/([aAbB-]{0,8}/){9}[aAbB-]{0,8}$""".toRegex()
        Board.Size.SIZE_10X8 -> """^10x8/([aAbB-]{0,10}/){7}[aAbB-]{0,10}$""".toRegex()
        Board.Size.SIZE_10X10 -> """^10x10/([aAbB-]{0,10}/){9}[aAbB-]{0,10}$""".toRegex()
    }
    return regex.matches(stringRepresentation);
}

fun getBoardSizeFromHcFen(stringRepresentation: String): Board.Size? {
    var sizeMarkerLength = 3
    if (stringRepresentation.length < sizeMarkerLength) {
        return null
    }
    var size: Board.Size? = when (stringRepresentation.subSequence(0, sizeMarkerLength).toString().lowercase()) {
        "6x6" -> Board.Size.SIZE_6X6
        "6x8" -> Board.Size.SIZE_6X8
        "8x6" -> Board.Size.SIZE_8X6
        "8x8" -> Board.Size.SIZE_8X8
        else -> null
    }

    if (size == null) {
        sizeMarkerLength = 4
        size = when (stringRepresentation.subSequence(0, sizeMarkerLength).toString().lowercase()) {
            "8x10" -> Board.Size.SIZE_8X10
            "10x8" -> Board.Size.SIZE_10X8
            else -> null
        }
    }

    if (size == null) {
        sizeMarkerLength = 5
        size = when (stringRepresentation.subSequence(0, sizeMarkerLength).toString().lowercase()) {
            "10x10" -> Board.Size.SIZE_10X10
            else -> null
        }
    }

    return size
}

private fun getBoardSizeStringLength(size: Board.Size): Int {
    return when (size) {
        Board.Size.SIZE_6X6, Board.Size.SIZE_6X8, Board.Size.SIZE_8X6, Board.Size.SIZE_8X8 -> 3
        Board.Size.SIZE_8X10, Board.Size.SIZE_10X8 -> 4
        else -> 5
    }
}
