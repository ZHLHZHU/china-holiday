package cn.zhlh6.api.chinaholiday.controller

import cn.zhlh6.api.chinaholiday.data.DataRepository
import cn.zhlh6.api.chinaholiday.model.HolidayBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * create 2021/11/10 14:54
 * @author LH
 */
@RestController
@RequestMapping("/api/v1")
class ChinaHolidayController(
    val dataRepository: DataRepository
) {
    @GetMapping("/holiday/")
    fun holidayToday(): HolidayBean {
        val today = LocalDate.now()
        val holiday = dataRepository.get(today)
        return holiday ?: HolidayBean(today, false, "工作日")
    }

    @GetMapping("/holiday/{year}/{month}/{day}", "/holiday/{year}/{month}", "/holiday/{year}")
    fun holiday(
        @PathVariable year: Int,
        @PathVariable(required = false) month: Int?,
        @PathVariable(required = false) day: Int?
    ): List<HolidayBean> {
        if (month == null) {
            return dataRepository.getWholeYear(LocalDate.of(year, 1, 1))
        }
        if (day == null) {
            return dataRepository.getWholeMonth(LocalDate.of(year, month, 1))
        }
        val date = LocalDate.of(year, month, day)

        dataRepository.get(date)?.let { return listOf(it) }

        if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY) {
            return listOf(HolidayBean(date, true, "周末"))
        }
        return listOf(HolidayBean(date, false, "工作日"))
    }

}