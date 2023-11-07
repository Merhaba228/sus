package com.example.sus.activity.logic.auth.retrofit.dto

enum class Schedule(val time: String, val breakTime: String) {
    LESSON_1("8.30 - 10.00", "10 мин"),
    LESSON_2("10.10 - 11.40", "20 мин"),
    LESSON_3("12.00 - 13.30", "15 мин"),
    LESSON_4("13.45 - 15.15", "10 мин"),
    LESSON_5("15.25 - 16.55", "10 мин"),
    LESSON_6("17.05 - 18.35", "5 мин"),
    LESSON_7("18.40 - 20.10", "-");

    companion object {
        fun getByNumber(number: Int): Schedule? {
            return values().find { it.ordinal + 1 == number }
        }
    }
}
