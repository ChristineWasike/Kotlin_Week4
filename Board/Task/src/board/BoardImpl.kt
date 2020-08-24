package board

import java.lang.IllegalArgumentException

data class NewCell<T>(override val i: Int, override val j: Int, var value: T? = null) : Cell {
    override fun toString(): String {
        return "($i, $j)"
    }
}

open class NewSquareBoard<T>(final override val width: Int) : SquareBoard {
    // Declaration of the list of cells within the square board.
    protected val cells: List<NewCell<T>>

    init {
        // Generating all the cells based on the square board width provided
        if (width <= 0) throw IllegalArgumentException("Width must be a positive number, was: $width")
        cells = IntRange(1, width).flatMap { i -> IntRange(1, width).map { j -> NewCell<T>(i, j) } }
    }
    // Necessary Private Functions

    // Ensuring that there is one based indexing used throughout the board with its cells
    protected fun toOneIndex(row: Int, col: Int): Int {
        require(row >= 1) {
            "row must be 1-based index, was: $row"
        }

        require(col >= 1) {
            "col must be 1-based index, was: $col"
        }
        return (row - 1) * width + (col - 1)
    }

    //
    private fun restrictToBoardBoundaries(range: IntProgression): Pair<Int, Int> {
        // Regardless of the final range, ensuring that the last value range is the size of the width
        return if (range.step > 0)
            Pair(maxOf(range.first, 1), minOf(range.last, width))
        else
            Pair(minOf(range.first, width), maxOf(range.last, 1))
    }

    //
    private fun getRange(fixedCoordinate: Int, range: IntProgression, indexer: (Int, Int) -> Int): List<NewCell<T>> {
        val (start, end) = restrictToBoardBoundaries(range)
        return IntProgression.fromClosedRange(start, end, range.step).map { fluentCoordinate ->
            val index = indexer(fixedCoordinate, fluentCoordinate)
            cells[index]
        }
    }


    // Ensuring that null is returned if the specified cell is not identified.
    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (i <= width && j <= width) {
            cells[toOneIndex(i, j)]
        } else {
            null
        }
    }

    // Throwing an IllegalArgumentException in the event that either
    // of the coordinates of a cell is out of the width bounds.
    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j)
                ?: throw IllegalArgumentException("Cell ($i, $j) is out of $width x $width board boundaries")
    }


    // Returning all cells within the specified square board.
    override fun getAllCells(): Collection<Cell> {
        return cells
    }

    // Getting the neighbouring cells based on the specified direction
    override fun Cell.getNeighbour(direction: Direction): Cell? {
        // simplified switch cases for the different directions
        return when (direction) {
            // Check that the cell is not to the extreme top.
            // And if so, return null to its neighbouring top cell.
            Direction.UP -> if (i > 1) cells[toOneIndex(i - 1, j)] else null

            // Check that the cell is not to the extreme bottom.
            // And if so, return null to its neighbouring bottom cell.
            Direction.DOWN -> if (i < width) cells[toOneIndex(i + 1, j)] else null

            // Check that the cell is not to the extreme left.
            // And if so, return null to its neighbouring left cell.
            Direction.LEFT -> if (j > 1) cells[toOneIndex(i, j - 1)] else null

            // Check that the cell is not to the extreme right.
            // And if so, return null to its neighbouring right cell.
            Direction.RIGHT -> if (this.j < width) cells[toOneIndex(this.i, this.j + 1)] else null
        }
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return getRange(i, jRange) { row, col -> toOneIndex(row, col) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return getRange(j, iRange) { col, row -> toOneIndex(row, col) }
    }
}

class NewGameBoard<T>(width: Int) : NewSquareBoard<T>(width), GameBoard<T> {

    private fun cellFinder(predicate: (T?) -> Boolean): (NewCell<T>) -> Boolean {
        return { cell -> predicate(cell.value) }
    }

    override fun get(cell: Cell): T? {
        return cells[toOneIndex(cell.i, cell.j)].value
    }

    override fun set(cell: Cell, value: T?) {
        cells[toOneIndex(cell.i, cell.j)].value = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cells.filter(cellFinder(predicate))
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cells.find(cellFinder(predicate))
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cells.any(cellFinder(predicate))
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cells.all(cellFinder(predicate))
    }

}

fun createSquareBoard(width: Int): SquareBoard {
    return NewSquareBoard<Nothing>(width)
}

fun <T> createGameBoard(width: Int): GameBoard<T> = NewGameBoard(width)

fun main() {
    // Sample Creation of a board of width 2
    val board = mutableListOf<Pair<Int, Int>>()
    for (i in 1..2) {
        for (j in 1..2) {
            board.add(Pair(i, j))
        }

    }
//    println(board)

    val biggerBoard = createSquareBoard(4)
//    println(biggerBoard.getRow(3,1..5))

    val gameBoard = createGameBoard<Nothing>(4)
    println(gameBoard.getCell(1, 1))


}