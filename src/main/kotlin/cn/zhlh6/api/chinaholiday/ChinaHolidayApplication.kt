package cn.zhlh6.api.chinaholiday

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChinaHolidayApplication

fun main(args: Array<String>) {
    val app = runApplication<ChinaHolidayApplication>(*args)
    app.getBean("globalReturnConfig")
}
