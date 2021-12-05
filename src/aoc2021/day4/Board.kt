package aoc2021.day4

class Board(private val fields: List<List<Int>>) {
    var score: Long = -1
    private val marked: List<MutableList<Boolean>> = List(fields.size) { MutableList(fields.first().size) { false } }
    private val marksInRows = MutableList(5) { 0 }
    private val marksInCols = MutableList(5) { 0 }

    fun draw(number: Int): Board {
        val row = fields.indexOfFirst { it.contains(number) }
        if (row > -1) {
            val col = fields[row].indexOf(number)
            marked[row][col] = true
            marksInRows[row]++
            marksInCols[col]++

            if (marksInRows[row] >= 5 || marksInCols[col] >= 5) {
                score = calculateScore(number.toLong())
            }
        }

        return this
    }

    fun calculateScore(number: Long): Long {
        val sum = fields.mapIndexed { i, row -> row.filterIndexed { j, _ -> !marked[i][j] } }.sumOf { it.sum() }
        return sum * number
    }
}