package com.entremp.core.entremp.support.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.vendors.FunctionProvider

open class CustomMariaDBFunctionProvider : FunctionProvider() {

    private object CharColumnType : StringColumnType() {
        override fun sqlType(): String = "CHAR"
    }

    override fun cast(expr: Expression<*>, type: IColumnType, builder: QueryBuilder): Unit = when (type) {
        is StringColumnType -> super.cast(expr, CharColumnType, builder)
        else -> super.cast(expr, type, builder)
    }

    override fun random(seed: Int?) = "RAND(${seed?.toString().orEmpty()})"

    override fun <T : String?> ExpressionWithColumnType<T>.match(pattern: String, mode: MatchMode?): Op<Boolean> = MATCH(this, pattern, mode ?: MysqlMatchMode.STRICT)

    override fun replace(table: Table, data: List<Pair<Column<*>, Any?>>, transaction: Transaction): String {
        val builder = QueryBuilder(true)
        val columns = data.joinToString { transaction.identity(it.first) }
        val values = builder.apply { data.appendTo { registerArgument(it.first.columnType, it.second) } }.toString()
        return "REPLACE INTO ${transaction.identity(table)} ($columns) VALUES ($values)"
    }

    override val DEFAULT_VALUE_EXPRESSION: String = "() VALUES ()"

    override fun insert(ignore: Boolean, table: Table, columns: List<Column<*>>, expr: String, transaction: Transaction): String {
        val def = super.insert(false, table, columns, expr, transaction)
        return if (ignore) def.replaceFirst("INSERT", "INSERT IGNORE") else def
    }

    override fun delete(ignore: Boolean, table: Table, where: String?, limit: Int?, transaction: Transaction): String {
        val def = super.delete(false, table, where, limit, transaction)
        return if (ignore) def.replaceFirst("DELETE", "DELETE IGNORE") else def
    }

    override fun <T : String?> regexp(expr1: Expression<T>, pattern: Expression<String>, caseSensitive: Boolean, queryBuilder: QueryBuilder): Unit = queryBuilder {
        append(expr1, " REGEXP ", pattern)
    }


    private class MATCH(val expr: ExpressionWithColumnType<*>, val pattern: String, val mode: MatchMode) : Op<Boolean>() {
        override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder {
            append("MATCH(", expr, ") AGAINST ('", pattern, "' ", mode.mode(),")")
        }
    }

    private enum class MysqlMatchMode(val operator: String) : MatchMode {
        STRICT("IN BOOLEAN MODE"),
        NATURAL_LANGUAGE("IN NATURAL LANGUAGE MODE");

        override fun mode() = operator
    }
}