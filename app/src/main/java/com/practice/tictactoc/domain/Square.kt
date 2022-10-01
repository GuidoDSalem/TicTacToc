package com.practice.tictactoc.domain

import androidx.compose.ui.geometry.Offset

data class Square(
    val id:Int,
    val center: Offset,
    val value: Simbol,
)

