package com.hexcheckers.engine

/**
 * Return string of cell's siblings cells starts from TOP_MIDDLE by hour hand.
 * For example, for cell "b2" result will be "b3c3c2b1a1a2".
 */
fun getCellSiblingsAsString(cell: Cell): String {
    return (cell.topMiddle?.name ?: "  ") + (cell.topRight?.name ?: "  ") + (cell.bottomRight?.name ?: "  ") + (cell.bottomMiddle?.name ?: "  ") + (cell.bottomLeft?.name ?: "  ") + (cell.topLeft?.name ?: "  ")
}

/**
 * Render in ASCII cell's siblings cells names
 * For example, for cell "b2" result will be:
 *     b3
 *  a2    c3
 *     b2
 *  a1    c2
 *     b1
 */
fun renderCellSiblings(cell: Cell) {
    println("   " + (cell.topMiddle?.name ?: "  "))
    println((cell.topLeft?.name ?: "  ") + "    " + (cell.topRight?.name ?: "  "))
    println("   " + cell.name)
    println((cell.bottomLeft?.name ?: "  ") + "    " + (cell.bottomRight?.name ?: "  "))
    println("   " + (cell.bottomMiddle?.name ?: "  "))
}

/**
 * Convert list of Move to list of its string representation
 * List<Move> -> List<String>
 */
fun movesListToStringList(moves: List<Move>): List<String> {
    val stringMoves: MutableList<String> = mutableListOf()
    moves.forEach { move -> stringMoves += move.toString() }
    return stringMoves
}

//       ___     ___     ___
//   ___/ b \___/ b \___/ b \
//6 / b \___/ b \___/ b \___/
//  \___/ b \___/ b \___/ b \
//5 / b \___/ b \___/ b \___/
//  \___/   \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/   \___/   \___/
//  \___/ a \___/ a \___/ a \
//2 / a \___/ a \___/ a \___/
//  \___/ a \___/ a \___/ a \
//1 / a \___/ a \___/ a \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getDefaultBoard6x6(): String {
    return "6x6/aaaaaa/aaaaaa///bbbbbb/bbbbbb"
}

fun getDefaultBoard6x8(): String {
    return "6x8/aaaaaa/aaaaaa/aaaaaa///bbbbbb/bbbbbb/bbbbbb"
}

fun getDefaultBoard8x6(): String {
    return "8x6/aaaaaaaa/aaaaaaaa///bbbbbbbb/bbbbbbbb"
}

fun getDefaultBoard8x8(): String {
    return "8x8/aaaaaaaa/aaaaaaaa/aaaaaaaa///bbbbbbbb/bbbbbbbb/bbbbbbbb"
}

fun getDefaultBoard8x10(): String {
    return "8x10/aaaaaaaa/aaaaaaaa/aaaaaaaa/aaaaaaaa///bbbbbbbb/bbbbbbbb/bbbbbbbb/bbbbbbbb"
}

fun getDefaultBoard10x8(): String {
    return "10x8/aaaaaaaaaa/aaaaaaaaaa/aaaaaaaaaa///bbbbbbbbbb/bbbbbbbbbb/bbbbbbbbbb"
}

fun getDefaultBoard10x10(): String {
    return "10x10/aaaaaaaaaa/aaaaaaaaaa/aaaaaaaaaa/aaaaaaaaaa///bbbbbbbbbb/bbbbbbbbbb/bbbbbbbbbb/bbbbbbbbbb"
}

//       ___     ___     ___
//   ___/   \___/   \___/   \
//6 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//5 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//2 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//1 /   \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getEmptyBoard6x6(): String {
    return "6x6//////"
}

//       ___     ___     ___
//   ___/   \___/   \___/ b \
//6 /   \___/   \___/ b \___/
//  \___/   \___/   \___/   \
//5 /   \___/ B \___/   \___/
//  \___/   \___/ a \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//2 / a \___/   \___/   \___/
//  \___/   \___/   \___/   \
//1 / a \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getCustomBoard6x6_1(): String {
    return "6x6/a/a//---a/--B/----bb"
}

//       ___     ___     ___
//   ___/   \___/   \___/   \
//6 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//5 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//2 /   \___/   \___/   \___/
//  \___/ A \___/   \___/   \
//1 /   \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getCustomBoard6x6_2(): String {
    return "6x6/-A/////"
}

//       ___     ___     ___
//   ___/   \___/   \___/   \
//6 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//5 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//2 /   \___/ b \___/   \___/
//  \___/ a \___/   \___/   \
//1 /   \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getCustomBoard6x6_3(): String {
    return "6x6/-a/--b////"
}

//       ___     ___     ___
//   ___/   \___/   \___/   \
//6 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//5 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//2 /   \___/ b \___/   \___/
//  \___/ A \___/   \___/   \
//1 /   \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getCustomBoard6x6_4(): String {
    return "6x6/-A/--b////"
}

//       ___     ___     ___
//   ___/   \___/   \___/   \
//6 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//5 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/ b \___/ b \___/
//  \___/   \___/   \___/   \
//2 /   \___/ b \___/   \___/
//  \___/ a \___/   \___/   \
//1 /   \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getCustomBoard6x6_5(): String {
    return "6x6/-a/--b/--b-b///"
}

//       ___     ___     ___
//   ___/   \___/   \___/   \
//6 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//5 /   \___/   \___/   \___/
//  \___/ b \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//3 /   \___/ b \___/ b \___/
//  \___/   \___/   \___/   \
//2 /   \___/ b \___/   \___/
//  \___/ a \___/   \___/   \
//1 /   \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getCustomBoard6x6_6(): String {
    return "6x6/-a/--b/--b-b/-b//"
}

//       ___     ___     ___
//   ___/   \___/   \___/   \
//6 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//5 /   \___/ b \___/   \___/
//  \___/   \___/   \___/   \
//4 /   \___/   \___/   \___/
//  \___/ b \___/   \___/   \
//3 /   \___/   \___/   \___/
//  \___/   \___/   \___/   \
//2 /   \___/ b \___/   \___/
//  \___/ A \___/   \___/   \
//1 /   \___/   \___/   \___/
//  \___/   \___/   \___/
//    a   b   c   d   e   f
fun getCustomBoard6x6_7(): String {
    return "6x6/-A/--b/-b//--b/"
}
