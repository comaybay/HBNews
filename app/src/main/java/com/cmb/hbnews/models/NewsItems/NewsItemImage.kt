package com.cmb.hbnews.models.NewsItems

/**
 * Thành phần nội dung ảnh của bài báo
 * @param imgSrc link hình ảnh
 * @param caption caption ảnh
 */
class NewsItemImage(val imgSrc: String, val caption: String) : NewsItem(NewsItem.Type.IMAGE)
