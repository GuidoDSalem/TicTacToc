package com.practice.tictactoc.domain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp


sealed class Simbol(){
    object Cross:Simbol()
    object Circle:Simbol()
    object Empty:Simbol()

    companion object{

        const val SCALE = 10f

        fun getCrossPath(center: Offset): Path {

            val path = Path().apply{
                moveTo(center.x-5f*SCALE,center.y-5f*SCALE)
                lineTo(center.x + 5f*SCALE,center.y + 5f*SCALE)
                moveTo(center.x + 5f*SCALE,center.y - 5f*SCALE)
                lineTo(center.x - 5f*SCALE,center.y + 5f*SCALE)
            }

            return path
        }

        fun getCirclePath(center:Offset): Path{
            val path = Path().apply{
                this.addOval(
                    Rect(
                        center = center, radius = 10f*SCALE
                    )
                )
            }
            return path
        }
    }
}
