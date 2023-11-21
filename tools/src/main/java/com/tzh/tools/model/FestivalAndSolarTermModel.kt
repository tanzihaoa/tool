package com.tzh.tools.model

import android.os.Parcelable
import android.text.SpannableStringBuilder
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Keep
@Parcelize
data class SolarTermModel(val solarTerm: String, val date: String, val isSelect: Boolean) :
    Parcelable

@Keep
@Parcelize
data class PublicVacationChildModel(
    val month: String,
    val day: String,
    val name: String,
    val lunar: String,
    val aFewDay: String
) : Parcelable

@Keep
@Parcelize
data class PublicVacationModel(val year: String, val list: List<PublicVacationChildModel>) :
    Parcelable

@Keep
@Parcelize
data class HolidaysChildModel(
    val monthDay: String,
    val week: String,
    val name: String,
    val content: @RawValue SpannableStringBuilder
) : Parcelable

@Keep
@Parcelize
data class HolidaysModel(val year: String, val list: List<HolidaysChildModel>) : Parcelable