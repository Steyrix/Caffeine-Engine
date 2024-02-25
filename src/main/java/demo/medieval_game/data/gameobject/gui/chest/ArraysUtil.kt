package demo.medieval_game.data.gameobject.gui.chest

typealias IndexMap = List<Pair<Int, List<Int>>>

// TODO: optimize
object ArraysUtil {

    private fun IntArray.findEmptyHorizontalSpace(size: Int): List<Int> {
        val tempList = mutableListOf<Int>()

        this.forEachIndexed { index, it ->
            if (it == 0) {
                tempList.add(index)
                if (index + size > this.size) return@forEachIndexed
                for (i in index + 1 until  this.size) {
                    if (this[i] == 0) {
                        tempList.add(i)
                    }
                }
                if (tempList.size >= size) {
                    return tempList
                }
                tempList.clear()
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
        val tempRowIndices = mutableListOf<Int>()
        val rowIndices = mutableListOf<Int>()
        val out = mutableListOf<Pair<Int, List<Int>>>()

        for (columnIndex in 0 until columnCount) {
            this.forEachIndexed { index, it ->
                if (it[columnIndex] == 0) {
                    tempRowIndices.add(index)
                    currSize++
                }
                if (currSize == size) {
                    rowIndices.clear()
                    rowIndices.addAll(tempRowIndices)
                    out.add(Pair(columnIndex, rowIndices))
                    currSize = 0
                    tempRowIndices.clear()

                }
                if (it[columnIndex] != 0 && currSize < size) {
                    currSize = 0
                    tempRowIndices.clear()
                }
            }
        }

        return out
    }

    private fun findIntersectionIndices(
        rectHeight: Int,
        rowToColumns: IndexMap,
        columnsToRows: IndexMap
    ): IndexMap {
        val out = mutableListOf<Pair<Int, List<Int>>>()
        val tempList = mutableListOf<Int>()

        var counter = 0

        rowToColumns.forEach { rowColumns ->
            columnsToRows.forEach { columnRows ->
                if (rowColumns.second.contains(columnRows.first)) {
                        tempList.add(columnRows.first)
                    counter++
                    if (counter == rectHeight) {
                        val pair = Pair(rowColumns.first, tempList.toList())
                        out.add(pair)
                        counter = 0
                        tempList.clear()
                    }
                } else {
                    counter = 0
                    tempList.clear()
                }
            }
        }

        return out
    }

    fun findRectangle(
        columnCount: Int,
        height: Int,
        width: Int,
        source: Array<IntArray>
    ): IndexMap {
        val rowToColumns = source.findEmptyHorizontalSpaces(width)
        val columnsToRows = source.findEmptyVerticalSpaces(columnCount, height)

        println(rowToColumns)
        println(columnsToRows)

        return findIntersectionIndices(width, rowToColumns, columnsToRows)
    }
}