package com.hexcheckers.engine

import com.hexcheckers.engine.helper.isBoardInInitialPosition
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlinx.coroutines.*

const val THREAD_COUNT = 4

class Ai(val board: Board) {

    private var interrupted = false

    var fullCalculation = false

    /**
     * Search best move with depth * 2 half moves
     */
    fun findBestMove(color: Piece.Color, depth: Int = 2): Move? {
        if (depth <= 0) {
            throw IllegalArgumentException("Incorrect depth value \"$depth\". Should greater than 0.")
        }

        interrupted = false

        val virtualBoard = board.copy()

        val bestMoves: MutableList<Move> = getPrecalculatedMoves(virtualBoard, color)
        if (bestMoves.isNotEmpty()) {
            return assignMoveToBoard(bestMoves[Random.nextInt(bestMoves.size)], board)
        }

        val moves: List<Move> = virtualBoard.getAvailableMoves(color)
        if (moves.isEmpty()) {
            return null
        } else if (moves.size == 1) {
            return assignMoveToBoard(moves[0], board)
        }

        val partSize = max(1, moves.size / THREAD_COUNT)
        val moveParts = moves.windowed(partSize, partSize, partialWindows = true).toMutableList()
        val virtualBoards: MutableList<Board> = mutableListOf()
        val scores: MutableList<Int> = mutableListOf()
        for (i in 0 until moveParts.size) {
            virtualBoards.add(virtualBoard.copy())
            assignMovesToBoard(moveParts[i], virtualBoards[i])
        }

        runBlocking {
            for (i in 0 until moveParts.size) {
                launch { scores.add(calculateBestMoveScore(virtualBoards[i], moveParts[i], color, depth)) }
            }
        }

        val bestMoveScore = scores.maxOrNull() ?: Int.MIN_VALUE
        for (part in moveParts) {
            for (move in part) {
                if (move.score == bestMoveScore) {
                    bestMoves += move
                }
            }
        }

        return assignMoveToBoard(bestMoves[Random.nextInt(bestMoves.size)], board)
    }

    fun interrupt() {
        interrupted = true
    }

    private fun getPrecalculatedMoves(board: Board, color: Piece.Color): MutableList<Move> {
        if (isBoardInInitialPosition(board)) {
            return board.getAvailableMoves(color).toMutableList()
        }
        return mutableListOf()
    }

    private fun calculateBestMoveScore(board: Board, startMoves: List<Move>, color: Piece.Color, depth: Int): Int {
        if (fullCalculation) {
            return calculateBestMoveScoreFull(
                board,
                buildBoardState(board),
                startMoves,
                color,
                depth * 2,
                Int.MIN_VALUE,
                Int.MAX_VALUE
            )
        } else {
            return calculateBestMoveScoreAB(
                board,
                buildBoardState(board),
                startMoves,
                color,
                depth * 2,
                Int.MIN_VALUE,
                Int.MAX_VALUE
            )
        }
    }

    private fun calculateBestMoveScoreFull(
        board: Board,
        startBoardState: BoardState,
        moves: List<Move>,
        color: Piece.Color,
        depth: Int,
        alpha: Int,
        beta: Int
    ): Int {
        if (depth == 0) {
            return calculateBoardScoreV2(startBoardState, buildBoardState(board), color)
        }

        val otherPieceColor = getInvertedColor(color)
        val isMaximizingPlayer = depth % 2 == 0
        var tmpAlpha = alpha
        var tmpBeta = beta
        var score: Int = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE

        for (move in moves) {
            if (interrupted) {
                return score
            }
            board.executeMove(move)
            val tmpScore = calculateBestMoveScoreFull(
                board,
                startBoardState,
                board.getAvailableMoves(otherPieceColor),
                otherPieceColor,
                depth - 1,
                tmpAlpha,
                tmpBeta
            )
            board.rollbackMove(move)
            move.root().score = tmpScore
            move.score = tmpScore
            if (isMaximizingPlayer) {
                score = max(tmpScore, score)
                tmpAlpha = max(tmpAlpha, score)
            } else {
                score = min(tmpScore, score)
                tmpBeta = min(tmpBeta, score)
            }
        }
        return score
    }

    private fun calculateBestMoveScoreAB(
        board: Board,
        startBoardState: BoardState,
        moves: List<Move>,
        color: Piece.Color,
        depth: Int,
        alpha: Int,
        beta: Int
    ): Int {
        if (depth == 0) {
            return calculateBoardScoreV2(startBoardState, buildBoardState(board), color)
        }

        val otherPieceColor = getInvertedColor(color)
        val isMaximizingPlayer = depth % 2 == 0
        var tmpAlpha = alpha
        var tmpBeta = beta
        var score: Int = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE

        for (move in moves) {
            if (interrupted) {
                return calculateBoardScoreV2(startBoardState, buildBoardState(board), color)
            }
            board.executeMove(move)
            val tmpScore = calculateBestMoveScoreAB(
                board,
                startBoardState,
                board.getAvailableMoves(otherPieceColor),
                otherPieceColor,
                depth - 1,
                tmpAlpha,
                tmpBeta
            )
            board.rollbackMove(move)
            move.root().score = tmpScore
            move.score = tmpScore
            if (isMaximizingPlayer) {
                score = max(tmpScore, score)
                tmpAlpha = max(tmpAlpha, score)
                if (beta <= tmpAlpha) break
            } else {
                score = min(tmpScore, score)
                tmpBeta = min(tmpBeta, score)
                if (tmpBeta <= alpha) break
            }
        }
        return score
    }

    private fun calculateBoardScoreV2(startBoardState: BoardState, boardState: BoardState, color: Piece.Color): Int {
        val menCost = 100
        var selfKingCost = menCost * 2
        var foreignKingCost = menCost * 2
        var efficiency: Int?
        val blackEfficiency: Int
        val whiteEfficiency: Int

        if (startBoardState.kingCountB == boardState.kingCountB && startBoardState.kingCountA == boardState.kingCountA
        ) {
            selfKingCost = menCost * 2
            foreignKingCost = menCost * 2
        }

        val playerCapturedMoves: Int
        val otherPlayerCapturedMoves: Int

        if (color == Piece.Color.A) {
            blackEfficiency = boardState.menCountB * menCost + boardState.kingCountB * foreignKingCost
            whiteEfficiency = boardState.menCountA * menCost + boardState.kingCountA * selfKingCost
            efficiency = if (boardState.menCountA + boardState.kingCountA == 0) {
                return Int.MIN_VALUE
            } else if (boardState.menCountB + boardState.kingCountB == 0) {
                return Int.MAX_VALUE
            } else {
                whiteEfficiency - blackEfficiency
            }
            if (startBoardState.menCountA > boardState.menCountA) {
                efficiency -= (startBoardState.menCountA - boardState.menCountA) * menCost
            }
            playerCapturedMoves = boardState.captureMovesCountA
            otherPlayerCapturedMoves = boardState.captureMovesCountB
        } else {
            blackEfficiency = boardState.menCountB * menCost + boardState.kingCountB * selfKingCost
            whiteEfficiency = boardState.menCountA * menCost + boardState.kingCountA * foreignKingCost
            efficiency = if (boardState.menCountB + boardState.kingCountB == 0) {
                return Int.MIN_VALUE
            } else if (boardState.menCountA + boardState.kingCountA == 0) {
                return Int.MAX_VALUE
            } else {
                blackEfficiency - whiteEfficiency
            }
            if (startBoardState.menCountB > boardState.menCountB) {
                efficiency -= (startBoardState.menCountB - boardState.menCountB) * menCost
            }
            playerCapturedMoves = boardState.captureMovesCountB
            otherPlayerCapturedMoves = boardState.captureMovesCountA
        }

        if (playerCapturedMoves > 0) efficiency += playerCapturedMoves
        if (otherPlayerCapturedMoves > 0) efficiency -= otherPlayerCapturedMoves

        return efficiency
    }
}
