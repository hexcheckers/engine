package com.hexcheckers.engine

import kotlin.test.*
import com.hexcheckers.engine.helper.makeBoardFromHcFen

internal class MoveTest {

    @Test
    fun testAvailableMovesDefaultBoard6x6() {
        val board = Board(Board.Size.SIZE_6X6, true)

        val expectedMovesColorA: List<String> = listOf(
            "a2-a3", "b2-a3", "b2-b3", "b2-c3", "c2-c3", "d2-c3", "d2-d3", "d2-e3", "e2-e3", "f2-e3", "f2-f3"
        )

        val expectedMovesColorB: List<String> = listOf(
            "a5-a4", "a5-b4", "b5-b4", "c5-b4", "c5-c4", "c5-d4", "d5-d4", "e5-d4", "e5-e4", "e5-f4", "f5-f4"
        )

        val allExpectedMoves = expectedMovesColorA + expectedMovesColorB

        val allMoves = movesListToStringList(board.getAvailableMoves())
        assertEquals(allExpectedMoves.size, allMoves.size)
        assertTrue(allExpectedMoves.containsAll(allMoves) && allMoves.containsAll(allExpectedMoves))

        val movesA = movesListToStringList(board.getAvailableMoves(Piece.Color.A))
        assertEquals(expectedMovesColorA.size, movesA.size)
        assertTrue(expectedMovesColorA.containsAll(movesA) && movesA.containsAll(expectedMovesColorA))

        val movesB = movesListToStringList(board.getAvailableMoves(Piece.Color.B))
        assertEquals(expectedMovesColorB.size, movesB.size)
        assertTrue(expectedMovesColorB.containsAll(movesB) && movesB.containsAll(expectedMovesColorB))
    }

    @Test
    fun testAvailableRegularKingMovesCustomBoard6x6() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_2())
        val moves = movesListToStringList(board.getAvailableMoves())

        val expectedMoves: List<String> = listOf(
            "b1-a2", "b1-b2", "b1-b3", "b1-b4", "b1-b5", "b1-b6", "b1-c2", "b1-d2", "b1-e3", "b1-f3", "b1-a1", "b1-c1"
        )

        assertEquals(expectedMoves.size, moves.size)
        assertTrue(expectedMoves.containsAll(moves) && moves.containsAll(expectedMoves))
    }

    @Test
    fun testAvailableCaptureMenMovesCustomBoard6x6() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_3())
        val moves = movesListToStringList(board.getAvailableMoves())

        val expectedMoves: List<String> = listOf("b1:d2", "c2:a1")

        assertEquals(expectedMoves.size, moves.size)
        assertTrue(expectedMoves.containsAll(moves) && moves.containsAll(expectedMoves))
    }

    @Test
    fun testAvailableCaptureKingMovesCustomBoard6x6_4() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_4())
        val moves = movesListToStringList(board.getAvailableMoves())

        val expectedMoves: List<String> = listOf(
            "b1:d2", "b1:e3", "b1:f3",
            "c2:a1"
        )

        assertEquals(expectedMoves.size, moves.size)
        assertTrue(expectedMoves.containsAll(moves) && moves.containsAll(expectedMoves))
    }

    @Test
    fun testAvailableCaptureKingMovesCustomBoard6x6_5() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_5())
        val moves = movesListToStringList(board.getAvailableMoves())

        val expectedMoves: List<String> = listOf(
            "b1:d2:b3", "b1:d2:f3",
            "c2:a1",
        )

        assertEquals(expectedMoves.size, moves.size)
        assertTrue(expectedMoves.containsAll(moves) && moves.containsAll(expectedMoves))
    }

    @Test
    fun testAvailableCaptureKingMovesCustomBoard6x6_6() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        val moves = movesListToStringList(board.getAvailableMoves())
        val expectedMoves: List<String> = listOf(
            "b1:d2:f3",
            "b1:d2:b3:b5",
            "c2:a1",
        )

        assertEquals(expectedMoves.size, moves.size)
        assertTrue(expectedMoves.containsAll(moves) && moves.containsAll(expectedMoves))
    }

    @Test
    fun testAvailableCaptureKingMovesCustomBoard6x6_7() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_7())
        val moves = movesListToStringList(board.getAvailableMoves())

        val expectedMoves: List<String> = listOf(
            "b1:b4:d5", "b1:b4:e6", "b1:b4:f6",
            "b1:b5:f3:b1", "b1:b5:f3:a1",
            "b1:d2:a4:d5", "b1:d2:a4:e6", "b1:d2:a4:f6",
            "b1:f3:b5:b2", "b1:f3:b5:b1",
            "c2:a1",
        )

        assertEquals(expectedMoves.size, moves.size)
        assertTrue(expectedMoves.containsAll(moves) && moves.containsAll(expectedMoves))
    }

    @Test
    fun testAvailableCaptureKingMovesCustomBoard6x6_8() {
        val board = makeBoardFromHcFen("6x6/---a/----b/---b/-----b/---a/----b")
        val moves = movesListToStringList(board.getAvailableMoves(Piece.Color.A))

        val expectedMoves: List<String> = listOf("d1:f2", "d5:f6:f2:c4", "d5:f6:f2:b4", "d5:f6:f2:a5", "d5:f6:f1:d2:d4", "d5:f6:f1:d2:d5", "d5:f6:f1:d2:d6", "d5:f6:f1:c3:e4")

        assertEquals(expectedMoves.size, moves.size)
        assertTrue(expectedMoves.containsAll(moves) && moves.containsAll(expectedMoves))
    }

    @Test
    fun testValidMove1() {
        val board = makeBoardFromHcFen("6x6/-aaa/--b////")
        assertTrue(board.isValidMove("b1:d2"))
        assertTrue(board.isValidMove("c1:c3"))
        assertTrue(board.isValidMove("d1:b2"))
        assertFalse(board.isValidMove("b1-a2"))
    }

    @Test
    fun testValidMove2() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        assertTrue(board.isValidMove("b1:d2:f3"))
        assertTrue(board.isValidMove("b1:d2:b3:b5"))
        assertTrue(board.isValidMove("c2:a1"))
        assertFalse(board.isValidMove("b1-b2"))
        assertFalse(board.isValidMove("c3-b2"))
    }

    @Test
    fun testValidMove3() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        val move = Move(board.cells["b1"]!!, board.cells["d2"]!!, board.cells["c2"]!!.piece)
        assertFalse(board.isValidMove(move))

        move.next = Move(board.cells["d2"]!!, board.cells["f3"]!!, board.cells["e3"]!!.piece)
        assertTrue(board.isValidMove(move))
    }

    @Test
    fun testIllegalMove1() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        assertFailsWith<NoSuchElementException> {
            board.buildMove("b1-b2")
        }
    }

    @Test
    fun testIllegalMove2() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        val move = Move(board.cells["b1"]!!, board.cells["d2"]!!, board.cells["c2"]!!.piece)
        assertFailsWith<NoSuchElementException> {
            board.executeMove(move)
        }
    }

    @Test
    fun testDetectCapturePiece1() {
        val board = makeBoardFromHcFen("6x6/aaaaaa/--b////")
        val piece = detectCapturePiece(board, "b1", "d2")
        assertEquals("c2", piece.cell.name)
    }

    @Test
    fun testDetectCapturePiece2() {
        val board = makeBoardFromHcFen("6x6//--b/--a///")
        val piece = detectCapturePiece(board, "c3", "c1")
        assertEquals("c2", piece.cell.name)
    }

    @Test
    fun testDetectCapturePiece3() {
        val board = makeBoardFromHcFen("6x6/A/--b////")
        val piece = detectCapturePiece(board, "a1", "f3")
        assertEquals("c2", piece.cell.name)
    }

    @Test
    fun testSimpleMove() {
        val board = Board(Board.Size.SIZE_6X6, true)
        board.executeOneMove(Move(board.cells["d2"]!!, board.cells["d3"]!!))
        assertEquals("6x6/aaaaaa/aaa-aa/---a//bbbbbb/bbbbbb", board.toHcFen())
    }

    @Test
    fun testCaptureMove1() {
        val board = makeBoardFromHcFen("6x6///---a/--b//")
        board.executeOneMove(Move(board.cells["d3"]!!, board.cells["b4"]!!, board.cells["c4"]!!.piece))
        assertEquals("6x6////-a//", board.toHcFen())
    }

    @Test
    fun testCaptureMove2() {
        val board = makeBoardFromHcFen("6x6/////-b---a/----b")
        val move1 = Move(board.cells["f5"]!!, board.cells["d6"]!!, board.cells["e6"]!!.piece)
        val move2 = Move(board.cells["d6"]!!, board.cells["a5"]!!, board.cells["b5"]!!.piece)
        move1.next = move2
        move2.previous = move1

        board.executeOneMove(move1)
        assertEquals("6x6/////-b/---Ab", board.toHcFen())

        board.executeOneMove(move2)
        assertEquals("6x6/////A/", board.toHcFen())
    }

    @Test
    fun testRollbackCaptureMove1() {
        val board = makeBoardFromHcFen("6x6///---a/--b//")
        val move = Move(board.cells["d3"]!!, board.cells["b4"]!!, board.cells["c4"]!!.piece)
        board.executeOneMove(move)
        assertEquals("6x6////-a//", board.toHcFen())

        board.rollbackOneMove(move)
        assertEquals("6x6///---a/--b//", board.toHcFen())
    }

    @Test
    fun testRollbackCaptureMove2() {
        val board = makeBoardFromHcFen("6x6///---a/--b//")
        val move = Move(board.cells["d3"]!!, board.cells["b4"]!!, board.cells["c4"]!!.piece)
        board.executeOneMove(move)
        assertEquals("6x6////-a//", board.toHcFen())

        board.rollbackOneMove(move)
        assertEquals("6x6///---a/--b//", board.toHcFen())
    }

    @Test
    fun testRollbackCaptureMove3() {
        val startBoardRepresentation = "6x6/////-b---a/----b"
        val board = makeBoardFromHcFen(startBoardRepresentation)
        val move1 = Move(board.cells["f5"]!!, board.cells["d6"]!!, board.cells["e6"]!!.piece)
        val move2 = Move(board.cells["d6"]!!, board.cells["a5"]!!, board.cells["b5"]!!.piece, move1)

        assertTrue(move1.dstPieceIsKing)

        board.executeOneMove(move1)
        assertEquals("6x6/////-b/---Ab", board.toHcFen())

        board.executeOneMove(move2)
        assertEquals("6x6/////A/", board.toHcFen())

        board.rollbackOneMove(move2)
        board.rollbackOneMove(move1)
        assertEquals(startBoardRepresentation, board.toHcFen())
    }

    @Test
    fun testRollbackCaptureMove4() {
        val startBoardRepresentation = "6x6/////-b---a/----b"
        val board = makeBoardFromHcFen(startBoardRepresentation)
        val move1 = Move(board.cells["f5"]!!, board.cells["d6"]!!, board.cells["e6"]!!.piece)
        val move2 = Move(board.cells["d6"]!!, board.cells["a5"]!!, board.cells["b5"]!!.piece, move1)

        board.executeMove(move1)
        assertEquals("6x6/////A/", board.toHcFen())

        board.rollbackMove(move2)
        assertEquals(startBoardRepresentation, board.toHcFen())
    }

    @Test
    fun testRollbackCaptureMove5() {
        val board = makeBoardFromHcFen("6x6///B/-b//-----A")
        val move = board.buildMove("f6:a4:a2")

        board.executeMove(move)
        assertEquals("6x6//A////", board.toHcFen())

        board.rollbackMove(move)
        assertEquals("6x6///B/-b//-----A", board.toHcFen())
    }

    @Test
    fun testCaptureMove3() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        val move1 = Move(board.cells["b1"]!!, board.cells["d2"]!!, board.cells["c2"]!!.piece)
        val move2 = Move(board.cells["d2"]!!, board.cells["f3"]!!, board.cells["e3"]!!.piece, move1)
        board.executeOneMove(move1)
        board.executeOneMove(move2)
        assertEquals("6x6///--b--a/-b//", board.toHcFen())
    }

    @Test
    fun testBuildMoveFromString() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        val move = board.buildMove("b1:d2:f3")
        assertEquals("b1:d2:f3", move.toString())

        board.executeMove(move)
        assertEquals("6x6///--b--a/-b//", board.toHcFen())
    }

    @Test
    fun testIsMoveStringRepresentationValid1() {
        val validMoves = listOf("a1-a2", "a1:a3", "j10-j9", "e2:e4:c5:a4:a2")
        for (m in validMoves) {
            assertTrue(isMoveStringRepresentationValid(m))
        }

        val invalidMoves = listOf("1234", "qwerty", "a1a2", "a1*a2", "a0-a1", "a11-a12", "s1-s2", "a1", "a1-", "a1-a",
            "a1-a11", "a1:", "a1:a", "a1:a11")
        for (m in invalidMoves) {
            assertFalse(isMoveStringRepresentationValid(m))
        }
    }

}
