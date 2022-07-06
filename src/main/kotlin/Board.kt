package com.hexcheckers.engine

import com.hexcheckers.engine.helper.renderToAscii
import com.hexcheckers.engine.helper.renderToHcFen

class Board(val size: Size) {

    enum class Size {
        SIZE_6X6, SIZE_6X8, SIZE_8X6, SIZE_8X8, SIZE_8X10, SIZE_10X8, SIZE_10X10
    }

    enum class Direction {
        TOP_LEFT, TOP_MIDDLE, TOP_RIGHT,
        BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT
    }

    constructor(size: Size, createInitialPieces: Boolean) : this(size) {
        if (createInitialPieces) {
            createInitialPieces()
        }
    }

    val positions: List<String> = generateCellsPositionsById()

    val cells: Map<String, Cell> = createCells()

    private fun generateCellsPositionsById(): List<String> {
        val colsNumber = getBoardColsNumber(size)
        val positions: MutableList<String> = mutableListOf()
        positions += "00" // hack to avoid -1 when access
        var letter = 'a'
        var number = 1
        for (i in 1..getBoardCellsNumber(size)) {
            positions += "" + letter + number
            letter++
            if (i % colsNumber == 0) {
                letter = 'a'
                number++
            }
        }
        return positions
    }

    private fun createCells(): HashMap<String, Cell> {
        val cells = mutableMapOf<String, Cell>() as HashMap<String, Cell>
        for (row in 1..getBoardRowsNumber(size)) {
            for (col in 1..getBoardColsNumber(size)) {
                val cell = Cell(this, row, col)
                cells[cell.name] = cell
            }
        }
        linkCells(cells)
        return cells
    }

    private fun linkCells(cells: Map<String, Cell>) {
        val cellsInRow: Int = getBoardColsNumber(size)
        val cellsOnBoard: Int = getBoardCellsNumber(size)
        val topLine: MutableList<Int> = mutableListOf()
        val bottomLine: MutableList<Int> = mutableListOf()

        for (i in 1..cellsInRow) {
            if (i % 2 == 0) {
                bottomLine += i - 1
            } else {
                topLine += cellsOnBoard - i + 1
            }
        }

        for (i in 1..cellsOnBoard) {
            val cell: Cell = cells[positions[i]]!!

            // BOTTOM_MIDDLE
            if (i > cellsInRow) {
                val bottomMiddleCell = cells[positions[i - cellsInRow]]!!
                cell.bottomMiddle = bottomMiddleCell
                bottomMiddleCell.topMiddle = cell
            }

            // TOP_RIGHT
            if (i % cellsInRow != 0 && !topLine.contains(i)) {
                var delta = 1
                if (i % 2 == 0) delta = cellsInRow + 1
                val topRightCell = cells[positions[i + delta]]!!
                cell.topRight = topRightCell
                topRightCell.bottomLeft = cell
            }

            // BOTTOM_RIGHT
            if (i % cellsInRow != 0 && !bottomLine.contains(i)) {
                var delta = 1
                if (i % 2 != 0) delta = -(cellsInRow - 1)
                val bottomRightCell = cells[positions[i + delta]]!!
                cell.bottomRight = bottomRightCell
                bottomRightCell.topLeft = cell
            }
        }
    }

    private fun createInitialPieces() {
        val cellsNumber = getBoardCellsNumber(size)
        val piecesNumber = getInitialPiecesNumberOnBoard(size)
        for (i in 1..piecesNumber) {
            if (i <= piecesNumber / 2) {
                Piece(cells[positions[i]]!!, Piece.Color.A)
            } else {
                Piece(cells[positions[cellsNumber - piecesNumber + i]]!!, Piece.Color.B)
            }
        }
    }

    private fun ensureExistsCellWithName(cellName: String) {
        if (!cells.containsKey(cellName)) {
            throw IllegalArgumentException("Cell \"${cellName}\" does not exists on the board.")
        }
    }

    private fun getPieceInCell(cell: Cell): Piece? {
        return cell.piece
    }

    /** public methods */

    fun getPieceInCell(cellName: String): Piece? {
        ensureExistsCellWithName(cellName)
        return getPieceInCell(cells[cellName]!!)
    }

    fun addPiece(cell: Cell, color: Piece.Color, isKing: Boolean = false): Piece {
        return Piece(cell, color, isKing)
    }

    fun addPiece(cellName: String, color: Piece.Color, isKing: Boolean = false): Piece {
        ensureExistsCellWithName(cellName)
        return addPiece(cells[cellName]!!, color, isKing)
    }

    fun clear() {
        cells.values.forEach { cell -> cell.clear() }
    }

    fun clearCell(cell: Cell) {
        cell.clear()
    }

    fun clearCell(cellName: String) {
        ensureExistsCellWithName(cellName)
        cells[cellName]!!.clear()
    }

    fun isCellEmpty(cell: Cell): Boolean {
        return cell.isEmpty()
    }

    fun isCellEmpty(cellName: String): Boolean {
        ensureExistsCellWithName(cellName)
        return isCellEmpty(cells[cellName]!!)
    }

    fun isCellExists(cell: Cell): Boolean {
        return cells.containsValue(cell)
    }

    fun isCellExists(cellName: String): Boolean {
        return cells.containsKey(cellName)
    }

    fun isCellExistsAndEmpty(cell: Cell?): Boolean {
        if (cell == null) {
            return false
        }
        return isCellEmpty(cell)
    }

    fun isCellExistsAndEmpty(cellName: String): Boolean {
        return isCellExistsAndEmpty(cells[cellName])
    }

    fun getAvailableMoves(): List<Move> {
        return getAvailableMoves(Piece.Color.A) + getAvailableMoves(Piece.Color.B)
    }

    private fun getAvailableCaptureMoves(): List<Move> {
        val moves: MutableList<Move> = mutableListOf()
        cells.values.forEach { cell ->
            if (cell.isNotEmpty()) {
                moves += cell.piece!!.getAvailableCaptureMoves()
            }
        }
        return moves
    }

    fun getAvailableMoves(color: Piece.Color): List<Move> {
        val moves: MutableList<Move> = mutableListOf()
        cells.values.forEach { cell ->
            if (cell.isNotEmpty() && cell.piece!!.color == color) {
                moves += cell.piece!!.getAvailableCaptureMoves()
            }
        }
        if (moves.isNotEmpty()) {
            return moves
        }

        cells.values.forEach { cell ->
            if (cell.isNotEmpty() && cell.piece!!.color == color) {
                moves += cell.piece!!.getAvailableRegularMoves()
            }
        }
        return moves
    }

    fun isValidMove(targetMove: Move, color: Piece.Color? = null): Boolean {
        val availableMoves = if (color == null) getAvailableMoves() else getAvailableMoves(color)
        for (move in availableMoves) {
            if (targetMove.toString() == move.toString()) {
                return true
            }
        }
        return false
    }

    fun isValidMove(moveString: String, color: Piece.Color? = null): Boolean {
        if (!isMoveStringRepresentationValid(moveString)) {
            return false
        }
        val availableMoves = if (color == null) getAvailableMoves() else getAvailableMoves(color)
        for (move in availableMoves) {
            if (moveString == move.toString()) {
                return true
            }
        }
        return false
    }

    /**
     * Build Move object from string representation
     */
    fun buildMove(moveRepresentation: String): Move {
        if (!isMoveStringRepresentationValid(moveRepresentation)) {
            throw IllegalArgumentException("Incorrect move representation \"${moveRepresentation}\".")
        }
        for (move in getAvailableMoves()) {
            if (moveRepresentation == move.toString()) {
                return move
            }
        }
        throw NoSuchElementException("Incorrect move representation \"${moveRepresentation}\".")
    }

    /**
     * Execute only one single move without previous/next parts
     */
    fun executeOneMove(move: Move, removeCapturedPiecesAtEnd: Boolean = true) {
        val piece = cells[move.srcCell.name]!!.piece
        move.dstCell.piece = piece
        if (move.dstPieceIsKing) {
            piece!!.isKing = true
        }
        move.srcCell.piece = null
        if (removeCapturedPiecesAtEnd && !move.hasNext()) {
            removeCapturedPieces(move)
        }
    }

    /**
     * Execute full move (all parts, from root to end)
     */
    fun executeMove(move: Move) {
        if (!isValidMove(move)) {
            throw NoSuchElementException("Incorrect move \"${move}\".")
        }
        var tmpMove: Move? = move.root()
        while (tmpMove != null) {
            executeOneMove(tmpMove)
            tmpMove = tmpMove.next
        }
    }

    /**
     * Rollback only one single move without previous/next parts
     */
    fun rollbackOneMove(move: Move) {
        val piece = move.dstCell.piece
        move.srcCell.piece = piece
        if (piece != null && !move.srcPieceIsKing) {
            piece.isKing = false
        }
        if (move.capturePiece != null) {
            move.capturePiece!!.cell.piece = move.capturePiece
        }
        move.dstCell.piece = null
    }

    /**
     * Rollback full move (all parts, from end to root)
     */
    fun rollbackMove(move: Move) {
        var tmpMove: Move? = move.end()
        while (tmpMove != null) {
            rollbackOneMove(tmpMove)
            tmpMove = tmpMove.previous
        }
    }

    fun removeCapturedPieces(move: Move) {
        var currentMove: Move? = move
        while (currentMove != null) {
            if (currentMove.capturePiece != null) {
                currentMove.capturePiece!!.removeFromBoard()
            }
            currentMove = currentMove.previous
        }
    }

    fun copy(): Board {
        val boardCopy = Board(size)
        cells.values.forEach { cell ->
            if (!cell.isEmpty()) {
                cell.piece!!.copyTo(boardCopy)
            }
        }
        return boardCopy
    }

    /**
     * Method not ready yet
     */
    fun isDrawCondition(): Boolean {
        val capturedMoves = getAvailableCaptureMoves()
        if (capturedMoves.isNotEmpty()) return false

        val state = buildBoardState(this)
        if (state.kingCountA in 1..2 && state.kingCountB == 1 && state.menCountA == 0 && state.menCountB == 0) {
            return true
        }

        if (state.kingCountA == 1 && state.kingCountB in 1..2 && state.menCountA == 0 && state.menCountB == 0) {
            return true
        }

        return false
    }

    /**
     * Create HC-FEN string representation of board (based on FEN)
     * @see <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">https://en.wikipedia.org/wiki/Forsyth–Edwards_Notation</a>
     * @return String
     */
    fun toHcFen(): String {
        return renderToHcFen(this)
    }

    override fun toString(): String {
        return toHcFen()
    }

    /**
     * Create Ascii-string representation of board
     * @return String
     */
    fun toAscii(): String {
        return renderToAscii(this)
    }
}

fun getBoardColsNumber(size: Board.Size): Int {
    return when (size) {
        Board.Size.SIZE_6X6, Board.Size.SIZE_6X8 -> 6
        Board.Size.SIZE_8X6, Board.Size.SIZE_8X8, Board.Size.SIZE_8X10 -> 8
        Board.Size.SIZE_10X8, Board.Size.SIZE_10X10 -> 10
    }
}

fun getBoardRowsNumber(size: Board.Size): Int {
    return when (size) {
        Board.Size.SIZE_6X6, Board.Size.SIZE_8X6 -> 6
        Board.Size.SIZE_6X8, Board.Size.SIZE_8X8, Board.Size.SIZE_10X8 -> 8
        Board.Size.SIZE_8X10, Board.Size.SIZE_10X10 -> 10
    }
}

fun getBoardCellsNumber(size: Board.Size): Int {
    return getBoardRowsNumber(size) * getBoardColsNumber(size)
}

fun getInitialPiecesNumberOnBoard(size: Board.Size): Int {
    return when (size) {
        Board.Size.SIZE_6X6 -> 24
        Board.Size.SIZE_8X6 -> 32
        Board.Size.SIZE_6X8 -> 36
        Board.Size.SIZE_8X8 -> 48
        Board.Size.SIZE_10X8 -> 60
        Board.Size.SIZE_8X10 -> 64
        Board.Size.SIZE_10X10 -> 80
    }
}

fun getPieceSignInCellId(board: Board, cellId: Int): String {
    val piece: Piece = board.getPieceInCell(board.positions[cellId]) ?: return " "
    return piece.getSign()
}

fun getAllMoveDirections(): List<Board.Direction> {
    return getTopMoveDirections() + getBottomMoveDirections()
}

fun getTopMoveDirections(): List<Board.Direction> {
    return listOf(Board.Direction.TOP_LEFT, Board.Direction.TOP_MIDDLE, Board.Direction.TOP_RIGHT)
}

fun getBottomMoveDirections(): List<Board.Direction> {
    return listOf(Board.Direction.BOTTOM_LEFT, Board.Direction.BOTTOM_MIDDLE, Board.Direction.BOTTOM_RIGHT)
}

fun getInvertedDirection(direction: Board.Direction): Board.Direction {
    return when (direction) {
        Board.Direction.TOP_LEFT -> Board.Direction.BOTTOM_RIGHT
        Board.Direction.TOP_MIDDLE -> Board.Direction.BOTTOM_MIDDLE
        Board.Direction.TOP_RIGHT -> Board.Direction.BOTTOM_LEFT
        Board.Direction.BOTTOM_LEFT -> Board.Direction.TOP_RIGHT
        Board.Direction.BOTTOM_MIDDLE -> Board.Direction.TOP_MIDDLE
        Board.Direction.BOTTOM_RIGHT -> Board.Direction.TOP_LEFT
    }
}

fun findTwinCellOnBoard(targetCell: Cell, board: Board): Cell {
    for (cell in board.cells.values) {
        if (targetCell.name == cell.name) {
            return cell
        }
    }
    throw Exception("Twin cell \"$targetCell\" not found on target board.")
}

fun findTwinPieceOnBoard(targetPiece: Piece, board: Board): Piece {
    for (cell in board.cells.values) {
        if (!cell.isEmpty() && targetPiece.cell.name == cell.name) {
            return cell.piece!!
        }
    }
    throw Exception("Twin piece \"$targetPiece\" not found on target board.")
}

fun assignMoveToBoard(move: Move, board: Board): Move {
    var tmpMove: Move? = move.root()
    while (tmpMove != null) {
        tmpMove.srcCell = findTwinCellOnBoard(tmpMove.srcCell, board)
        tmpMove.piece = tmpMove.srcCell.piece
        tmpMove.dstCell = findTwinCellOnBoard(tmpMove.dstCell, board)
        if (tmpMove.capturePiece != null) {
            tmpMove.capturePiece = findTwinPieceOnBoard(tmpMove.capturePiece!!, board)
        }
        tmpMove = tmpMove.next
    }
    return move
}

fun assignMovesToBoard(moves: List<Move>, board: Board): List<Move> {
    for (move in moves) {
        assignMoveToBoard(move, board)
    }
    return moves
}
