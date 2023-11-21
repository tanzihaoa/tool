package com.tzh.tools.model

import androidx.annotation.Keep

@Keep
data class FormulaMathsBean(
    val name: String, val formulaList: List<String>, val notes: String
)
