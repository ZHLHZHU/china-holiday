package cn.zhlh6.api.chinaholiday.data

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

/**
 * create 2021/11/11 12:17
 * @author LH
 */
@SpringBootTest
class DataRepositoryTest(
    @Autowired val dateRepository: DataRepository
) {

    @Test
    fun getHoliday() {
        val testDate = LocalDate.of(2021, 10, 1)
        assertNotNull(dateRepository.get(testDate))

        val singleDogDate = LocalDate.of(2021, 11, 11)
        assertNull(dateRepository.get(singleDogDate))
    }
}