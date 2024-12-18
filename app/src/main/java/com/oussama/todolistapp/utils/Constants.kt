package com.oussama.todolistapp.utils

object Constants {
    // Intent Keys
    const val EXTRA_TASK = "extra_task"
    const val EXTRA_TASK_ID = "extra_task_id"

    // Bundle Keys
    const val KEY_TASK = "key_task"
    const val KEY_TASK_ID = "key_task_id"

    // SharedPreferences Keys
    const val PREF_SORT_ORDER = "pref_sort_order"
    const val PREF_FILTER_TYPE = "pref_filter_type"

    // Request Codes
    const val REQUEST_CREATE_TASK = 1001
    const val REQUEST_EDIT_TASK = 1002

    // Sort Orders
    const val SORT_BY_DATE = "sort_by_date"
    const val SORT_BY_PRIORITY = "sort_by_priority"

    // Filter Types
    const val FILTER_ALL = "filter_all"
    const val FILTER_COMPLETED = "filter_completed"
    const val FILTER_PENDING = "filter_pending"

    // Default Values
    const val DEFAULT_SORT_ORDER = SORT_BY_DATE
    const val DEFAULT_FILTER_TYPE = FILTER_ALL
}