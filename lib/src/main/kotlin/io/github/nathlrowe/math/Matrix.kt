package io.github.nathlrowe.math

/**
 * Simple, performance-based matrix implementation.
 */
class Matrix(
    val rows: Int,
    val cols: Int,
    val values: Array<Double>
) {
    init {
        require(rows * cols == values.size)
    }

    val size: Pair<Int, Int> get() = Pair(rows, cols)

    operator fun get(x: Int): Double = values[x]

    fun get(i: Int, j: Int): Double = values[index(i, j)]

    operator fun set(x: Int, value: Double) {
        values[x] = value
    }

    fun set(i: Int, j: Int, value: Double) = set(index(i, j), value)

    operator fun plus(other: Matrix): Matrix {
        if (rows != other.rows || cols != other.cols) throw MatrixArithmeticException("Matrices are incompatible for addition ($size with ${other.size})")
        return Matrix(rows, cols, Array(rows * cols) { values[it] + other.values[it] })
    }

    operator fun minus(other: Matrix): Matrix {
        if (rows != other.rows || cols != other.cols) throw MatrixArithmeticException("Matrices are incompatible for subtraction ($size with ${other.size})")
        return Matrix(rows, cols, Array(rows * cols) { values[it] - other.values[it] })
    }

    operator fun times(other: Matrix): Matrix {
        if (cols != other.rows) throw MatrixArithmeticException("Matrices are incompatible for multiplication ($size with ${other.size})")
        return Matrix(rows, other.cols) { i, j ->
            var value = 0.0
            for (k in 0 until cols) {
                value += get(i, k) * other.get(k, j)
            }
            value
        }
    }

    operator fun times(number: Number): Matrix = Matrix(rows, cols, Array(rows * cols) { values[it] * number.toDouble() })

    operator fun div(number: Number): Matrix = Matrix(rows, cols, Array(rows * cols) { values[it] / number.toDouble() })

    operator fun unaryMinus(): Matrix = Matrix(rows, cols, Array(rows * cols) { -values[it] })

    private fun index(i: Int, j: Int): Int = i * cols + j
}

// TODO refactor Matrix constructors

fun Matrix(
    n: Int,
    values: Array<Double>
): Matrix {
    return Matrix(n, n, values)
}

fun Matrix(
    rows: Int,
    cols: Int,
    value: Number
): Matrix {
    return Matrix(rows, cols, Array(rows * cols) { value.toDouble() })
}

fun Matrix(
    rows: Int,
    cols: Int,
    value: (i: Int, j: Int) -> Number
): Matrix {
    return Matrix(rows, cols, Array(rows * cols) { value(it / cols, it % cols).toDouble() })
}

fun Matrix(
    n: Int,
    value: (i: Int, j: Int) -> Number
): Matrix = Matrix(n, n, value)

fun Collection<Double>.toMatrix(
    rows: Int? = null,
    cols: Int? = null
): Matrix {
    return when {
        rows != null && cols != null -> Matrix(rows, cols, toTypedArray())
        rows != null -> Matrix(rows, size / rows, toTypedArray())
        cols != null -> Matrix(size / cols, cols, toTypedArray())
        else -> throw NullPointerException("rows and cols cannot both be null.")
    }
}

fun Array<Array<Number>>.toMatrix(): Matrix = Matrix(size, first().size) { i, j -> this[i][j] }

fun identityMatrix(n: Int): Matrix {
    return Matrix(n, n) { i, j -> if (i == j) 1 else 0 }
}

fun Matrix.copy(): Matrix {
    return Matrix(rows, cols, values.copyOf())
}

fun Matrix.transpose(): Matrix {
    return Matrix(cols, rows) { i, j -> get(j, i) }
}

fun Matrix.rowSwitch(i: Int, j: Int): Matrix {
    require(i in 0 until rows)
    require(j in 0 until rows)
    require(i != j)
    return Matrix(rows, cols) { r, c ->
        when (r) {
            i -> get(j, c)
            j -> get(i, c)
            else -> get(r, c)
        }
    }
}

fun Matrix.rowTimes(i: Int, k: Number): Matrix {
    require(i in 0 until rows)
    require(k.toDouble() != 0.0)
    return Matrix(rows, cols) { r, c ->
        when (r) {
            i -> get(r, c) * k.toDouble()
            else -> get(r, c)
        }
    }
}

fun Matrix.rowPlus(i: Int, j: Int, k: Number = 1): Matrix {
    require(i in 0 until rows)
    require(j in 0 until rows)
    require(i != j)
    return Matrix(rows, cols) { r, c ->
        when (r) {
            i -> get(i, c) + get(j, c) * k.toDouble()
            else -> get(r, c)
        }
    }
}

fun Matrix.subMatrix(i: Int, j: Int): Matrix {
    return Matrix(rows - 1, cols - 1) { r, c ->
        when {
            r < i && c < j -> get(r, c)
            r < i -> get(r, c + 1)
            c < j -> get(r + 1, c)
            else -> get(r + 1, c + 1)
        }
    }
}

fun Matrix.map(transform: (Double) -> Double): Matrix {
    return Matrix(rows, cols) { i, j -> transform(get(i, j)) }
}

fun Matrix.mapIndexed(transform: (Int, Int, Double) -> Double): Matrix {
    return Matrix(rows, cols) { i, j -> transform(i, j, get(i, j)) }
}


class MatrixArithmeticException(message: String? = null) : ArithmeticException(message)
