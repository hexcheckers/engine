package com.hexcheckers.engine

import kotlin.test.*
import com.hexcheckers.engine.helper.makeBoardFromHcFen

internal class AiTest {

    @Test
    fun testAi1() {
        val board = makeBoardFromHcFen(getCustomBoard6x6_6())
        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.A, 4)
        assertNotNull(move)
        assertEquals("b1:d2:b3:b5", move.toString())
    }

    @Test
    fun testAi2() {
        val board = makeBoardFromHcFen("6x6//-a---b/-b/---a-b/---b/----b")
        val move = board.buildMove("d4:d6:f5:f3:f1:a4")
        board.executeMove(move)
        assertEquals("6x6//-a//A//", board.toHcFen())
    }

    @Test
    fun testAi3() {
        val board = makeBoardFromHcFen("6x6//-a---b/-b/---a-b/---b/----b")
        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.A, 2)

        assertNotNull(move)
        assertEquals("d4:d6:f5:f3:f1:a4", move.toString())

        board.executeMove(move)
        assertEquals("6x6//-a//A//", board.toHcFen())
    }

    @Test
    fun testAi4() {
        val board = makeBoardFromHcFen("6x6/---a/----b/---b/-----b/---a/----b")
        val expectedMoves: List<String> = listOf("d5:f6:f1:d2:d4", "d5:f6:f1:d2:d5", "d5:f6:f1:d2:d6")

        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.A, 2)

        assertNotNull(move)
        assertTrue(expectedMoves.contains(move.toString()))
    }

    @Test
    fun testAi5() {
        val board = makeBoardFromHcFen("6x6/-b/-a-b/b/---a-b/---b/b---b")
        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.A, 4)
        assertTrue(listOf("d4:d6:f5:f3:c2:a1:a4", "d4:d6:f5:f1:a4:a2:c1", "d4:d6:f5:f1:a4:a1:c2").contains(move.toString()))
    }

    @Test
    fun testAi6() {
        val board = makeBoardFromHcFen("6x6//--a-a/----b/----b//--a")
        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.A, 4) // e2-f2
        assertNotNull(move)
        board.executeMove(move)
        assertEquals("6x6//--a--a/----b/----b//--a", board.toHcFen())
    }

    @Test
    fun testAi6_1() {
        val board = makeBoardFromHcFen("6x6//--a-a/----b/----b//--a")
        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.A, 2)
        assertTrue(listOf("e2-d2", "e2-f2").contains(move.toString()))
    }

    @Test
    fun testAi7() {
        val board = makeBoardFromHcFen("6x6//b--a/---bb/--b/-b--b/----b")

        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.A, 4)
        assertNotNull(move)

        board.executeMove(move)
        assertEquals("6x6///-----A/--b//", board.toHcFen())
    }

    @Test
    fun testAi8() {
        val board = makeBoardFromHcFen("6x6//---a-b/-a-aa/a-a--b/-b---b/")
        val ai = Ai(board)
        val move = ai.findBestMove(Piece.Color.B, 4)
        assertTrue(listOf("f2-f1", "f4-e4", "f4-f3", "b5-a5", "b5-b4", "b5-c5", "f5-e5").contains(move.toString()))
    }

    //@Test
    fun testTmp() {
        var board = makeBoardFromHcFen("6x6//---a-b/-a-aa/a-a--b/-b---b/")
        println(board.toAscii())
        var ai = Ai(board)
        ai.fullCalculation = false
        var move = ai.findBestMove(Piece.Color.B, 2)
        println(move)
        board.executeMove(move!!)
        println(board.toAscii())
        println("--------------------------------------------")


//        var board = makeBoardFromString("6x6/-a-aa/-aaaaa/ab--aa/--b/----bb/-bbbbb")
//        println(board.toAsciiString())
//        var ai = Ai(board)
//        var move = ai.findBestMove(Piece.Color.B, 1)
//        println(move)
//        board.executeMove(move!!)
//        println(board.toAsciiString())
//        println("--------------------------------------------")

//        board = makeBoardFromString("6x6/a/---a-a/-a--aa/--bb-a/a--bb/a--b-b")
//        println(board.toAsciiString())
//        ai = Ai(board)
//        ai.fullCalculation = true
//        move = ai.findBestMove(Piece.Color.A, 1)
//        println(move)
//        board.executeMove(move!!)
//        println(board.toAsciiString())
//        println("--------------------------------------------")

//
//        move = ai.findBestMove(Piece.Color.B, 3)
//        println(move)
//        println("--------------------------------------------")

//        move = ai.findBestMove(Piece.Color.B, 4)
//        println(move)

        //for (i in 1..10) {
//            val ai = Ai(board)
//            val move = ai.findBestMove(Piece.Color.A, 4)
//            println(move)
        //}
//        assertNotNull(move)
//
//        board.executeMove(move)
//        assertEquals("6x6///-----A/--b//", board.toFenString())
    }
}
