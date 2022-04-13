package com.cmb.hbnews.models.NewsItems

/**
 * Thành phần nội dung của bài báo, đây là lớp trừu tượng, cần chuyển kiểu sang kiểu con để lấy thông tin chi tiết
 * @param type dùng để suy ra kiểu thật sự đằng sau NewsItem.
 */
abstract class NewsItem(val type: Type) {
    enum class Type {
        TEXT, IMAGE
    }
}
