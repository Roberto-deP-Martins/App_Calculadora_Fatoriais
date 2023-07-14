package com.example.calculadorafatoriais

import android.app.ActionBar
import android.content.Context
import android.util.TypedValue

/**Configurando parametros para layout da classe */
interface SetLayoutParams {
    private fun dpToPx(context: Context, size: Float): Int { return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics).toInt() }

    fun configureLayoutParams(context: Context): ActionBar.LayoutParams {  // Configura margem do LinearLayout
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
        params.setMargins(dpToPx(context, 20F), dpToPx(context, 25F), 0, 0)
        return params
    }
}