package store.model

import java.time.LocalDate

data class Promotion(
    val name : String,
    val buyCount : Int,
    val getCount : Int,
    val startDate : LocalDate,
    val endDate : LocalDate
)
