package com.hexcheckers.engine.helper

import com.hexcheckers.engine.*

private const val ANSI_RESET = "\u001B[0m"
private const val ANSI_YELLOW = "\u001B[33m"
private const val ANSI_BLUE = "\u001B[34m"

fun renderToAscii(board: Board): String {
    val cellsNumber = getBoardCellsNumber(board.size)
    val boardHeight = getBoardRowsNumber(board.size)
    val boardWidth = getBoardColsNumber(board.size)
    var currentCell: Int

    var buffer = ""
    if (boardWidth == 6) {
        buffer += "       ___     ___     ___\n"
        buffer += "   ___/ ${getColoredPieceSignInCellId(board, cellsNumber - 4)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber - 2)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber)} \\\n"
        currentCell = cellsNumber - 5
    } else if (boardWidth == 8) {
        buffer += "       ___     ___     ___     ___\n"
        buffer += "   ___/ ${getColoredPieceSignInCellId(board, cellsNumber - 6)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber - 4)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber - 2)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber)} \\\n"
        currentCell = cellsNumber - 7
    } else { // 10
        buffer += "       ___     ___     ___     ___     ___\n"
        buffer += "   ___/ ${getColoredPieceSignInCellId(board, cellsNumber - 8)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber - 6)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber - 4)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber - 2)} \\___/ ${getColoredPieceSignInCellId(board, cellsNumber)} \\\n"
        currentCell = cellsNumber - 9
    }

    for (i in 1..boardHeight) {
        if (boardWidth == 6) {
            buffer += "${boardHeight - i + 1} / ${getColoredPieceSignInCellId(board, currentCell)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 2)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 4)} \\___/\n"
            if (i < boardHeight) {
                buffer += "  \\___/ ${getColoredPieceSignInCellId(board, currentCell - 5)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 3)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 1)} \\\n"
                currentCell -= 6
            }
        } else if (boardWidth == 8) {
            buffer += "${(boardHeight - i + 1) % 10} / ${getColoredPieceSignInCellId(board, currentCell)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 2)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 4)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 6)} \\___/\n"
            if (i < boardHeight) {
                buffer += "  \\___/ ${getColoredPieceSignInCellId(board, currentCell - 7)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 5)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 3)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 1)} \\\n"
                currentCell -= 8
            }
        } else { // 10
            buffer += "${(boardHeight - i + 1) % 10} / ${getColoredPieceSignInCellId(board, currentCell)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 2)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 4)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 6)} \\___/ ${getColoredPieceSignInCellId(board, currentCell + 8)} \\___/\n"
            if (i < boardHeight) {
                buffer += "  \\___/ ${getColoredPieceSignInCellId(board, currentCell - 9)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 7)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 5)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 3)} \\___/ ${getColoredPieceSignInCellId(board, currentCell - 1)} \\\n"
                currentCell -= 10
            }
        }
    }

    if (boardWidth == 6) {
        buffer += "  \\___/   \\___/   \\___/\n"
        buffer += "    a   b   c   d   e   f"
    } else if (boardWidth == 8) {
        buffer += "  \\___/   \\___/   \\___/   \\___/\n"
        buffer += "    a   b   c   d   e   f   g   h"
    } else { // 10
        buffer += "  \\___/   \\___/   \\___/   \\___/   \\___/\n"
        buffer += "    a   b   c   d   e   f   g   h   i   j"
    }

    return buffer
}

fun renderToHcFen(board: Board): String {
    val cols = getBoardColsNumber(board.size)
    val rows = getBoardRowsNumber(board.size)
    var stringRepresentation = "${cols}x${rows}/"
    var cellId = 1
    for (row in 1..rows) {
        var rowString = ""
        for (col in 1..cols) {
            rowString += getPieceSignInCellId(board, cellId++)
        }
        stringRepresentation += rowString.trimEnd().replace(" ", "-")
        if (row < rows) {
            stringRepresentation += "/"
        }
    }
    return stringRepresentation
}

fun getColoredPieceSignInCellId(board: Board, cellId: Int): String {
    return when(val sign = getPieceSignInCellId(board, cellId)) {
        "a", "A" -> "${ANSI_YELLOW}${sign}${ANSI_RESET}"
        else -> "${ANSI_BLUE}${sign}${ANSI_RESET}"
    }
}