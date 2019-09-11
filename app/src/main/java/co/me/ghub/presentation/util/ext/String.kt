package co.me.ghub.presentation.util.ext

fun String.toSqlLikeTempalate(): String = "%$this%"