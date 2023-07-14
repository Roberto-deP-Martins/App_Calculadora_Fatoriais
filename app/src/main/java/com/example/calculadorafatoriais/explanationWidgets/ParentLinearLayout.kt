package com.example.calculadorafatoriais.explanationWidgets

import android.content.Context
import android.widget.LinearLayout
import com.example.calculadorafatoriais.SetLayoutParams

/** Layout usado como Layout Pai para fórmulas de Análise Combinatória. Define seus parâmetros de
 * layout e margens em seu construtor. */
open class ParentLinearLayout(context: Context) : LinearLayout(context), SetLayoutParams {
    init {
        val params by lazy { configureLayoutParams(context) }
        this.layoutParams = params
        this.orientation = HORIZONTAL
    }
}