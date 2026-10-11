package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RetroCalculatorApp(
    onClose: () -> Unit
) {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var operator by remember { mutableStateOf<String?>(null) }
    var resetOnNext by remember { mutableStateOf(false) }

    fun onNumber(num: String) {
        if (display == "0" || resetOnNext) {
            display = num
            resetOnNext = false
        } else {
            if (display.length < 9) display += num
        }
    }

    fun onOperator(op: String) {
        val current = display.toDoubleOrNull() ?: 0.0
        operand1 = current
        operator = op
        resetOnNext = true
    }

    fun onEquals() {
        val op = operator ?: return
        val op1 = operand1 ?: return
        val op2 = display.toDoubleOrNull() ?: 0.0
        val result = when (op) {
            "+" -> op1 + op2
            "−" -> op1 - op2
            "×" -> op1 * op2
            "÷" -> if (op2 != 0.0) op1 / op2 else Double.NaN
            else -> op2
        }
        display = if (result.isNaN()) "Error"
        else if (result % 1.0 == 0.0) result.toLong().toString()
        else String.format(java.util.Locale.US, "%.4f", result).trimEnd('0').trimEnd('.')
        operand1 = null
        operator = null
        resetOnNext = true
    }

    fun onClear() {
        display = "0"
        operand1 = null
        operator = null
        resetOnNext = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
            .padding(16.dp)
    ) {
        // Done button
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF333333))
                    .border(1.dp, Color(0xFF555555), RoundedCornerShape(6.dp))
                    .clickable { onClose() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("◀ Home", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Display
        Text(
            text = display,
            color = Color.White,
            fontSize = 54.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        )

        // Buttons
        val buttons = listOf(
            listOf("C", "±", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "−"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { btn ->
                    val isOp = btn in listOf("÷", "×", "−", "+", "=")
                    val isFunc = btn in listOf("C", "±", "%")
                    val isZero = btn == "0"

                    val bgBrush = if (isOp) {
                        Brush.verticalGradient(listOf(Color(0xFFFFA538), Color(0xFFE88A1A)))
                    } else if (isFunc) {
                        Brush.verticalGradient(listOf(Color(0xFFAFAFAF), Color(0xFF888888)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF4C4C4C), Color(0xFF2E2E2E)))
                    }

                    val textColor = if (isFunc) Color.Black else Color.White

                    Box(
                        modifier = Modifier
                            .weight(if (isZero) 2f else 1f)
                            .height(64.dp)
                            .clip(if (isZero) RoundedCornerShape(32.dp) else CircleShape)
                            .background(bgBrush)
                            .border(
                                0.5.dp,
                                Color.White.copy(0.2f),
                                if (isZero) RoundedCornerShape(32.dp) else CircleShape
                            )
                            .clickable {
                                when (btn) {
                                    "C" -> onClear()
                                    "±" -> {
                                        val v = display.toDoubleOrNull() ?: 0.0
                                        display = if (v % 1.0 == 0.0) (-v.toLong()).toString() else (-v).toString()
                                    }
                                    "%" -> {
                                        val v = display.toDoubleOrNull() ?: 0.0
                                        display = (v / 100.0).toString()
                                    }
                                    "÷", "×", "−", "+" -> onOperator(btn)
                                    "=" -> onEquals()
                                    else -> onNumber(btn)
                                }
                            },
                        contentAlignment = if (isZero) Alignment.CenterStart else Alignment.Center
                    ) {
                        Text(
                            text = btn,
                            color = textColor,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = if (isZero) Modifier.padding(start = 24.dp) else Modifier
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
