package com.example.homework3

fun contacts(): MutableList<ContactModel> {
    val list = mutableListOf<ContactModel>()
    for (i in 1..100) {
        if (i < 10) {
            list.add(
                ContactModel(
                    "First Name: $i",
                    "Last Name: $i",
                    "+7 800 $i$i$i $i$i $i$i",
                    false,
                    i
                )
            )
        } else {
            list.add(
                ContactModel(
                    "First Name: $i",
                    "Last Name: $i",
                    "+7 800 ${i % 10}$i $i $i",
                    false,
                    i
                )
            )
        }

    }
    return list
}