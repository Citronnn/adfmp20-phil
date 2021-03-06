package com.example.philsapp.api

enum class Order {
    ASC,
    DESC
}

class OrderBy(val column: String, val order: Order)

enum class Operator {
    GT,
    LT,
    CONTAINS,
    EQUALS,
    NOTNULL
}

class FilterBy(val column: String, val op: Operator, val value: Any? = null)

class Filter(
    private val limit: Int? = null,
    private val offset: Int? = null,
    private val filter: Array<FilterBy>? = null,
    private val order: Array<OrderBy>? = null
) {
    private fun getFilter(filterBy: FilterBy): String {
        return when (filterBy.op) {
            Operator.GT -> "${filterBy.column} > ${filterBy.value}"
            Operator.LT -> "${filterBy.column} < ${filterBy.value}"
            Operator.CONTAINS -> "${filterBy.column} LIKE '%${filterBy.value}%'"
            Operator.EQUALS -> "${filterBy.column} = ${filterBy.value}"
            Operator.NOTNULL -> "${filterBy.column} IS NOT NULL"
        }
    }

    fun toSql(): String {
        var q = ""
        filter?.let { filter ->
            q += " WHERE "
            q += filter.joinToString(separator = " AND ") { getFilter(it) }
        }
        order?.let { order ->
            q += " ORDER BY "
            q += order.joinToString(separator = ", ") { "${it.column} ${it.order}" }
        }
        limit?.let { q += " LIMIT $it" }
        offset?.let { q += " OFFSET $it" }
        return q
    }
}
