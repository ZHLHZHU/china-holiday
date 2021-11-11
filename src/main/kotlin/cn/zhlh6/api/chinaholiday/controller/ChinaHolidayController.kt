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

    @GetMapping("/holiday/{year}/{month}/{day}")
    fun holiday(@PathVariable year: Int, @PathVariable month: Int, @PathVariable day: Int): HolidayBean {
        val date = LocalDate.of(year, month, day)

        dataRepository.get(date)?.let { return it }

        if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY) {
            return HolidayBean(date, true, "周末")
        }
        return HolidayBean(date, false, "工作日")
    }

}