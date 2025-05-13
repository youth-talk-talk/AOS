package com.youthtalk.util

import com.youthtalk.model.enum.SpecializedType

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
        return when (item) {
            SpecializedType.UNRESTRICTED ->
                value.all { filter -> specials?.contains(filter) == false } ||
                    specials == null

            else -> !value.all { filter -> specials?.contains(filter) == true } &&
                specials?.contains(item) == true
        }
    }

    fun changeSpecialized(value: List<SpecializedType>, item: SpecializedType, specials: List<SpecializedType>?): List<SpecializedType>? {
        val newFilter = when (item) {
            SpecializedType.UNRESTRICTED ->
                specials?.filter { filter -> !value.contains(filter) }

            else -> {
                val currentFilter = specials ?: listOf()
                val changes = if (currentFilter.contains(item)) {
                    currentFilter - item
                } else {
                    currentFilter + item
                }
                if (value.all { changes.contains((it)) }) {
                    currentFilter.filter { !value.contains(it) }
                } else {
                    changes
                }
            }
        }

        return if (
            newFilter.isNullOrEmpty() ||
            newFilter.size == SpecializedType.entries.size - 1
        ) {
            null
        } else {
            newFilter
        }
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
