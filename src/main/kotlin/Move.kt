package com.hexcheckers.engine

class Move(var srcCell: Cell, var dstCell: Cell, var capturePiece: Piece? = null, var previous: Move? = null) {
    val direction = detectMoveDirection(srcCell, dstCell)
    var next: Move? = null
        set(value) {
            field = value
            value?.previous = this
        }
    var piece: Piece? = srcCell.piece
    var srcPieceIsKing: Boolean = false
    var dstPieceIsKing: Boolean = false
    var score: Int = Int.MIN_VALUE

    init {
        if (piece != null) {
            srcPieceIsKing = piece!!.isKing
            dstPieceIsKing = dstCell.isKingCellForColor(piece!!.color)
        }

        if (previous != null) {
            if (previous!!.dstPieceIsKing) {
                srcPieceIsKing = true
                dstPieceIsKing = true
            }
            previous!!.next = this
        }
    }

    override fun toString(): String {
        val delimiter = if (isCaptured()) ":" else "-"
        var moveString = srcCell.name + delimiter + dstCell.name
        var tmpMove = this
        while (tmpMove.hasNext()) {
            tmpMove = tmpMove.next!!
            moveString += delimiter + tmpMove.dstCell.name
        }
        return moveString
    }

    fun asString(full: Boolean = false): String {
        if (full) {
            return toString()
        }
        return srcCell.name + (if (isCaptured()) ":" else "-") + dstCell.name
    }

    fun isCaptured(): Boolean {
        return capturePiece != null
    }

    fun hasNext(): Boolean {
        return next != null
    }

    fun hasPrevious(): Boolean {
        return previous != null
    }

    fun root(): Move {
        var root = this
        while (root.hasPrevious()) {
            root = root.previous!!
        }
        return root
    }

    fun end(): Move {
        var end = this
        while (end.hasNext()) {
            end = end.next!!
        }
        return end
    }

    fun copy(): Move {
        var currentMove: Move = root()
        var copy = copyOneMove(currentMove)
        while (currentMove.hasNext()) {
            currentMove = currentMove.next!!
            val nextMove = copyOneMove(currentMove)
            copy.next = nextMove
            nextMove.previous = copy
            copy = copy.next!!
        }
        return copy
    }

    private fun copyOneMove(move: Move): Move {
        val copy = Move(move.srcCell, move.dstCell, move.capturePiece)
        copy.piece = move.piece
        copy.srcPieceIsKing = move.srcPieceIsKing
        copy.dstPieceIsKing = move.dstPieceIsKing
        return copy
    }
}

fun detectMoveDirection(srcCell: Cell, dstCell: Cell): Board.Direction {
    if (srcCell.name == dstCell.name) {
        throw IllegalArgumentException("Cells should be different")
    }
    return if (dstCell.col < srcCell.col) {
        if (dstCell.row < srcCell.row) Board.Direction.BOTTOM_LEFT else Board.Direction.TOP_LEFT
    } else if (dstCell.col == srcCell.col) {
        if (dstCell.row < srcCell.row) Board.Direction.BOTTOM_MIDDLE else Board.Direction.TOP_MIDDLE
    } else {
        if (dstCell.row < srcCell.row) Board.Direction.BOTTOM_RIGHT else Board.Direction.TOP_RIGHT
    }
}

fun detectCapturePiece(board: Board, srcCell: String, dstCell: String): Piece {
    if (!board.isCellExists(srcCell)) {
        throw IllegalArgumentException("Source cell \"${srcCell}\" does not exists on the board.")
    }
    if (!board.isCellExistsAndEmpty(dstCell)) {
        throw IllegalArgumentException("Empty destination cell \"${dstCell}\" does not exists on the board.")
    }
    return detectCapturePiece(board.cells[srcCell]!!, board.cells[dstCell]!!)
}

fun detectCapturePiece(srcCell: Cell, dstCell: Cell): Piece {
    if (srcCell.isEmpty()) {
        throw IllegalArgumentException("Source cell \"${srcCell.name}\" is empty.")
    }
    if (!dstCell.isEmpty()) {
        throw IllegalArgumentException("Destination cell \"${srcCell.name}\" is not empty.")
    }

    val moveDirection = detectMoveDirection(srcCell, dstCell)
    var targetCell: Cell?
    if (srcCell.piece!!.isKing) {
        targetCell = srcCell.getSiblingCell(moveDirection)
        while (targetCell != null && targetCell.isEmpty() && targetCell.name != dstCell.name) {
            targetCell = targetCell.getSiblingCell(moveDirection)
        }
    } else {
        targetCell = srcCell.getSiblingCell(moveDirection)
    }
    if (targetCell != null && !targetCell.isEmpty() && targetCell.name != dstCell.name && targetCell.piece!!.color != srcCell.piece!!.color) {
        return targetCell.piece!!
    }

    throw Exception("No capture piece detected between \"${srcCell.name}\" and \"${dstCell.name}\" cells.")
}

fun isMoveStringRepresentationValid(stringRepresentation: String): Boolean {
    val regex = """^[a-j][1-9]0*[-:][a-j][1-9]0*([-:][a-j][1-9]0*)*$""".toRegex()
    return regex.matches(stringRepresentation)
}
