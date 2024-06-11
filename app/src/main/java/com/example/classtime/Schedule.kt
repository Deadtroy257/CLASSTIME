package com.example.classtime

data class Schedule(
    var id: String? = null,
    var title: String = "",
    var schedule: Map<String, Map<String, String>> = mapOf() // nested maps for schedule
)
