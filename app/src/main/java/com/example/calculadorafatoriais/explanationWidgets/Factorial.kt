package com.example.calculadorafatoriais.explanationWidgets

import android.text.SpannableString
import android.text.SpannedString
import android.text.style.StrikethroughSpan

interface Factorial {
    /**
     * Retorna uma string que representa uma sequência de multiplicações que vai de rangeStart até
     * rangeEnd de forma decrescente.
     *  */
    fun factorialMultiplicationsString(rangeStart: Int, rangeEnd: Int = 1, hasStrikethrough: Boolean = false): SpannableString {
        var seriesText = ""
        if (hasStrikethrough) {
            if (1 + rangeStart - rangeEnd > 3) {
                seriesText += "$rangeStart x ${rangeStart - 1} x ... x ${rangeEnd + 1} x ${rangeEnd}!"
            }
            else {
                for (i in 0..rangeStart - rangeEnd) {
                    seriesText += if (i != (rangeStart - rangeEnd)) "${rangeStart - i} x " else "${rangeStart - i}!"
                }
            }
        }
        else {
            if (rangeStart > 5) {
                seriesText += "$rangeStart x ${rangeStart - 1} x ... x " + factorialMultiplicationsString(3, rangeEnd)
            }
            else {
                for (i in rangeStart downTo rangeEnd) {  // Printa sequência de valores multiplicadas no fatorial
                    if (i != rangeEnd) {
                        seriesText += "$i x "
                    } else {
                        seriesText += if (rangeEnd == 1) "$i" else "$i!"
                    }
                }
            }
        }
        val seriesTextString = SpannableString(seriesText)
        if (rangeEnd != 1) {  // rangeEnd só não é um quando se usa a função para demonstrar corte do fatorial por causa de uma divisão
            val spanStart = seriesText.indexOf(seriesText.split(" x ").last())  // Dá o índice do último número da série de multiplicações na string
            seriesTextString.setSpan(StrikethroughSpan(), spanStart, spanStart + seriesText.split(" x ").last().length, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return seriesTextString
    }
}