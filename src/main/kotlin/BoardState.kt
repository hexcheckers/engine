package com.hexcheckers.engine

class BoardState {
    var menCountA = 0
    var menCountB = 0
    var kingCountA = 0
    var kingCountB = 0
    var captureMovesCountA = 0
    var captureMovesCountB = 0

    fun incPiecesValue(piece: Piece) {
        if (piece.isKing) {
            incKing(piece.color)
        } else {
            incMen(piece.color)
        }
    }

    fun incMovesValue(piece: Piece) {
        if (piece.color == Piece.Color.A) {
            captureMovesCountA++
        } else {
            captureMovesCountB++
        }
    }

    private fun incMen(color: Piece.Color) {
        if (color == Piece.Color.A) {
            menCountA++
        } else {
            menCountB++
        }
    }

    private fun incKing(color: Piece.Color) {
        if (color == Piece.Color.A) {
            kingCountA++
        } else {
            kingCountB++
        }
    }
}

fun buildBoardState(board: Board): BoardState {
    val boardState = BoardState()
    for (cell in board.cells.values) {
        if (cell.isNotEmpty()) {
            val piece = cell.piece!!
            boardState.incPiecesValue(piece)
            if (piece.getAvailableCaptureMoves().isNotEmpty()) {
                boardState.incMovesValue(piece)
            }
        }
    }
    return boardState
}
