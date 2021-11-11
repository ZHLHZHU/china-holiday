package cn.zhlh6.api.chinaholiday.config

import cn.zhlh6.api.chinaholiday.exception.FeedbackException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.lang.Nullable
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * create 2021/11/10 15:33
 * 统一返回格式
 * @author LH
 */

@RestControllerAdvice
class GlobalReturnConfig(
    val objectMapper: ObjectMapper
) : ResponseBodyAdvice<Any> {

    override fun supports(
        methodParameter: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        @Nullable body: Any?,
        methodParameter: MethodParameter,
        mediaType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        serverHttpRequest: ServerHttpRequest,
        serverHttpResponse: ServerHttpResponse
    ): Any? {
        return when (body) {
            is Result<*> -> body
            is String -> objectMapper.writeValueAsString(Result.success(body))
            else -> Result.success(body)
        }
    }

    @ExceptionHandler(FeedbackException::class)
    fun handleFeedbackException(e: FeedbackException): Result<*> {
        return e.message.let { Result.fail(e.code, e.msg) }
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): Result<*> {
        return e.message?.let { Result.fail(500, it) } ?: Result.fail(500, "unknown error")
    }

}


data class Result<T>(
    val code: Int,
    val msg: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T): Result<T> {
            return Result(200, "success", data)
        }

        fun fail(code: Int, msg: String): Result<Any> {
            return Result(code, msg)
        }
    }
}