package com.youthtalk.util

import com.youthtalk.model.typeenum.SpecializedType

object SpecializedUtils {
    val job = listOf(
        SpecializedType.SMALL_BUSINESS,
        SpecializedType.FARMER,
        SpecializedType.SOLDIER
    )

    val weeks = listOf(
        SpecializedType.WOMEN,
        SpecializedType.WELFARE_RECIPIENT,
        SpecializedType.DISABLED,
        SpecializedType.SINGLE_PARENT
    )

    val etc = listOf(
        SpecializedType.REGIONAL_TALENT,
        SpecializedType.OTHER
    )

    fun getAllList(types: List<SpecializedType>): List<SpecializedType> = listOf(SpecializedType.UNRESTRICTED) + types

    fun isChecked(value: List<SpecializedType>, item: SpecializedType, specials: List<SpecializedType>?): Boolean {
        if (specials.isNullOrEmpty()) return false
        if (specials.containsAll(value)) return item == SpecializedType.UNRESTRICTED
        return specials.contains(item)
    }

    fun changeSpecialized(value: List<SpecializedType>, item: SpecializedType, specials: List<SpecializedType>?): List<SpecializedType>? {
        val newFilter = when (item) {
            SpecializedType.UNRESTRICTED -> {
                if (specials.isNullOrEmpty()) {
                    return value
                } else if (specials.containsAll(value)) {
                    specials - value.toSet()
                } else {
                    specials + value
                }
            }

            else -> {
                val currentFilter = specials ?: listOf()
                val changes = if (currentFilter.contains(item)) {
                    currentFilter - item
                } else {
                    currentFilter + item
                }
                changes
            }
        }

        return newFilter
    }

    fun getFilterList(values: List<SpecializedType>?): List<SpecializedType>? {
        return values?.let { list ->
            val jobList = if (job.none { list.contains(it) }) {
                list + job
            } else {
                listOf()
            }

            val weekList = if (weeks.none { list.contains(it) }) {
                list + weeks
            } else {
                listOf()
            }

            val ectList = if (etc.none { list.contains(it) }) {
                list + etc
            } else {
                listOf()
            }

            values + jobList + weekList + ectList
        }
    }
}
