package cn.net.ilean.cms.network.entity

import cn.net.ilean.cms.network.response.User

class Company(
    var companyId: Int? = null,
    var name: String? = null,
    var dateCreated: String? = null,
    var lastUsedTime: String? = null,
    var countOfIssue: Int? = null,
    var countOfEmployees: Int? = null,
    var registrant: User? = null
)
