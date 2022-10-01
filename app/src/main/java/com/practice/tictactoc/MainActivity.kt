package com.practice.tictactoc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.tictactoc.domain.Simbol
import com.practice.tictactoc.domain.Square
import com.practice.tictactoc.ui.theme.TicTacTocTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacTocTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {

                    var greenWins by remember{
                        mutableStateOf(0)
                    }
                    var redWins by remember{
                        mutableStateOf(0)
                    }
                    var turn by remember{
                        mutableStateOf(0)
                    }


                    Column(modifier = Modifier.fillMaxSize()) {
                        // Top Info
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Green\n   $greenWins",
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.primary,
                            )
                            Spacer(modifier = Modifier.width(50.dp))

                            
                            Text(
                                text = "Red\n   $redWins",
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.primary,
                            )

                        }
                        //Main Game

                        BoxWithConstraints(modifier = Modifier
                            .fillMaxSize()
                            .padding(40.dp, 120.dp)
                            ) {

                            val gameSize = Size(this.constraints.maxWidth.toFloat(),this.constraints.maxHeight.toFloat())

                            var table by remember{
                                mutableStateOf(emptyGame(gameSize))
                            }
                            Log.i("AAA","TableSize = ${table.size}")


                            val thirdOffset =Offset(
                                    x = this.constraints.maxWidth / 3f,
                                    y = this.constraints.maxHeight / 3f
                                )

                            val tablePath = Path().apply{
                                moveTo(thirdOffset.x,0f)
                                lineTo(thirdOffset.x,thirdOffset.y * 3)
                                moveTo(thirdOffset.x * 2,0f)
                                lineTo(thirdOffset.x * 2,thirdOffset.y * 3)
                                moveTo(0f,thirdOffset.y)
                                lineTo(thirdOffset.x * 3,thirdOffset.y)
                                moveTo(0f,thirdOffset.y*2)
                                lineTo(thirdOffset.x * 3,thirdOffset.y*2)
                            }

                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pointerInput(turn) {
                                        this.detectTapGestures {
                                            val index = getIndexByTap(it,
                                                gameSize = Size(constraints.maxWidth.toFloat(),
                                                    constraints.maxHeight.toFloat()))
                                            val newValue: Simbol
                                            when (turn) {
                                                0 -> {
                                                    newValue = Simbol.Circle
                                                    turn = 1
                                                }
                                                else -> {
                                                    newValue = Simbol.Cross
                                                    turn = 0
                                                }
                                            }
                                            table[index] = table[index].copy(value = newValue)

                                            if (isGameOver(table)) {

                                                when (turn) {
                                                    1 -> greenWins++
                                                    0 -> redWins++
                                                }

                                                table = emptyGame(gameSize)
                                            }
                                        }
                                    }
                            ){

                                drawPath(tablePath, color = Color.Black, style = Stroke(5.dp.toPx()))

                                Log.i("AAA","TableSize = ${table.size}")

                                for(i in 0..8){
                                    when(table[i].value){
                                        is Simbol.Circle -> {
                                            drawPath(
                                                color = Color.Green,
                                                path = Simbol.getCirclePath(table[i].center),
                                                style = Stroke(5.dp.toPx())
                                            )
                                        }
                                        is Simbol.Cross -> {
                                            drawPath(
                                                color = Color.Red,
                                                path = Simbol.getCrossPath(table[i].center),
                                                style = Stroke(5.dp.toPx(),cap = StrokeCap.Round)
                                            )
                                        }

                                        else -> {
//                                            val center = table[i].center
//                                            drawLine(
//                                                Color.Gray,
//                                                start = Offset(center.x - 5.dp.toPx(),center.y),
//                                                end = Offset(center.x + 5.dp.toPx(),center.y),
//                                                5.dp.toPx()
//                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().height(100.dp).alpha(0.2f).background(Color.Red)
                        ) {
                            Button(onClick = { /*TODO*/ }) {
                                Text(
                                    text = "Reset",
                                    style = MaterialTheme.typography.h3,
                                    color = MaterialTheme.colors.primary,

                                )
                            }

                        }

                    }

                }
            }
        }
    }
}

private fun get9Centers(gameSize:Size):Array<Offset>{

    val unitX = gameSize.width / 6
    val unitY = gameSize.height / 6

    return arrayOf(
        Offset(unitX,unitY),Offset(unitX*3,unitY),Offset(unitX*5,unitY),
        Offset(unitX,unitY*3),Offset(unitX*3,unitY*3),Offset(unitX*5,unitY*3),
        Offset(unitX,unitY*5),Offset(unitX*3,unitY*5),Offset(unitX*5,unitY*5),
    )

}

private fun emptyGame(gameSize: Size): Array<Square>{
    val gameCenter = gameSize.center
    val game9Centers = get9Centers(gameSize)
    val table:MutableList<Square> = mutableListOf<Square>()

    for (i in 0..8){
        table.add(Square(i,game9Centers[i],Simbol.Empty))
    }

    return table.toTypedArray()

}

fun getIndexByTap(tapOffset: Offset,gameSize: Size): Int{

    val thirdX = gameSize.width / 3
    val thirdY = gameSize.height / 3
    val tapX = tapOffset.x
    val tapY = tapOffset.y
    val i = when{
        (tapX <= thirdX) -> 0
        (thirdX < tapX) && (tapX <= thirdX*2) -> 1
        (thirdX*2 < tapX) -> 2
        else -> 0
    }
    val j = when{
        (tapY <= thirdY) -> 0
        (thirdY < tapY) && (tapY <= thirdY*2) -> 1
        (thirdY*2 < tapY) -> 2
        else -> 0
    }
    val res = j * 3 + i
    Log.i("AAA","res: $res")
    return res
}

fun isGameOver(table:Array<Square>):Boolean{
    //Vertical Win
    for(i in 0..2){
        if(table[0+i*3].value == table[1+i*3].value &&
                table[1+i*3].value == table[2+i*3].value){
            if(table[0+i*3].value != Simbol.Empty){
                return true
            }
        }
    }
    //Horizontal Win
    for(i in 0..2){
        if(table[0+i].value == table[3+i].value &&
            table[3+i].value == table[6+i].value){
            if(table[0+i].value != Simbol.Empty){
                return true
            }
        }
    }
    //Diagonal Win
    if((table[0].value == table[4].value && table[4].value == table[8].value) ||
        (table[2].value == table[4].value && table[4].value == table[6].value)){
        if(table[4].value != Simbol.Empty){
            return true
        }
    }
    return false
}

