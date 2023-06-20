package com.example.calculadorafatoriais.explanationWidgets

import android.app.ActionBar
import android.content.Context
import android.util.TypedValue
import android.widget.LinearLayout

class ParentLinearLayout(context: Context) : LinearLayout(context) {
    init {
        this.orientation = HORIZONTAL
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
        fun dpToPx(context: Context, size: Float): Int { return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics).toInt() }
        params.setMargins(dpToPx(context, 20F), dpToPx(context, 25F), 0, 0)
        this.layoutParams = params
    }
}