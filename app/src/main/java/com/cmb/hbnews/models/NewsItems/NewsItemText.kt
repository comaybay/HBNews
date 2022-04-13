package com.cmb.hbnews.models.NewsItems

/**
 * Thành phần nội dung văn bản của bài báo
 * @param text nội dung văn bản
 * @param textType loại văn bản, như paragraph, heading, ...
 */
class NewsItemText(val text: String, val textType: TextType) : NewsItem(NewsItem.Type.TEXT) {
    enum class TextType {
        P, H2
    }
}
