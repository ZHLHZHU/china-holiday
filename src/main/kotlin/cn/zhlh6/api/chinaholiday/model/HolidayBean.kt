package cn.zhlh6.api.chinaholiday.model

import java.time.LocalDate

/**
 * create 2021/11/10 14:55
 * @author LH
 */
data class HolidayBean(
    val date: LocalDate,
    val holiday: Boolean,
    val name: String = "",
)
