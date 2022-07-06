package com.hexcheckers.engine

import kotlin.test.*
import com.hexcheckers.engine.helper.makeBoardFromHcFen

internal class BoardTest {
    @Test
    fun testBoardSize() {
        var board = Board(Board.Size.SIZE_6X6)
        assertEquals(Board.Size.SIZE_6X6, board.size)

        board = Board(Board.Size.SIZE_6X8)
        assertEquals(Board.Size.SIZE_6X8, board.size)

        board = Board(Board.Size.SIZE_8X6)
        assertEquals(Board.Size.SIZE_8X6, board.size)

        board = Board(Board.Size.SIZE_8X8)
        assertEquals(Board.Size.SIZE_8X8, board.size)

        board = Board(Board.Size.SIZE_8X10)
        assertEquals(Board.Size.SIZE_8X10, board.size)

        board = Board(Board.Size.SIZE_10X8)
        assertEquals(Board.Size.SIZE_10X8, board.size)

        board = Board(Board.Size.SIZE_10X10)
        assertEquals(Board.Size.SIZE_10X10, board.size)
    }

    @Test
    fun testEmpty6x6BoardRender() {
        val board = Board(Board.Size.SIZE_6X6)
        assertEquals(getEmptyBoard6x6(), board.toHcFen())
    }

    @Test
    fun testDefault6x6BoardRender() {
        var board = Board(Board.Size.SIZE_6X6, true)
        assertEquals(getDefaultBoard6x6(), board.toHcFen())

        board = Board(Board.Size.SIZE_6X8, true)
        assertEquals(getDefaultBoard6x8(), board.toHcFen())

        board = Board(Board.Size.SIZE_8X6, true)
        assertEquals(getDefaultBoard8x6(), board.toHcFen())

        board = Board(Board.Size.SIZE_8X8, true)
        assertEquals(getDefaultBoard8x8(), board.toHcFen())

        board = Board(Board.Size.SIZE_8X10, true)
        assertEquals(getDefaultBoard8x10(), board.toHcFen())

        board = Board(Board.Size.SIZE_10X8, true)
        assertEquals(getDefaultBoard10x8(), board.toHcFen())

        board = Board(Board.Size.SIZE_10X10, true)
        assertEquals(getDefaultBoard10x10(), board.toHcFen())
    }

    @Test
    fun testCustomBoardRender() {
        val board = Board(Board.Size.SIZE_6X6)
        board.addPiece("a1", Piece.Color.A)
        board.addPiece("a2", Piece.Color.A)
        board.addPiece("d4", Piece.Color.A)
        board.addPiece("c5", Piece.Color.B, true)
        board.addPiece("f6", Piece.Color.B)
        board.addPiece("e6", Piece.Color.B)

        assertEquals(getCustomBoard6x6_1(), board.toHcFen())
    }

    @Test
    fun testBoardCopy() {
        val board = Board(Board.Size.SIZE_6X6)
        board.addPiece("a1", Piece.Color.A)
        board.addPiece("a2", Piece.Color.A)
        board.addPiece("d4", Piece.Color.A)
        board.addPiece("c5", Piece.Color.B, true)
        board.addPiece("f6", Piece.Color.B)
        board.addPiece("e6", Piece.Color.B)

        assertEquals(getCustomBoard6x6_1(), board.toHcFen())

        val boardCopy = board.copy()
        assertEquals(Board.Size.SIZE_6X6, boardCopy.size)
        assertEquals(getCustomBoard6x6_1(), boardCopy.toHcFen())
    }

    @Test
    fun testMakeEmpty6x6BoardBoardFromString() {
        val board = makeBoardFromHcFen(getEmptyBoard6x6())
        assertEquals(Board.Size.SIZE_6X6, board.size)
        assertEquals(getEmptyBoard6x6(), board.toHcFen())
    }

    @Test
    fun testMakeDefault6x6BoardBoardFromString() {
        val board = makeBoardFromHcFen(getDefaultBoard6x6())
        assertEquals(Board.Size.SIZE_6X6, board.size)
        assertEquals(getDefaultBoard6x6(), board.toHcFen())
    }

    @Test
    fun testMakeCustom6x6BoardBoardFromString() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_1())
        assertEquals(Board.Size.SIZE_6X6, board.size)
        assertEquals(getCustomBoard6x6_1(), board.toHcFen())
        assertEquals("d4", board.getPieceInCell("d4")!!.getCellName())
        assertEquals(Piece.Color.A, board.getPieceInCell("d4")!!.color)
        assertEquals(false, board.getPieceInCell("d4")!!.isKing)
        assertEquals("c5", board.getPieceInCell("c5")!!.getCellName())
        assertEquals(Piece.Color.B, board.getPieceInCell("c5")!!.color)
        assertEquals(true, board.getPieceInCell("c5")!!.isKing)
    }

    @Test
    fun testMakeCustom6x8BoardBoardFromIncorrectString() {
        assertFailsWith<IllegalArgumentException> {
            makeBoardFromHcFen("6x8/a-----b/a//aa/a-bb/b--bba/b/")
        }
    }

    @Test
    fun testGetPieceInCell1() {
        var board = Board(Board.Size.SIZE_6X6, true)
        assertEquals("a@a1", board.getPieceInCell("a1").toString())
        assertEquals("b@a6", board.getPieceInCell("a6").toString())

        board = Board(Board.Size.SIZE_6X8, true)
        assertEquals("b@f8", board.getPieceInCell("f8").toString())

        assertFailsWith<IllegalArgumentException> {
            assertEquals("b@g8", board.getPieceInCell("g8").toString())
        }

        board = Board(Board.Size.SIZE_8X6, true)
        assertEquals("b@h6", board.getPieceInCell("h6").toString())

        assertFailsWith<IllegalArgumentException> {
            assertEquals("b@h8", board.getPieceInCell("h8").toString())
        }

        board = Board(Board.Size.SIZE_8X8, true)
        assertEquals("b@h8", board.getPieceInCell("h8").toString())

        assertFailsWith<IllegalArgumentException> {
            assertEquals("b@h10", board.getPieceInCell("h10").toString())
        }

        board = Board(Board.Size.SIZE_8X10, true)
        assertEquals("b@h10", board.getPieceInCell("h10").toString())

        assertFailsWith<IllegalArgumentException> {
            assertEquals("b@j10", board.getPieceInCell("j10").toString())
        }

        board = Board(Board.Size.SIZE_10X8, true)
        assertEquals("b@j8", board.getPieceInCell("j8").toString())

        assertFailsWith<IllegalArgumentException> {
            assertEquals("b@j10", board.getPieceInCell("j10").toString())
        }

        board = Board(Board.Size.SIZE_10X10, true)
        assertEquals("b@j10", board.getPieceInCell("j10").toString())
    }

    @Test
    fun testClearBoard() {
        val board = Board(Board.Size.SIZE_6X6, true)
        assertEquals(getDefaultBoard6x6(), board.toHcFen())

        board.clear()
        assertEquals(getEmptyBoard6x6(), board.toHcFen())
    }

    @Test
    fun testDrawCondition1() {
        val board = Board(Board.Size.SIZE_6X6, true)
        assertFalse(board.isDrawCondition())
    }

    @Test
    fun testDrawCondition2() {
        val board = makeBoardFromHcFen("6x6/a/////b")
        assertFalse(board.isDrawCondition())
    }

    @Test
    fun testDrawCondition3() {
        val board = makeBoardFromHcFen("6x6/A/////-----B")
        assertTrue(board.isDrawCondition())
    }

    @Test
    fun testDrawCondition4() {
        val board = makeBoardFromHcFen("6x6/AA/////-----B")
        assertTrue(board.isDrawCondition())
    }

    @Test
    fun testDrawCondition5() {
        val board = makeBoardFromHcFen("6x6/a/////bb")
        assertFalse(board.isDrawCondition())
    }

    @Test
    fun testDrawCondition6() {
        val board = makeBoardFromHcFen("6x6/a/b////")
        assertFalse(board.isDrawCondition())
    }

    @Test
    fun testLinkCellsOfBoard6x6() {
        val board = Board(Board.Size.SIZE_6X6)
        assertEquals("a2b1        ", getCellSiblingsAsString(board.cells["a1"]!!))
        assertEquals("b2c2c1  a1a2", getCellSiblingsAsString(board.cells["b1"]!!))
        assertEquals("c2d1      b1", getCellSiblingsAsString(board.cells["c1"]!!))
        assertEquals("d2e2e1  c1c2", getCellSiblingsAsString(board.cells["d1"]!!))
        assertEquals("e2f1      d1", getCellSiblingsAsString(board.cells["e1"]!!))
        assertEquals("f2      e1e2", getCellSiblingsAsString(board.cells["f1"]!!))
        assertEquals("a3b2b1a1    ", getCellSiblingsAsString(board.cells["a2"]!!))
        assertEquals("b3c3c2b1a2a3", getCellSiblingsAsString(board.cells["b2"]!!))
        assertEquals("c3d2d1c1b1b2", getCellSiblingsAsString(board.cells["c2"]!!))
        assertEquals("d3e3e2d1c2c3", getCellSiblingsAsString(board.cells["d2"]!!))
        assertEquals("e3f2f1e1d1d2", getCellSiblingsAsString(board.cells["e2"]!!))
        assertEquals("f3    f1e2e3", getCellSiblingsAsString(board.cells["f2"]!!))
        assertEquals("a4b3b2a2    ", getCellSiblingsAsString(board.cells["a3"]!!))
        assertEquals("b4c4c3b2a3a4", getCellSiblingsAsString(board.cells["b3"]!!))
        assertEquals("c4d3d2c2b2b3", getCellSiblingsAsString(board.cells["c3"]!!))
        assertEquals("d4e4e3d2c3c4", getCellSiblingsAsString(board.cells["d3"]!!))
        assertEquals("e4f3f2e2d2d3", getCellSiblingsAsString(board.cells["e3"]!!))
        assertEquals("f4    f2e3e4", getCellSiblingsAsString(board.cells["f3"]!!))
        assertEquals("a5b4b3a3    ", getCellSiblingsAsString(board.cells["a4"]!!))
        assertEquals("b5c5c4b3a4a5", getCellSiblingsAsString(board.cells["b4"]!!))
        assertEquals("c5d4d3c3b3b4", getCellSiblingsAsString(board.cells["c4"]!!))
        assertEquals("d5e5e4d3c4c5", getCellSiblingsAsString(board.cells["d4"]!!))
        assertEquals("e5f4f3e3d3d4", getCellSiblingsAsString(board.cells["e4"]!!))
        assertEquals("f5    f3e4e5", getCellSiblingsAsString(board.cells["f4"]!!))
        assertEquals("a6b5b4a4    ", getCellSiblingsAsString(board.cells["a5"]!!))
        assertEquals("b6c6c5b4a5a6", getCellSiblingsAsString(board.cells["b5"]!!))
        assertEquals("c6d5d4c4b4b5", getCellSiblingsAsString(board.cells["c5"]!!))
        assertEquals("d6e6e5d4c5c6", getCellSiblingsAsString(board.cells["d5"]!!))
        assertEquals("e6f5f4e4d4d5", getCellSiblingsAsString(board.cells["e5"]!!))
        assertEquals("f6    f4e5e6", getCellSiblingsAsString(board.cells["f5"]!!))
        assertEquals("  b6b5a5    ", getCellSiblingsAsString(board.cells["a6"]!!))
        assertEquals("    c6b5a6  ", getCellSiblingsAsString(board.cells["b6"]!!))
        assertEquals("  d6d5c5b5b6", getCellSiblingsAsString(board.cells["c6"]!!))
        assertEquals("    e6d5c6  ", getCellSiblingsAsString(board.cells["d6"]!!))
        assertEquals("  f6f5e5d5d6", getCellSiblingsAsString(board.cells["e6"]!!))
        assertEquals("      f5e6  ", getCellSiblingsAsString(board.cells["f6"]!!))
    }

    @Test
    fun testLinkCellsOfBoard6x8() {
        val board = Board(Board.Size.SIZE_6X8)
        assertEquals("a2b1        ", getCellSiblingsAsString(board.cells["a1"]!!))
        assertEquals("b2c2c1  a1a2", getCellSiblingsAsString(board.cells["b1"]!!))
        assertEquals("c2d1      b1", getCellSiblingsAsString(board.cells["c1"]!!))
        assertEquals("d2e2e1  c1c2", getCellSiblingsAsString(board.cells["d1"]!!))
        assertEquals("e2f1      d1", getCellSiblingsAsString(board.cells["e1"]!!))
        assertEquals("f2      e1e2", getCellSiblingsAsString(board.cells["f1"]!!))
        assertEquals("a3b2b1a1    ", getCellSiblingsAsString(board.cells["a2"]!!))
        assertEquals("b3c3c2b1a2a3", getCellSiblingsAsString(board.cells["b2"]!!))
        assertEquals("c3d2d1c1b1b2", getCellSiblingsAsString(board.cells["c2"]!!))
        assertEquals("d3e3e2d1c2c3", getCellSiblingsAsString(board.cells["d2"]!!))
        assertEquals("e3f2f1e1d1d2", getCellSiblingsAsString(board.cells["e2"]!!))
        assertEquals("f3    f1e2e3", getCellSiblingsAsString(board.cells["f2"]!!))
        assertEquals("a4b3b2a2    ", getCellSiblingsAsString(board.cells["a3"]!!))
        assertEquals("b4c4c3b2a3a4", getCellSiblingsAsString(board.cells["b3"]!!))
        assertEquals("c4d3d2c2b2b3", getCellSiblingsAsString(board.cells["c3"]!!))
        assertEquals("d4e4e3d2c3c4", getCellSiblingsAsString(board.cells["d3"]!!))
        assertEquals("e4f3f2e2d2d3", getCellSiblingsAsString(board.cells["e3"]!!))
        assertEquals("f4    f2e3e4", getCellSiblingsAsString(board.cells["f3"]!!))
        assertEquals("a5b4b3a3    ", getCellSiblingsAsString(board.cells["a4"]!!))
        assertEquals("b5c5c4b3a4a5", getCellSiblingsAsString(board.cells["b4"]!!))
        assertEquals("c5d4d3c3b3b4", getCellSiblingsAsString(board.cells["c4"]!!))
        assertEquals("d5e5e4d3c4c5", getCellSiblingsAsString(board.cells["d4"]!!))
        assertEquals("e5f4f3e3d3d4", getCellSiblingsAsString(board.cells["e4"]!!))
        assertEquals("f5    f3e4e5", getCellSiblingsAsString(board.cells["f4"]!!))
        assertEquals("a6b5b4a4    ", getCellSiblingsAsString(board.cells["a5"]!!))
        assertEquals("b6c6c5b4a5a6", getCellSiblingsAsString(board.cells["b5"]!!))
        assertEquals("c6d5d4c4b4b5", getCellSiblingsAsString(board.cells["c5"]!!))
        assertEquals("d6e6e5d4c5c6", getCellSiblingsAsString(board.cells["d5"]!!))
        assertEquals("e6f5f4e4d4d5", getCellSiblingsAsString(board.cells["e5"]!!))
        assertEquals("f6    f4e5e6", getCellSiblingsAsString(board.cells["f5"]!!))
        assertEquals("a7b6b5a5    ", getCellSiblingsAsString(board.cells["a6"]!!))
        assertEquals("b7c7c6b5a6a7", getCellSiblingsAsString(board.cells["b6"]!!))
        assertEquals("c7d6d5c5b5b6", getCellSiblingsAsString(board.cells["c6"]!!))
        assertEquals("d7e7e6d5c6c7", getCellSiblingsAsString(board.cells["d6"]!!))
        assertEquals("e7f6f5e5d5d6", getCellSiblingsAsString(board.cells["e6"]!!))
        assertEquals("f7    f5e6e7", getCellSiblingsAsString(board.cells["f6"]!!))
        assertEquals("a8b7b6a6    ", getCellSiblingsAsString(board.cells["a7"]!!))
        assertEquals("b8c8c7b6a7a8", getCellSiblingsAsString(board.cells["b7"]!!))
        assertEquals("c8d7d6c6b6b7", getCellSiblingsAsString(board.cells["c7"]!!))
        assertEquals("d8e8e7d6c7c8", getCellSiblingsAsString(board.cells["d7"]!!))
        assertEquals("e8f7f6e6d6d7", getCellSiblingsAsString(board.cells["e7"]!!))
        assertEquals("f8    f6e7e8", getCellSiblingsAsString(board.cells["f7"]!!))
        assertEquals("  b8b7a7    ", getCellSiblingsAsString(board.cells["a8"]!!))
        assertEquals("    c8b7a8  ", getCellSiblingsAsString(board.cells["b8"]!!))
        assertEquals("  d8d7c7b7b8", getCellSiblingsAsString(board.cells["c8"]!!))
        assertEquals("    e8d7c8  ", getCellSiblingsAsString(board.cells["d8"]!!))
        assertEquals("  f8f7e7d7d8", getCellSiblingsAsString(board.cells["e8"]!!))
        assertEquals("      f7e8  ", getCellSiblingsAsString(board.cells["f8"]!!))
    }

    @Test
    fun testLinkCellsOfBoard8x6() {
        val board = Board(Board.Size.SIZE_8X6)
        assertEquals("a2b1        ", getCellSiblingsAsString(board.cells["a1"]!!))
        assertEquals("b2c2c1  a1a2", getCellSiblingsAsString(board.cells["b1"]!!))
        assertEquals("c2d1      b1", getCellSiblingsAsString(board.cells["c1"]!!))
        assertEquals("d2e2e1  c1c2", getCellSiblingsAsString(board.cells["d1"]!!))
        assertEquals("e2f1      d1", getCellSiblingsAsString(board.cells["e1"]!!))
        assertEquals("f2g2g1  e1e2", getCellSiblingsAsString(board.cells["f1"]!!))
        assertEquals("g2h1      f1", getCellSiblingsAsString(board.cells["g1"]!!))
        assertEquals("h2      g1g2", getCellSiblingsAsString(board.cells["h1"]!!))
        assertEquals("a3b2b1a1    ", getCellSiblingsAsString(board.cells["a2"]!!))
        assertEquals("b3c3c2b1a2a3", getCellSiblingsAsString(board.cells["b2"]!!))
        assertEquals("c3d2d1c1b1b2", getCellSiblingsAsString(board.cells["c2"]!!))
        assertEquals("d3e3e2d1c2c3", getCellSiblingsAsString(board.cells["d2"]!!))
        assertEquals("e3f2f1e1d1d2", getCellSiblingsAsString(board.cells["e2"]!!))
        assertEquals("f3g3g2f1e2e3", getCellSiblingsAsString(board.cells["f2"]!!))
        assertEquals("g3h2h1g1f1f2", getCellSiblingsAsString(board.cells["g2"]!!))
        assertEquals("h3    h1g2g3", getCellSiblingsAsString(board.cells["h2"]!!))
        assertEquals("a4b3b2a2    ", getCellSiblingsAsString(board.cells["a3"]!!))
        assertEquals("b4c4c3b2a3a4", getCellSiblingsAsString(board.cells["b3"]!!))
        assertEquals("c4d3d2c2b2b3", getCellSiblingsAsString(board.cells["c3"]!!))
        assertEquals("d4e4e3d2c3c4", getCellSiblingsAsString(board.cells["d3"]!!))
        assertEquals("e4f3f2e2d2d3", getCellSiblingsAsString(board.cells["e3"]!!))
        assertEquals("f4g4g3f2e3e4", getCellSiblingsAsString(board.cells["f3"]!!))
        assertEquals("g4h3h2g2f2f3", getCellSiblingsAsString(board.cells["g3"]!!))
        assertEquals("h4    h2g3g4", getCellSiblingsAsString(board.cells["h3"]!!))
        assertEquals("a5b4b3a3    ", getCellSiblingsAsString(board.cells["a4"]!!))
        assertEquals("b5c5c4b3a4a5", getCellSiblingsAsString(board.cells["b4"]!!))
        assertEquals("c5d4d3c3b3b4", getCellSiblingsAsString(board.cells["c4"]!!))
        assertEquals("d5e5e4d3c4c5", getCellSiblingsAsString(board.cells["d4"]!!))
        assertEquals("e5f4f3e3d3d4", getCellSiblingsAsString(board.cells["e4"]!!))
        assertEquals("f5g5g4f3e4e5", getCellSiblingsAsString(board.cells["f4"]!!))
        assertEquals("g5h4h3g3f3f4", getCellSiblingsAsString(board.cells["g4"]!!))
        assertEquals("h5    h3g4g5", getCellSiblingsAsString(board.cells["h4"]!!))
        assertEquals("a6b5b4a4    ", getCellSiblingsAsString(board.cells["a5"]!!))
        assertEquals("b6c6c5b4a5a6", getCellSiblingsAsString(board.cells["b5"]!!))
        assertEquals("c6d5d4c4b4b5", getCellSiblingsAsString(board.cells["c5"]!!))
        assertEquals("d6e6e5d4c5c6", getCellSiblingsAsString(board.cells["d5"]!!))
        assertEquals("e6f5f4e4d4d5", getCellSiblingsAsString(board.cells["e5"]!!))
        assertEquals("f6g6g5f4e5e6", getCellSiblingsAsString(board.cells["f5"]!!))
        assertEquals("g6h5h4g4f4f5", getCellSiblingsAsString(board.cells["g5"]!!))
        assertEquals("h6    h4g5g6", getCellSiblingsAsString(board.cells["h5"]!!))
        assertEquals("  b6b5a5    ", getCellSiblingsAsString(board.cells["a6"]!!))
        assertEquals("    c6b5a6  ", getCellSiblingsAsString(board.cells["b6"]!!))
        assertEquals("  d6d5c5b5b6", getCellSiblingsAsString(board.cells["c6"]!!))
        assertEquals("    e6d5c6  ", getCellSiblingsAsString(board.cells["d6"]!!))
        assertEquals("  f6f5e5d5d6", getCellSiblingsAsString(board.cells["e6"]!!))
        assertEquals("    g6f5e6  ", getCellSiblingsAsString(board.cells["f6"]!!))
        assertEquals("  h6h5g5f5f6", getCellSiblingsAsString(board.cells["g6"]!!))
        assertEquals("      h5g6  ", getCellSiblingsAsString(board.cells["h6"]!!))
    }

    @Test
    fun testLinkCellsOfBoard8x8() {
        val board = Board(Board.Size.SIZE_8X8)
        assertEquals("a2b1        ", getCellSiblingsAsString(board.cells["a1"]!!))
        assertEquals("b2c2c1  a1a2", getCellSiblingsAsString(board.cells["b1"]!!))
        assertEquals("c2d1      b1", getCellSiblingsAsString(board.cells["c1"]!!))
        assertEquals("d2e2e1  c1c2", getCellSiblingsAsString(board.cells["d1"]!!))
        assertEquals("e2f1      d1", getCellSiblingsAsString(board.cells["e1"]!!))
        assertEquals("f2g2g1  e1e2", getCellSiblingsAsString(board.cells["f1"]!!))
        assertEquals("g2h1      f1", getCellSiblingsAsString(board.cells["g1"]!!))
        assertEquals("h2      g1g2", getCellSiblingsAsString(board.cells["h1"]!!))
        assertEquals("a3b2b1a1    ", getCellSiblingsAsString(board.cells["a2"]!!))
        assertEquals("b3c3c2b1a2a3", getCellSiblingsAsString(board.cells["b2"]!!))
        assertEquals("c3d2d1c1b1b2", getCellSiblingsAsString(board.cells["c2"]!!))
        assertEquals("d3e3e2d1c2c3", getCellSiblingsAsString(board.cells["d2"]!!))
        assertEquals("e3f2f1e1d1d2", getCellSiblingsAsString(board.cells["e2"]!!))
        assertEquals("f3g3g2f1e2e3", getCellSiblingsAsString(board.cells["f2"]!!))
        assertEquals("g3h2h1g1f1f2", getCellSiblingsAsString(board.cells["g2"]!!))
        assertEquals("h3    h1g2g3", getCellSiblingsAsString(board.cells["h2"]!!))
        assertEquals("a4b3b2a2    ", getCellSiblingsAsString(board.cells["a3"]!!))
        assertEquals("b4c4c3b2a3a4", getCellSiblingsAsString(board.cells["b3"]!!))
        assertEquals("c4d3d2c2b2b3", getCellSiblingsAsString(board.cells["c3"]!!))
        assertEquals("d4e4e3d2c3c4", getCellSiblingsAsString(board.cells["d3"]!!))
        assertEquals("e4f3f2e2d2d3", getCellSiblingsAsString(board.cells["e3"]!!))
        assertEquals("f4g4g3f2e3e4", getCellSiblingsAsString(board.cells["f3"]!!))
        assertEquals("g4h3h2g2f2f3", getCellSiblingsAsString(board.cells["g3"]!!))
        assertEquals("h4    h2g3g4", getCellSiblingsAsString(board.cells["h3"]!!))
        assertEquals("a5b4b3a3    ", getCellSiblingsAsString(board.cells["a4"]!!))
        assertEquals("b5c5c4b3a4a5", getCellSiblingsAsString(board.cells["b4"]!!))
        assertEquals("c5d4d3c3b3b4", getCellSiblingsAsString(board.cells["c4"]!!))
        assertEquals("d5e5e4d3c4c5", getCellSiblingsAsString(board.cells["d4"]!!))
        assertEquals("e5f4f3e3d3d4", getCellSiblingsAsString(board.cells["e4"]!!))
        assertEquals("f5g5g4f3e4e5", getCellSiblingsAsString(board.cells["f4"]!!))
        assertEquals("g5h4h3g3f3f4", getCellSiblingsAsString(board.cells["g4"]!!))
        assertEquals("h5    h3g4g5", getCellSiblingsAsString(board.cells["h4"]!!))
        assertEquals("a6b5b4a4    ", getCellSiblingsAsString(board.cells["a5"]!!))
        assertEquals("b6c6c5b4a5a6", getCellSiblingsAsString(board.cells["b5"]!!))
        assertEquals("c6d5d4c4b4b5", getCellSiblingsAsString(board.cells["c5"]!!))
        assertEquals("d6e6e5d4c5c6", getCellSiblingsAsString(board.cells["d5"]!!))
        assertEquals("e6f5f4e4d4d5", getCellSiblingsAsString(board.cells["e5"]!!))
        assertEquals("f6g6g5f4e5e6", getCellSiblingsAsString(board.cells["f5"]!!))
        assertEquals("g6h5h4g4f4f5", getCellSiblingsAsString(board.cells["g5"]!!))
        assertEquals("h6    h4g5g6", getCellSiblingsAsString(board.cells["h5"]!!))
        assertEquals("a7b6b5a5    ", getCellSiblingsAsString(board.cells["a6"]!!))
        assertEquals("b7c7c6b5a6a7", getCellSiblingsAsString(board.cells["b6"]!!))
        assertEquals("c7d6d5c5b5b6", getCellSiblingsAsString(board.cells["c6"]!!))
        assertEquals("d7e7e6d5c6c7", getCellSiblingsAsString(board.cells["d6"]!!))
        assertEquals("e7f6f5e5d5d6", getCellSiblingsAsString(board.cells["e6"]!!))
        assertEquals("f7g7g6f5e6e7", getCellSiblingsAsString(board.cells["f6"]!!))
        assertEquals("g7h6h5g5f5f6", getCellSiblingsAsString(board.cells["g6"]!!))
        assertEquals("h7    h5g6g7", getCellSiblingsAsString(board.cells["h6"]!!))
        assertEquals("a8b7b6a6    ", getCellSiblingsAsString(board.cells["a7"]!!))
        assertEquals("b8c8c7b6a7a8", getCellSiblingsAsString(board.cells["b7"]!!))
        assertEquals("c8d7d6c6b6b7", getCellSiblingsAsString(board.cells["c7"]!!))
        assertEquals("d8e8e7d6c7c8", getCellSiblingsAsString(board.cells["d7"]!!))
        assertEquals("e8f7f6e6d6d7", getCellSiblingsAsString(board.cells["e7"]!!))
        assertEquals("f8g8g7f6e7e8", getCellSiblingsAsString(board.cells["f7"]!!))
        assertEquals("g8h7h6g6f6f7", getCellSiblingsAsString(board.cells["g7"]!!))
        assertEquals("h8    h6g7g8", getCellSiblingsAsString(board.cells["h7"]!!))
        assertEquals("  b8b7a7    ", getCellSiblingsAsString(board.cells["a8"]!!))
        assertEquals("    c8b7a8  ", getCellSiblingsAsString(board.cells["b8"]!!))
        assertEquals("  d8d7c7b7b8", getCellSiblingsAsString(board.cells["c8"]!!))
        assertEquals("    e8d7c8  ", getCellSiblingsAsString(board.cells["d8"]!!))
        assertEquals("  f8f7e7d7d8", getCellSiblingsAsString(board.cells["e8"]!!))
        assertEquals("    g8f7e8  ", getCellSiblingsAsString(board.cells["f8"]!!))
        assertEquals("  h8h7g7f7f8", getCellSiblingsAsString(board.cells["g8"]!!))
        assertEquals("      h7g8  ", getCellSiblingsAsString(board.cells["h8"]!!))
    }

    @Test
    fun testLinkCellsOfBoard8x10() {
        val board = Board(Board.Size.SIZE_8X10)
        assertEquals("a2b1        ", getCellSiblingsAsString(board.cells["a1"]!!))
        assertEquals("b2c2c1  a1a2", getCellSiblingsAsString(board.cells["b1"]!!))
        assertEquals("c2d1      b1", getCellSiblingsAsString(board.cells["c1"]!!))
        assertEquals("d2e2e1  c1c2", getCellSiblingsAsString(board.cells["d1"]!!))
        assertEquals("e2f1      d1", getCellSiblingsAsString(board.cells["e1"]!!))
        assertEquals("f2g2g1  e1e2", getCellSiblingsAsString(board.cells["f1"]!!))
        assertEquals("g2h1      f1", getCellSiblingsAsString(board.cells["g1"]!!))
        assertEquals("h2      g1g2", getCellSiblingsAsString(board.cells["h1"]!!))
        assertEquals("a3b2b1a1    ", getCellSiblingsAsString(board.cells["a2"]!!))
        assertEquals("b3c3c2b1a2a3", getCellSiblingsAsString(board.cells["b2"]!!))
        assertEquals("c3d2d1c1b1b2", getCellSiblingsAsString(board.cells["c2"]!!))
        assertEquals("d3e3e2d1c2c3", getCellSiblingsAsString(board.cells["d2"]!!))
        assertEquals("e3f2f1e1d1d2", getCellSiblingsAsString(board.cells["e2"]!!))
        assertEquals("f3g3g2f1e2e3", getCellSiblingsAsString(board.cells["f2"]!!))
        assertEquals("g3h2h1g1f1f2", getCellSiblingsAsString(board.cells["g2"]!!))
        assertEquals("h3    h1g2g3", getCellSiblingsAsString(board.cells["h2"]!!))
        assertEquals("a4b3b2a2    ", getCellSiblingsAsString(board.cells["a3"]!!))
        assertEquals("b4c4c3b2a3a4", getCellSiblingsAsString(board.cells["b3"]!!))
        assertEquals("c4d3d2c2b2b3", getCellSiblingsAsString(board.cells["c3"]!!))
        assertEquals("d4e4e3d2c3c4", getCellSiblingsAsString(board.cells["d3"]!!))
        assertEquals("e4f3f2e2d2d3", getCellSiblingsAsString(board.cells["e3"]!!))
        assertEquals("f4g4g3f2e3e4", getCellSiblingsAsString(board.cells["f3"]!!))
        assertEquals("g4h3h2g2f2f3", getCellSiblingsAsString(board.cells["g3"]!!))
        assertEquals("h4    h2g3g4", getCellSiblingsAsString(board.cells["h3"]!!))
        assertEquals("a5b4b3a3    ", getCellSiblingsAsString(board.cells["a4"]!!))
        assertEquals("b5c5c4b3a4a5", getCellSiblingsAsString(board.cells["b4"]!!))
        assertEquals("c5d4d3c3b3b4", getCellSiblingsAsString(board.cells["c4"]!!))
        assertEquals("d5e5e4d3c4c5", getCellSiblingsAsString(board.cells["d4"]!!))
        assertEquals("e5f4f3e3d3d4", getCellSiblingsAsString(board.cells["e4"]!!))
        assertEquals("f5g5g4f3e4e5", getCellSiblingsAsString(board.cells["f4"]!!))
        assertEquals("g5h4h3g3f3f4", getCellSiblingsAsString(board.cells["g4"]!!))
        assertEquals("h5    h3g4g5", getCellSiblingsAsString(board.cells["h4"]!!))
        assertEquals("a6b5b4a4    ", getCellSiblingsAsString(board.cells["a5"]!!))
        assertEquals("b6c6c5b4a5a6", getCellSiblingsAsString(board.cells["b5"]!!))
        assertEquals("c6d5d4c4b4b5", getCellSiblingsAsString(board.cells["c5"]!!))
        assertEquals("d6e6e5d4c5c6", getCellSiblingsAsString(board.cells["d5"]!!))
        assertEquals("e6f5f4e4d4d5", getCellSiblingsAsString(board.cells["e5"]!!))
        assertEquals("f6g6g5f4e5e6", getCellSiblingsAsString(board.cells["f5"]!!))
        assertEquals("g6h5h4g4f4f5", getCellSiblingsAsString(board.cells["g5"]!!))
        assertEquals("h6    h4g5g6", getCellSiblingsAsString(board.cells["h5"]!!))
        assertEquals("a7b6b5a5    ", getCellSiblingsAsString(board.cells["a6"]!!))
        assertEquals("b7c7c6b5a6a7", getCellSiblingsAsString(board.cells["b6"]!!))
        assertEquals("c7d6d5c5b5b6", getCellSiblingsAsString(board.cells["c6"]!!))
        assertEquals("d7e7e6d5c6c7", getCellSiblingsAsString(board.cells["d6"]!!))
        assertEquals("e7f6f5e5d5d6", getCellSiblingsAsString(board.cells["e6"]!!))
        assertEquals("f7g7g6f5e6e7", getCellSiblingsAsString(board.cells["f6"]!!))
        assertEquals("g7h6h5g5f5f6", getCellSiblingsAsString(board.cells["g6"]!!))
        assertEquals("h7    h5g6g7", getCellSiblingsAsString(board.cells["h6"]!!))
        assertEquals("a8b7b6a6    ", getCellSiblingsAsString(board.cells["a7"]!!))
        assertEquals("b8c8c7b6a7a8", getCellSiblingsAsString(board.cells["b7"]!!))
        assertEquals("c8d7d6c6b6b7", getCellSiblingsAsString(board.cells["c7"]!!))
        assertEquals("d8e8e7d6c7c8", getCellSiblingsAsString(board.cells["d7"]!!))
        assertEquals("e8f7f6e6d6d7", getCellSiblingsAsString(board.cells["e7"]!!))
        assertEquals("f8g8g7f6e7e8", getCellSiblingsAsString(board.cells["f7"]!!))
        assertEquals("g8h7h6g6f6f7", getCellSiblingsAsString(board.cells["g7"]!!))
        assertEquals("h8    h6g7g8", getCellSiblingsAsString(board.cells["h7"]!!))
        assertEquals("a9b8b7a7    ", getCellSiblingsAsString(board.cells["a8"]!!))
        assertEquals("b9c9c8b7a8a9", getCellSiblingsAsString(board.cells["b8"]!!))
        assertEquals("c9d8d7c7b7b8", getCellSiblingsAsString(board.cells["c8"]!!))
        assertEquals("d9e9e8d7c8c9", getCellSiblingsAsString(board.cells["d8"]!!))
        assertEquals("e9f8f7e7d7d8", getCellSiblingsAsString(board.cells["e8"]!!))
        assertEquals("f9g9g8f7e8e9", getCellSiblingsAsString(board.cells["f8"]!!))
        assertEquals("g9h8h7g7f7f8", getCellSiblingsAsString(board.cells["g8"]!!))
        assertEquals("h9    h7g8g9", getCellSiblingsAsString(board.cells["h8"]!!))
        assertEquals("a10b9b8a8    ", getCellSiblingsAsString(board.cells["a9"]!!))
        assertEquals("b10c10c9b8a9a10", getCellSiblingsAsString(board.cells["b9"]!!))
        assertEquals("c10d9d8c8b8b9", getCellSiblingsAsString(board.cells["c9"]!!))
        assertEquals("d10e10e9d8c9c10", getCellSiblingsAsString(board.cells["d9"]!!))
        assertEquals("e10f9f8e8d8d9", getCellSiblingsAsString(board.cells["e9"]!!))
        assertEquals("f10g10g9f8e9e10", getCellSiblingsAsString(board.cells["f9"]!!))
        assertEquals("g10h9h8g8f8f9", getCellSiblingsAsString(board.cells["g9"]!!))
        assertEquals("h10    h8g9g10", getCellSiblingsAsString(board.cells["h9"]!!))
        assertEquals( "  b10b9a9    ", getCellSiblingsAsString(board.cells["a10"]!!))
        assertEquals( "    c10b9a10  ", getCellSiblingsAsString(board.cells["b10"]!!))
        assertEquals( "  d10d9c9b9b10", getCellSiblingsAsString(board.cells["c10"]!!))
        assertEquals( "    e10d9c10  ", getCellSiblingsAsString(board.cells["d10"]!!))
        assertEquals( "  f10f9e9d9d10", getCellSiblingsAsString(board.cells["e10"]!!))
        assertEquals( "    g10f9e10  ", getCellSiblingsAsString(board.cells["f10"]!!))
        assertEquals( "  h10h9g9f9f10", getCellSiblingsAsString(board.cells["g10"]!!))
        assertEquals( "      h9g10  ", getCellSiblingsAsString(board.cells["h10"]!!))
    }

    @Test
    fun testLinkCellsOfBoard10x8() {
        val board = Board(Board.Size.SIZE_10X8)
        assertEquals("a2b1        ", getCellSiblingsAsString(board.cells["a1"]!!))
        assertEquals("b2c2c1  a1a2", getCellSiblingsAsString(board.cells["b1"]!!))
        assertEquals("c2d1      b1", getCellSiblingsAsString(board.cells["c1"]!!))
        assertEquals("d2e2e1  c1c2", getCellSiblingsAsString(board.cells["d1"]!!))
        assertEquals("e2f1      d1", getCellSiblingsAsString(board.cells["e1"]!!))
        assertEquals("f2g2g1  e1e2", getCellSiblingsAsString(board.cells["f1"]!!))
        assertEquals("g2h1      f1", getCellSiblingsAsString(board.cells["g1"]!!))
        assertEquals("h2i2i1  g1g2", getCellSiblingsAsString(board.cells["h1"]!!))
        assertEquals("i2j1      h1", getCellSiblingsAsString(board.cells["i1"]!!))
        assertEquals("j2      i1i2", getCellSiblingsAsString(board.cells["j1"]!!))
        assertEquals("a3b2b1a1    ", getCellSiblingsAsString(board.cells["a2"]!!))
        assertEquals("b3c3c2b1a2a3", getCellSiblingsAsString(board.cells["b2"]!!))
        assertEquals("c3d2d1c1b1b2", getCellSiblingsAsString(board.cells["c2"]!!))
        assertEquals("d3e3e2d1c2c3", getCellSiblingsAsString(board.cells["d2"]!!))
        assertEquals("e3f2f1e1d1d2", getCellSiblingsAsString(board.cells["e2"]!!))
        assertEquals("f3g3g2f1e2e3", getCellSiblingsAsString(board.cells["f2"]!!))
        assertEquals("g3h2h1g1f1f2", getCellSiblingsAsString(board.cells["g2"]!!))
        assertEquals("h3i3i2h1g2g3", getCellSiblingsAsString(board.cells["h2"]!!))
        assertEquals("i3j2j1i1h1h2", getCellSiblingsAsString(board.cells["i2"]!!))
        assertEquals("j3    j1i2i3", getCellSiblingsAsString(board.cells["j2"]!!))
        assertEquals("a4b3b2a2    ", getCellSiblingsAsString(board.cells["a3"]!!))
        assertEquals("b4c4c3b2a3a4", getCellSiblingsAsString(board.cells["b3"]!!))
        assertEquals("c4d3d2c2b2b3", getCellSiblingsAsString(board.cells["c3"]!!))
        assertEquals("d4e4e3d2c3c4", getCellSiblingsAsString(board.cells["d3"]!!))
        assertEquals("e4f3f2e2d2d3", getCellSiblingsAsString(board.cells["e3"]!!))
        assertEquals("f4g4g3f2e3e4", getCellSiblingsAsString(board.cells["f3"]!!))
        assertEquals("g4h3h2g2f2f3", getCellSiblingsAsString(board.cells["g3"]!!))
        assertEquals("h4i4i3h2g3g4", getCellSiblingsAsString(board.cells["h3"]!!))
        assertEquals("i4j3j2i2h2h3", getCellSiblingsAsString(board.cells["i3"]!!))
        assertEquals("j4    j2i3i4", getCellSiblingsAsString(board.cells["j3"]!!))
        assertEquals("a5b4b3a3    ", getCellSiblingsAsString(board.cells["a4"]!!))
        assertEquals("b5c5c4b3a4a5", getCellSiblingsAsString(board.cells["b4"]!!))
        assertEquals("c5d4d3c3b3b4", getCellSiblingsAsString(board.cells["c4"]!!))
        assertEquals("d5e5e4d3c4c5", getCellSiblingsAsString(board.cells["d4"]!!))
        assertEquals("e5f4f3e3d3d4", getCellSiblingsAsString(board.cells["e4"]!!))
        assertEquals("f5g5g4f3e4e5", getCellSiblingsAsString(board.cells["f4"]!!))
        assertEquals("g5h4h3g3f3f4", getCellSiblingsAsString(board.cells["g4"]!!))
        assertEquals("h5i5i4h3g4g5", getCellSiblingsAsString(board.cells["h4"]!!))
        assertEquals("i5j4j3i3h3h4", getCellSiblingsAsString(board.cells["i4"]!!))
        assertEquals("j5    j3i4i5", getCellSiblingsAsString(board.cells["j4"]!!))
        assertEquals("a6b5b4a4    ", getCellSiblingsAsString(board.cells["a5"]!!))
        assertEquals("b6c6c5b4a5a6", getCellSiblingsAsString(board.cells["b5"]!!))
        assertEquals("c6d5d4c4b4b5", getCellSiblingsAsString(board.cells["c5"]!!))
        assertEquals("d6e6e5d4c5c6", getCellSiblingsAsString(board.cells["d5"]!!))
        assertEquals("e6f5f4e4d4d5", getCellSiblingsAsString(board.cells["e5"]!!))
        assertEquals("f6g6g5f4e5e6", getCellSiblingsAsString(board.cells["f5"]!!))
        assertEquals("g6h5h4g4f4f5", getCellSiblingsAsString(board.cells["g5"]!!))
        assertEquals("h6i6i5h4g5g6", getCellSiblingsAsString(board.cells["h5"]!!))
        assertEquals("i6j5j4i4h4h5", getCellSiblingsAsString(board.cells["i5"]!!))
        assertEquals("j6    j4i5i6", getCellSiblingsAsString(board.cells["j5"]!!))
        assertEquals("a7b6b5a5    ", getCellSiblingsAsString(board.cells["a6"]!!))
        assertEquals("b7c7c6b5a6a7", getCellSiblingsAsString(board.cells["b6"]!!))
        assertEquals("c7d6d5c5b5b6", getCellSiblingsAsString(board.cells["c6"]!!))
        assertEquals("d7e7e6d5c6c7", getCellSiblingsAsString(board.cells["d6"]!!))
        assertEquals("e7f6f5e5d5d6", getCellSiblingsAsString(board.cells["e6"]!!))
        assertEquals("f7g7g6f5e6e7", getCellSiblingsAsString(board.cells["f6"]!!))
        assertEquals("g7h6h5g5f5f6", getCellSiblingsAsString(board.cells["g6"]!!))
        assertEquals("h7i7i6h5g6g7", getCellSiblingsAsString(board.cells["h6"]!!))
        assertEquals("i7j6j5i5h5h6", getCellSiblingsAsString(board.cells["i6"]!!))
        assertEquals("j7    j5i6i7", getCellSiblingsAsString(board.cells["j6"]!!))
        assertEquals("a8b7b6a6    ", getCellSiblingsAsString(board.cells["a7"]!!))
        assertEquals("b8c8c7b6a7a8", getCellSiblingsAsString(board.cells["b7"]!!))
        assertEquals("c8d7d6c6b6b7", getCellSiblingsAsString(board.cells["c7"]!!))
        assertEquals("d8e8e7d6c7c8", getCellSiblingsAsString(board.cells["d7"]!!))
        assertEquals("e8f7f6e6d6d7", getCellSiblingsAsString(board.cells["e7"]!!))
        assertEquals("f8g8g7f6e7e8", getCellSiblingsAsString(board.cells["f7"]!!))
        assertEquals("g8h7h6g6f6f7", getCellSiblingsAsString(board.cells["g7"]!!))
        assertEquals("h8i8i7h6g7g8", getCellSiblingsAsString(board.cells["h7"]!!))
        assertEquals("i8j7j6i6h6h7", getCellSiblingsAsString(board.cells["i7"]!!))
        assertEquals("j8    j6i7i8", getCellSiblingsAsString(board.cells["j7"]!!))
        assertEquals("  b8b7a7    ", getCellSiblingsAsString(board.cells["a8"]!!))
        assertEquals("    c8b7a8  ", getCellSiblingsAsString(board.cells["b8"]!!))
        assertEquals("  d8d7c7b7b8", getCellSiblingsAsString(board.cells["c8"]!!))
        assertEquals("    e8d7c8  ", getCellSiblingsAsString(board.cells["d8"]!!))
        assertEquals("  f8f7e7d7d8", getCellSiblingsAsString(board.cells["e8"]!!))
        assertEquals("    g8f7e8  ", getCellSiblingsAsString(board.cells["f8"]!!))
        assertEquals("  h8h7g7f7f8", getCellSiblingsAsString(board.cells["g8"]!!))
        assertEquals("    i8h7g8  ", getCellSiblingsAsString(board.cells["h8"]!!))
        assertEquals("  j8j7i7h7h8", getCellSiblingsAsString(board.cells["i8"]!!))
        assertEquals("      j7i8  ", getCellSiblingsAsString(board.cells["j8"]!!))
    }

    @Test
    fun testLinkCellsOfBoard10x10() {
        val board = Board(Board.Size.SIZE_10X10)
        assertEquals("a2b1        ", getCellSiblingsAsString(board.cells["a1"]!!))
        assertEquals("b2c2c1  a1a2", getCellSiblingsAsString(board.cells["b1"]!!))
        assertEquals("c2d1      b1", getCellSiblingsAsString(board.cells["c1"]!!))
        assertEquals("d2e2e1  c1c2", getCellSiblingsAsString(board.cells["d1"]!!))
        assertEquals("e2f1      d1", getCellSiblingsAsString(board.cells["e1"]!!))
        assertEquals("f2g2g1  e1e2", getCellSiblingsAsString(board.cells["f1"]!!))
        assertEquals("g2h1      f1", getCellSiblingsAsString(board.cells["g1"]!!))
        assertEquals("h2i2i1  g1g2", getCellSiblingsAsString(board.cells["h1"]!!))
        assertEquals("i2j1      h1", getCellSiblingsAsString(board.cells["i1"]!!))
        assertEquals("j2      i1i2", getCellSiblingsAsString(board.cells["j1"]!!))
        assertEquals("a3b2b1a1    ", getCellSiblingsAsString(board.cells["a2"]!!))
        assertEquals("b3c3c2b1a2a3", getCellSiblingsAsString(board.cells["b2"]!!))
        assertEquals("c3d2d1c1b1b2", getCellSiblingsAsString(board.cells["c2"]!!))
        assertEquals("d3e3e2d1c2c3", getCellSiblingsAsString(board.cells["d2"]!!))
        assertEquals("e3f2f1e1d1d2", getCellSiblingsAsString(board.cells["e2"]!!))
        assertEquals("f3g3g2f1e2e3", getCellSiblingsAsString(board.cells["f2"]!!))
        assertEquals("g3h2h1g1f1f2", getCellSiblingsAsString(board.cells["g2"]!!))
        assertEquals("h3i3i2h1g2g3", getCellSiblingsAsString(board.cells["h2"]!!))
        assertEquals("i3j2j1i1h1h2", getCellSiblingsAsString(board.cells["i2"]!!))
        assertEquals("j3    j1i2i3", getCellSiblingsAsString(board.cells["j2"]!!))
        assertEquals("a4b3b2a2    ", getCellSiblingsAsString(board.cells["a3"]!!))
        assertEquals("b4c4c3b2a3a4", getCellSiblingsAsString(board.cells["b3"]!!))
        assertEquals("c4d3d2c2b2b3", getCellSiblingsAsString(board.cells["c3"]!!))
        assertEquals("d4e4e3d2c3c4", getCellSiblingsAsString(board.cells["d3"]!!))
        assertEquals("e4f3f2e2d2d3", getCellSiblingsAsString(board.cells["e3"]!!))
        assertEquals("f4g4g3f2e3e4", getCellSiblingsAsString(board.cells["f3"]!!))
        assertEquals("g4h3h2g2f2f3", getCellSiblingsAsString(board.cells["g3"]!!))
        assertEquals("h4i4i3h2g3g4", getCellSiblingsAsString(board.cells["h3"]!!))
        assertEquals("i4j3j2i2h2h3", getCellSiblingsAsString(board.cells["i3"]!!))
        assertEquals("j4    j2i3i4", getCellSiblingsAsString(board.cells["j3"]!!))
        assertEquals("a5b4b3a3    ", getCellSiblingsAsString(board.cells["a4"]!!))
        assertEquals("b5c5c4b3a4a5", getCellSiblingsAsString(board.cells["b4"]!!))
        assertEquals("c5d4d3c3b3b4", getCellSiblingsAsString(board.cells["c4"]!!))
        assertEquals("d5e5e4d3c4c5", getCellSiblingsAsString(board.cells["d4"]!!))
        assertEquals("e5f4f3e3d3d4", getCellSiblingsAsString(board.cells["e4"]!!))
        assertEquals("f5g5g4f3e4e5", getCellSiblingsAsString(board.cells["f4"]!!))
        assertEquals("g5h4h3g3f3f4", getCellSiblingsAsString(board.cells["g4"]!!))
        assertEquals("h5i5i4h3g4g5", getCellSiblingsAsString(board.cells["h4"]!!))
        assertEquals("i5j4j3i3h3h4", getCellSiblingsAsString(board.cells["i4"]!!))
        assertEquals("j5    j3i4i5", getCellSiblingsAsString(board.cells["j4"]!!))
        assertEquals("a6b5b4a4    ", getCellSiblingsAsString(board.cells["a5"]!!))
        assertEquals("b6c6c5b4a5a6", getCellSiblingsAsString(board.cells["b5"]!!))
        assertEquals("c6d5d4c4b4b5", getCellSiblingsAsString(board.cells["c5"]!!))
        assertEquals("d6e6e5d4c5c6", getCellSiblingsAsString(board.cells["d5"]!!))
        assertEquals("e6f5f4e4d4d5", getCellSiblingsAsString(board.cells["e5"]!!))
        assertEquals("f6g6g5f4e5e6", getCellSiblingsAsString(board.cells["f5"]!!))
        assertEquals("g6h5h4g4f4f5", getCellSiblingsAsString(board.cells["g5"]!!))
        assertEquals("h6i6i5h4g5g6", getCellSiblingsAsString(board.cells["h5"]!!))
        assertEquals("i6j5j4i4h4h5", getCellSiblingsAsString(board.cells["i5"]!!))
        assertEquals("j6    j4i5i6", getCellSiblingsAsString(board.cells["j5"]!!))
        assertEquals("a7b6b5a5    ", getCellSiblingsAsString(board.cells["a6"]!!))
        assertEquals("b7c7c6b5a6a7", getCellSiblingsAsString(board.cells["b6"]!!))
        assertEquals("c7d6d5c5b5b6", getCellSiblingsAsString(board.cells["c6"]!!))
        assertEquals("d7e7e6d5c6c7", getCellSiblingsAsString(board.cells["d6"]!!))
        assertEquals("e7f6f5e5d5d6", getCellSiblingsAsString(board.cells["e6"]!!))
        assertEquals("f7g7g6f5e6e7", getCellSiblingsAsString(board.cells["f6"]!!))
        assertEquals("g7h6h5g5f5f6", getCellSiblingsAsString(board.cells["g6"]!!))
        assertEquals("h7i7i6h5g6g7", getCellSiblingsAsString(board.cells["h6"]!!))
        assertEquals("i7j6j5i5h5h6", getCellSiblingsAsString(board.cells["i6"]!!))
        assertEquals("j7    j5i6i7", getCellSiblingsAsString(board.cells["j6"]!!))
        assertEquals("a8b7b6a6    ", getCellSiblingsAsString(board.cells["a7"]!!))
        assertEquals("b8c8c7b6a7a8", getCellSiblingsAsString(board.cells["b7"]!!))
        assertEquals("c8d7d6c6b6b7", getCellSiblingsAsString(board.cells["c7"]!!))
        assertEquals("d8e8e7d6c7c8", getCellSiblingsAsString(board.cells["d7"]!!))
        assertEquals("e8f7f6e6d6d7", getCellSiblingsAsString(board.cells["e7"]!!))
        assertEquals("f8g8g7f6e7e8", getCellSiblingsAsString(board.cells["f7"]!!))
        assertEquals("g8h7h6g6f6f7", getCellSiblingsAsString(board.cells["g7"]!!))
        assertEquals("h8i8i7h6g7g8", getCellSiblingsAsString(board.cells["h7"]!!))
        assertEquals("i8j7j6i6h6h7", getCellSiblingsAsString(board.cells["i7"]!!))
        assertEquals("j8    j6i7i8", getCellSiblingsAsString(board.cells["j7"]!!))
        assertEquals("a9b8b7a7    ", getCellSiblingsAsString(board.cells["a8"]!!))
        assertEquals("b9c9c8b7a8a9", getCellSiblingsAsString(board.cells["b8"]!!))
        assertEquals("c9d8d7c7b7b8", getCellSiblingsAsString(board.cells["c8"]!!))
        assertEquals("d9e9e8d7c8c9", getCellSiblingsAsString(board.cells["d8"]!!))
        assertEquals("e9f8f7e7d7d8", getCellSiblingsAsString(board.cells["e8"]!!))
        assertEquals("f9g9g8f7e8e9", getCellSiblingsAsString(board.cells["f8"]!!))
        assertEquals("g9h8h7g7f7f8", getCellSiblingsAsString(board.cells["g8"]!!))
        assertEquals("h9i9i8h7g8g9", getCellSiblingsAsString(board.cells["h8"]!!))
        assertEquals("i9j8j7i7h7h8", getCellSiblingsAsString(board.cells["i8"]!!))
        assertEquals("j9    j7i8i9", getCellSiblingsAsString(board.cells["j8"]!!))
        assertEquals("a10b9b8a8    ", getCellSiblingsAsString(board.cells["a9"]!!))
        assertEquals("b10c10c9b8a9a10", getCellSiblingsAsString(board.cells["b9"]!!))
        assertEquals("c10d9d8c8b8b9", getCellSiblingsAsString(board.cells["c9"]!!))
        assertEquals("d10e10e9d8c9c10", getCellSiblingsAsString(board.cells["d9"]!!))
        assertEquals("e10f9f8e8d8d9", getCellSiblingsAsString(board.cells["e9"]!!))
        assertEquals("f10g10g9f8e9e10", getCellSiblingsAsString(board.cells["f9"]!!))
        assertEquals("g10h9h8g8f8f9", getCellSiblingsAsString(board.cells["g9"]!!))
        assertEquals("h10i10i9h8g9g10", getCellSiblingsAsString(board.cells["h9"]!!))
        assertEquals("i10j9j8i8h8h9", getCellSiblingsAsString(board.cells["i9"]!!))
        assertEquals("j10    j8i9i10", getCellSiblingsAsString(board.cells["j9"]!!))
        assertEquals( "  b10b9a9    ", getCellSiblingsAsString(board.cells["a10"]!!))
        assertEquals( "    c10b9a10  ", getCellSiblingsAsString(board.cells["b10"]!!))
        assertEquals( "  d10d9c9b9b10", getCellSiblingsAsString(board.cells["c10"]!!))
        assertEquals( "    e10d9c10  ", getCellSiblingsAsString(board.cells["d10"]!!))
        assertEquals( "  f10f9e9d9d10", getCellSiblingsAsString(board.cells["e10"]!!))
        assertEquals( "    g10f9e10  ", getCellSiblingsAsString(board.cells["f10"]!!))
        assertEquals( "  h10h9g9f9f10", getCellSiblingsAsString(board.cells["g10"]!!))
        assertEquals( "    i10h9g10  ", getCellSiblingsAsString(board.cells["h10"]!!))
        assertEquals( "  j10j9i9h9h10", getCellSiblingsAsString(board.cells["i10"]!!))
        assertEquals( "      j9i10  ", getCellSiblingsAsString(board.cells["j10"]!!))
    }
}
