package com.example.calculadorafatoriais.combinatorics

private fun multiplicationOverflows(a: ULong, b: ULong, result: ULong): Boolean {
    return (a != result / b)
}

/* rangeEnd permite diminuir tamanho da operação quando se usa a função em outras operações de
combinatória (semelhante a cortar números em contas no papel) evitando overflow desnecessário */
fun permutacao(n: ULong, rangeEnd: ULong = 1UL): ULong? {
    if (n == 0UL || n == 1UL) { return 1UL }
    var result: ULong = n
    for (i in (n - 1UL).downTo(rangeEnd + 1UL)) {
        val resultBeforeMultiplication = result
        result *= i
        if (multiplicationOverflows(resultBeforeMultiplication, i, result)) {
            return null
        }
    }
    return result
}

fun permutacaoRepeticao(n: ULong,k: ULong): ULong? {
    return permutacao(n, k)
}

fun arranjo(n: ULong, k: ULong): ULong? {
    return permutacao(n, n - k)
}

fun arranjoRepeticao(n:ULong,k: ULong): ULong? {
    var result = 1UL
    for (i in 1UL..k) {
        val currNum = result
        result *= n
        if (multiplicationOverflows(n, currNum, result)) {
            return null
        }
    }
    return result
}

fun combinacao(n:ULong,k:ULong): ULong? {
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