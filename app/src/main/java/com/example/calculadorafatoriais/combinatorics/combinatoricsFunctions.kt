package com.example.calculadorafatoriais.combinatorics

private fun multiplicationOverflows(a: ULong, b: ULong, result: ULong): Boolean {
    return (a != result / b)
}

fun permutacao(n: ULong): ULong? {
    var result: ULong = n
    for (i in (n - 1U).downTo(2U)) {
        val resultBeforeMultiplication = result
        result *= i
        if (multiplicationOverflows(resultBeforeMultiplication, i, result)) {
            return null
        }
    }
    return result
}

fun permutacaoRepeticao(n: ULong,k: ULong): ULong? {
    val numerator = permutacao(n) ?: return null
    val divisor = permutacao(k) ?: return null
    return numerator / divisor
}

fun arranjo(n: ULong, k: ULong): ULong? {
    val numerator = permutacao(n) ?: return null
    val divisor = permutacao(n - k) ?: return null
    return numerator / divisor
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
    val numerator = permutacao(n) ?: return null
    val divisorFirstFactor = permutacao(k) ?: return null
    val divisorSecondFactor = permutacao(n - k) ?: return null
    val divisor = divisorFirstFactor * divisorSecondFactor
    if (multiplicationOverflows(divisorFirstFactor, divisorSecondFactor, divisor)) {
        return null
    }
    return numerator / divisor
}