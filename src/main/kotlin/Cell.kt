package com.hexcheckers.engine

class Cell(val board: Board, val row: Int, val col: Int) {
    val name: String = getCellNameByCoordinates(row, col)
    var piece: Piece? = null
        set(value) {
            field = value
            value?.cell = this
        }
    var topLeft: Cell? = null
    var topMiddle: Cell? = null
    var topRight: Cell? = null
    var bottomLeft: Cell? = null
    var bottomMiddle: Cell? = null
    var bottomRight: Cell? = null

    fun isKingCellForColor(color: Piece.Color): Boolean {
        return generateKingCells(color).contains(name)
    }

    private fun generateKingCells(color: Piece.Color): List<String> {
        val cells: MutableList<String> = if (color == Piece.Color.A) mutableListOf("b10", "d10", "f10", "h10", "j10") else mutableListOf("a1", "c1", "e1", "g1", "i1")
        if (color == Piece.Color.A && getBoardRowsNumber(board.size) == 8) {
            cells += listOf("b8", "d8", "f8", "h8", "j8")
        }
        if (color == Piece.Color.A && getBoardRowsNumber(board.size) == 6) {
            cells += listOf("b6", "d6", "f6", "h6", "j6")
        }
        return cells
    }

    override fun toString(): String {
        return name
    }

    fun isEmpty(): Boolean {
        return piece == null
    }

    fun isNotEmpty(): Boolean {
        return piece != null
    }

    fun getSiblingCell(direction: Board.Direction): Cell? {
        return when (direction) {
            Board.Direction.TOP_LEFT -> topLeft
            Board.Direction.TOP_MIDDLE -> topMiddle
            Board.Direction.TOP_RIGHT -> topRight
            Board.Direction.BOTTOM_LEFT -> bottomLeft
            Board.Direction.BOTTOM_MIDDLE -> bottomMiddle
            Board.Direction.BOTTOM_RIGHT -> bottomRight
        }
    }

    fun clear() {
        piece = null
    }
}

fun getCellNameByCoordinates(row: Int, col: Int): String {
    val letters = arrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j')
    return letters[col - 1].toString() + row.toString()
}
