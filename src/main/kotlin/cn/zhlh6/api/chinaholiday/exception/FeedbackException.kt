package cn.zhlh6.api.chinaholiday.exception

/**
 * create 2021/11/10 17:07
 * 一些需要
 * @author LH
 */
class FeedbackException(
    val code: Int = 500,
    val msg: String = "unknown error"
) : Exception() {

}