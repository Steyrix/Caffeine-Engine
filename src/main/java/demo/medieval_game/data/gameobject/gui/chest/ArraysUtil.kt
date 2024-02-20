package demo.medieval_game.data.gameobject.gui.chest

typealias IndexMap = List<Pair<Int, List<Int>>>

object ArraysUtil {

    private fun IntArray.findEmptyHorizontalSpace(size: Int): List<Int> {
        this.forEachIndexed { index, it ->
            if (it == 0) {
                if (index + 2 < this.size) {
                    if (this[index + 1] == 0 && this[index + 2] == 0)
                        return mutableListOf(index, index + 1, index + 2)
                }
            }
        }

        return emptyList()
    }

    private fun Array<IntArray>.findEmptyHorizontalSpaces(size: Int): IndexMap {
        val out = mutableListOf<Pair<Int, List<Int>>>()

        this.forEachIndexed { rowIndex, it ->
            val columnIndices = it.findEmptyHorizontalSpace(size)
            if (columnIndices.isNotEmpty()) out.add(Pair(rowIndex, columnIndices))
        }

        return out
    }


    private fun Array<IntArray>.findEmptyVerticalSpaces(
        columnCount: Int,
        size: Int
    ): IndexMap {
        var currSize = 0
        val rowIndices = mutableListOf<Int>()
        val out = mutableListOf<Pair<Int, List<Int>>>()

        for (columnIndex in 0 until columnCount) {
            this.forEachIndexed { index, it ->
                if (it[columnIndex] == 0) {
                    rowIndices.add(index)
                    currSize++
                }
                if (currSize == size) {
                    out.add(Pair(columnIndex, rowIndices))
                }
                if (it[columnIndex] != 0 && currSize < size) {
                    currSize = 0
                    rowIndices.clear()
                }
            }
        }

        return out
    }

    private fun findIntersectionIndices(rowToColumns: IndexMap, columnsToRows: IndexMap) {
        val out = mutableListOf<Pair<Int, MutableList<Int>>>()

    }
}