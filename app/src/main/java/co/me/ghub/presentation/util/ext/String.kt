package co.me.ghub.presentation.util.ext

fun String.toSqlLikeTemplate(): String = "%$this%"