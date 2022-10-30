package com.example.myapplication.network.response

class Wrapper<T>(
    var code: String? = null,
    var msg: String? = null,
    var data: T? = null
)

class Page<T>(
    var pageNum: Int? = null,
    var pageSize: Int? = null,
    var pages: Int? = null,
    var total: Int? = null,
    var list: List<T>? = null,
)
