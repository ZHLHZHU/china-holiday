package cn.zhlh6.api.chinaholiday.data

import cn.zhlh6.api.chinaholiday.model.HolidayBean
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.zip.DataFormatException
import javax.annotation.PostConstruct

/**
 * create 2021/11/10 23:31
 * @author LH
 */
@Component
class DataRepository(
    val objectMapper: ObjectMapper
) {
    val holidayIndex = Array(100) { Array(15) { IntArray(35) { 0 } } }

    val holidayData = mutableMapOf<Int, HolidayBean>()

    /**
     * load data from json after init
     */
    @PostConstruct
    fun load() {
        val file = File("data/data.json")
        val root = objectMapper.readTree(file)

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var dateIndex = 1

        if (!root.isObject) {
            throw DataFormatException("data.json format error")
        }
        for (entry in root.fields()) {
            if (!entry.value.isArray) {
                throw DataFormatException("data.json format error: $entry")
            }
            for (element in entry.value.elements()) {
                val date = LocalDate.parse(element.get("date").asText(), dateFormatter)
                val holidayBean = HolidayBean(
                    date,
                    element.get("holiday").asBoolean(),
                    element.get("name").asText(),
                )
                holidayData[dateIndex++] = holidayBean
                holidayIndex[date.year - 2000][date.monthValue][date.dayOfMonth] = dateIndex
            }
        }
    }

    fun get(date: LocalDate): HolidayBean? =
        holidayData[holidayIndex[date.year - 2000][date.monthValue][date.dayOfMonth]]

    fun getWholeMonth(date: LocalDate): List<HolidayBean> =
        holidayIndex[date.year - 2000][date.monthValue].map { holidayData[it] }.filterNotNull().toList()

    fun getWholeYear(date: LocalDate): List<HolidayBean> =
        holidayIndex[date.year - 2000].flatMap { it.toList() }.mapNotNull { holidayData[it] }.toList()
}