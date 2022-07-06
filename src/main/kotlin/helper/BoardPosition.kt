package com.hexcheckers.engine.helper

import com.hexcheckers.engine.Board

fun isBoardInInitialPosition(board: Board): Boolean {
    val initialBoardPositions: List<String> = listOf(
        "6x6/aaaaaa/aaaaaa///bbbbbb/bbbbbb",
        "6x8/aaaaaa/aaaaaa/aaaaaa///bbbbbb/bbbbbb/bbbbbb",
        "8x6/aaaaaaaa/aaaaaaaa///bbbbbbbb/bbbbbbbb",
        "8x8/aaaaaaaa/aaaaaaaa/aaaaaaaa///bbbbbbbb/bbbbbbbb/bbbbbbbb",
        "8x10/aaaaaaaa/aaaaaaaa/aaaaaaaa/aaaaaaaa///bbbbbbbb/bbbbbbbb/bbbbbbbb/bbbbbbbb",
        "10x8/aaaaaaaaaa/aaaaaaaaaa/aaaaaaaaaa///bbbbbbbbbb/bbbbbbbbbb/bbbbbbbbbb",
        "10x10/aaaaaaaaaa/aaaaaaaaaa/aaaaaaaaaa/aaaaaaaaaa///bbbbbbbbbb/bbbbbbbbbb/bbbbbbbbbb/bbbbbbbbbb",
    )
    return initialBoardPositions.contains(board.toHcFen())
}
