package com.example.myapplication.network.entity

import com.example.myapplication.network.response.User

class Company(
    var companyId: Int? = null,
    var name: String? = null,
    var dateCreated: String? = null,
    var lastUsedTime: String? = null,
    var countOfIssue: Int? = null,
    var countOfEmployees: Int? = null,
    var registrant: User? = null
)
