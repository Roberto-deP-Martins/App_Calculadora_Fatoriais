package com.example.calculadorafatoriais.combinatorics

private fun multiplicationOverflows(a: ULong, b: ULong, result: ULong): Boolean {
    return (a != result / b)
}

/* rangeEnd permite diminuir tamanho da operação quando se usa a função em outras operações de
combinatória (semelhante a cortar números em contas no papel) evitando overflow desnecessário */
fun permutacao(n: Int, rangeEnd: Int = 1): ULong? {
    if (n == 0 || n == 1) { return 1UL }
    var result: ULong = n.toULong()
    for (i in ((n - 1).toULong()).downTo((rangeEnd + 1).toULong())) {
        val resultBeforeMultiplication = result
        result *= i
        if (multiplicationOverflows(resultBeforeMultiplication, i, result)) {
            return null
        }
    }
    return result
}

fun permutacaoRepeticao(n: Int,k: Int): ULong? {
    return permutacao(n, k)
}

fun arranjo(n: Int, k: Int): ULong? {
    return permutacao(n, n - k)
}

fun arranjoRepeticao(n:Int,k: Int): ULong? {
    var result = 1UL
    for (i in 1UL..k.toULong()) {
        val currNum = result
        result *= n.toULong()
        if (multiplicationOverflows(n.toULong(), currNum, result)) {
            return null
        }
    }
    return result
}

fun combinacao(n:Int,k:Int): ULong? {
    var numerator: ULong? = null
    var divisor: ULong? = null
    if (k > n - k) {
        numerator = permutacao(n, k) ?: return  null
        divisor = permutacao(n - k) ?: return null
    }
    else {
        numerator = permutacao(n, n - k) ?: return  null
        divisor = permutacao(k) ?: return null
    }
    return numerator / divisor
}