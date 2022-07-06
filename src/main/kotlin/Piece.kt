package com.hexcheckers.engine

class Piece(var cell: Cell, val color: Color, var isKing: Boolean = false) {
    enum class Color {
        A, B
    }

    init {
        cell.piece = this
    }

    override fun toString(): String {
        return "${getSign()}@${getCellName()}"
    }

    fun getSign(): String {
        var color = if (color == Color.A) "a" else "b"
        if (isKing) {
            color = color.uppercase()
        }
        return color
    }

    fun getCellName(): String {
        return cell.name
    }

    fun copyTo(toBoard: Board): Piece {
        return Piece(toBoard.cells[cell.name]!!, color, isKing)
    }

    fun removeFromBoard() {
        cell.clear()
    }

    fun getAvailableRegularMoves(): List<Move> {
        val virtualBoard = cell.board.copy()
        val piece = findTwinPieceOnBoard(this, virtualBoard)
        val moves: List<Move> = if (isKing) getAvailableRegularKingMoves(piece) else getAvailableRegularMenMoves(piece)
        return assignMovesToBoard(moves, cell.board)
    }

    fun getAvailableCaptureMoves(): List<Move> {
        val virtualBoard = cell.board.copy()
        val piece = findTwinPieceOnBoard(this, virtualBoard)
        val moves: List<Move> = if (isKing) getAvailableCaptureKingMoves(piece) else getAvailableCaptureMenMoves(piece)
        return assignMovesToBoard(moves, cell.board)
    }
}

fun getInvertedColor(color: Piece.Color): Piece.Color {
    return if (color == Piece.Color.A) Piece.Color.B else Piece.Color.A
}

private fun getAvailableRegularMenMoves(piece: Piece): List<Move> {
    val moves: MutableList<Move> = mutableListOf()
    val directions: List<Board.Direction> = if (piece.color == Piece.Color.A) getTopMoveDirections() else getBottomMoveDirections()
    directions.forEach { direction ->
        if (piece.cell.board.isCellExistsAndEmpty(piece.cell.getSiblingCell(direction))) {
            moves += Move(piece.cell, piece.cell.getSiblingCell(direction)!!)
        }
    }
    return moves
}

private fun getAvailableRegularKingMoves(piece: Piece): List<Move> {
    val moves: MutableList<Move> = mutableListOf()
    getAllMoveDirections().forEach { direction ->
        var tmp = piece.cell
        while (piece.cell.board.isCellExistsAndEmpty(tmp.getSiblingCell(direction))) {
            moves += Move(piece.cell, tmp.getSiblingCell(direction)!!)
            tmp = tmp.getSiblingCell(direction)!!
        }
    }
    return moves
}

private fun getAvailableCaptureMenMoves(piece: Piece): List<Move> {
    val moves: MutableList<Move> = mutableListOf()
    val capturedPieces: MutableList<Piece> = mutableListOf()
    getAvailableCaptureMenMoves(piece, moves, capturedPieces)
    return moves
}

private fun getAvailableCaptureMenMoves(
    piece: Piece,
    queue: MutableList<Move>,
    capturedPieces: MutableList<Piece>,
    parentMove: Move? = null
): Boolean {
    if (piece.isKing || (parentMove != null && (parentMove.srcPieceIsKing || parentMove.dstPieceIsKing))) {
        return getAvailableCaptureKingMoves(piece, queue, capturedPieces, parentMove)
    }

    if (parentMove != null) {
        piece.cell.board.executeOneMove(parentMove, false)
        if (parentMove.isCaptured()) {
            capturedPieces += parentMove.capturePiece!!
        }
    }

    val cell: Cell = parentMove?.dstCell ?: piece.cell
    var move: Move? = null

    getAllMoveDirections().forEach { direction ->
        val siblingCell = cell.getSiblingCell(direction)
        val targetCell = siblingCell?.getSiblingCell(direction)
        if (siblingCell != null && targetCell != null && !siblingCell.isEmpty() && targetCell.isEmpty()
            && siblingCell.piece?.color != piece.color
            && (parentMove == null || parentMove.direction != getInvertedDirection(direction))
            && !capturedPieces.contains(siblingCell.piece)
        ) {
            move = Move(cell, targetCell, siblingCell.piece, parentMove?.copy())
            if (!getAvailableCaptureMenMoves(piece, queue, capturedPieces, move)) {
                queue.add(move!!.root())
            }
        }
    }

    if (parentMove != null) {
        piece.cell.board.rollbackOneMove(parentMove)
        if (parentMove.isCaptured()) {
            capturedPieces.remove(parentMove.capturePiece!!)
        }
    }

    return move != null
}

private fun getAvailableCaptureKingMoves(piece: Piece): List<Move> {
    val moves: MutableList<Move> = mutableListOf()
    val capturedPieces: MutableList<Piece> = mutableListOf()
    getAvailableCaptureKingMoves(piece, moves, capturedPieces)
    return moves
}

private fun getAvailableCaptureKingMoves(
    piece: Piece,
    queue: MutableList<Move>,
    capturedPieces: MutableList<Piece>,
    parentMove: Move? = null
): Boolean {
    if (parentMove != null) {
        piece.cell.board.executeOneMove(parentMove, false)
        if (parentMove.isCaptured()) {
            capturedPieces += parentMove.capturePiece!!
        }
    }

    val cell: Cell = parentMove?.dstCell ?: piece.cell
    var enemyPieceCell: Cell?
    var emptyCell: Cell?
    var move: Move? = null

    getAllMoveDirections().forEach { direction ->
        if (parentMove == null || parentMove.direction !== getInvertedDirection(direction)) {
            enemyPieceCell = cell.getSiblingCell(direction)
            while (enemyPieceCell != null && enemyPieceCell!!.isEmpty()) {
                enemyPieceCell = enemyPieceCell!!.getSiblingCell(direction)
            }
            if (enemyPieceCell is Cell) {
                if (enemyPieceCell!!.isNotEmpty()
                    && enemyPieceCell!!.getSiblingCell(direction) != null
                    && enemyPieceCell!!.piece!!.color != piece.color
                    && !capturedPieces.contains(enemyPieceCell!!.piece)
                ) {
                    emptyCell = enemyPieceCell!!.getSiblingCell(direction)
                    var capturedMovesExists = false
                    val regularMoves: MutableList<Move> = mutableListOf()
                    while (emptyCell != null) {
                        if (emptyCell!!.isNotEmpty()) {
                            break
                        }
                        move = Move(cell, emptyCell!!, enemyPieceCell!!.piece, parentMove?.copy())
                        regularMoves.add(move!!.root())
                        if (getAvailableCaptureKingMoves(piece, queue, capturedPieces, move)) {
                            capturedMovesExists = true
                        }
                        emptyCell = emptyCell!!.getSiblingCell(direction)
                    }
                    if (!capturedMovesExists) {
                        queue.addAll(regularMoves)
                    }
                }
            }
        }
    }

    if (parentMove != null) {
        piece.cell.board.rollbackOneMove(parentMove)
        if (parentMove.isCaptured()) {
            capturedPieces.remove(parentMove.capturePiece!!)
        }
    }

    return move != null
}
