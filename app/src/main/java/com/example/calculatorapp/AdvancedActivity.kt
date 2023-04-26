package com.example.calculatorapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import net.objecthunter.exp4j.ExpressionBuilder

class AdvancedActivity : AppCompatActivity() {
    private lateinit var previousCalculation: TextView
    private lateinit var display: TextView
    private var paranthesisCounter = 0
    private var dotCounter = 0
    private var lastChar = ""
    private var showZeroFlag = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)
        previousCalculation = findViewById(R.id.previousCalculationView)
        display = findViewById(R.id.displayEditText)

        previousCalculation.showSoftInputOnFocus = false
        display.showSoftInputOnFocus = false
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {

        savedInstanceState.putString("display", display.text.toString())
        savedInstanceState.putString("currentCalculation", previousCalculation.text.toString())

        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        display.text = savedInstanceState.getString("display")
        previousCalculation.text = savedInstanceState.getString("currentCalculation")
    }

    private fun updateText(strToAdd: String) {
        display.append(strToAdd)
        if(strToAdd.isEmpty()) {
            lastChar = ""
            return
        }
        lastChar = strToAdd.last().toString()
    }

    fun textButtonPush(view: View) {
        val b = view as Button
        updateText(b.text.toString())
        val out = calculateResult()
        if(showZeroFlag == 1) {
            showZeroFlag = 0
        }
        previousCalculation.text = out
    }

    fun dotPush(view: View) {
        if(dotCounter == 0) {
            if(lastChar in "+-*/(") {
                updateText("0.")
            } else {
                updateText(".")
            }
            dotCounter++
        }
    }

    fun operatorsPush(view: View) {
        val b = view as Button
        val newoperator = b.text.toString()
        if(newoperator == lastChar) {
            showToast(this, "Something went wrong")
            return
        }
        if(display.text.isEmpty() && newoperator in "/*") {
            showToast(this, "Something went wrong")
            return
        }
        if(lastChar == "(" && newoperator in "+-") {
            updateText(newoperator)
            previousCalculation.text = calculateResult()
            dotCounter = 0
            return
        }
        if(lastChar in "0123456789") {
            updateText(newoperator)
            previousCalculation.text = calculateResult()
            dotCounter = 0
            return
        }
        if(newoperator in "+-/*") {
            if(newoperator in "/*") {
                if(display.text.length > 1 && display.text[display.text.lastIndex - 1] == '(') {
                    showToast(this, "Something went wrong")
                    return
                }
                if(display.text.isNotEmpty() && display.text.last() == '(') {
                    showToast(this, "Something went wrong")
                    return
                }
            }
            if(lastChar == ".") {
                display.append("0")
            } else if(lastChar != ")"){
                display.text = display.text.dropLast(1)
            }
            updateText(newoperator)
            previousCalculation.text = calculateResult()
            dotCounter = 0
            return
        }
    }

    fun paranthesisPush(view: View) {
        dotCounter = 0
        if(display.text.isEmpty() || lastChar == "(") {
            paranthesisCounter++
            updateText("(")
            return
        }
        if(lastChar >= "0" && lastChar <= "9") {
            if(paranthesisCounter > 0) {
                paranthesisCounter--
                updateText(")")
                return
            } else {
                paranthesisCounter++
                updateText("(")
                return
            }
        }
        if(lastChar == ")") {
            if(paranthesisCounter > 0) {
                paranthesisCounter--
                updateText(")")
                return
            } else {
                paranthesisCounter++
                updateText("*(")
                return
            }
        }
        if(lastChar == ".") {
            paranthesisCounter++
            updateText("0*(")
            return
        }
        if(lastChar in "+-/*") {
            paranthesisCounter++
            updateText("(")
        }
        previousCalculation.text = calculateResult()
    }
    fun advancedButtonPush(view: View) {
        val b = view as Button
        val text = b.text.toString()
        var out = ""
        if(lastChar == ".") {
            out = "0*"
        }
        paranthesisCounter++
        if(text.compareTo("sin") == 0) {
            out = out.plus("sin(")
        } else if(text.compareTo("cos") == 0) {
            out = out.plus("cos(")
        } else if(text.compareTo("tan") == 0) {
            out = out.plus("tan(")
        } else if(text.compareTo("sin-1") == 0) {
            out = out.plus("asin(")
        } else if(text.compareTo("cos-1") == 0) {
            out = out.plus("acos(")
        } else if(text.compareTo("tan-1") == 0) {
            out = out.plus("atan(")
        } else if(text.compareTo("x2") == 0) {
            paranthesisCounter--
            if(display.text.isEmpty() || display.text.last() !in "0123456789)") {
                showToast(this, "Something went wrong")
                return
            }
            if(out.isNotEmpty()) {
                out = "0"
            }
            out = out.plus("^(2)")
        } else if(text.compareTo("log") == 0) {
            out = out.plus("log10(")
        } else if(text.compareTo("ln") == 0) {
            out = out.plus("log(")
        } else if(text.compareTo("xy") == 0) {
            if(display.text.isEmpty() || lastChar !in "0123456789") {
                showToast(this, "Something went wrong")
                paranthesisCounter--
                return
            }
            if(out.isNotEmpty()) {
                out = "0"
            }
            out = out.plus("^(")
        } else if(text.compareTo("sqrt") == 0) {
            out = out.plus("sqrt(")
        } else if(text.compareTo("%") == 0) {
            paranthesisCounter--
            if(display.text.isEmpty() || display.text.last() !in "0123456789)") {
                showToast(this, "Something went wrong")
                return
            }
            out = out.plus("%")
        }
        updateText(out)
        previousCalculation.text = calculateResult()
    }

    fun backspaceText(view: View) {
        if(lastChar == "."&& dotCounter > 0) {
            dotCounter--
        }
        if(lastChar == "(" && paranthesisCounter > 0) {
            paranthesisCounter--
        }
        if(lastChar in "+-/*") {
            dotCounter++
        }
        if(lastChar in "0123456789+-/*).^%") {
            display.text = display.text.dropLast(1)
            if(display.text.isNotEmpty()) {
                lastChar = display.text.last().toString()
            } else {
                lastChar = ""
            }
            return
        }
        if(lastChar == "(") {
            // log10(
            val temp = display.text.takeLast(6).toString()
            println("temp: " + temp)
            if(temp == "log10(") {
                println("Here")
                display.text = display.text.dropLast(6)
                if(display.text.isNotEmpty()) {
                    lastChar = display.text.last().toString()
                } else {
                    lastChar = ""
                }
                return
            }

            val index = display.text.lastIndex
            var counter = 1
            while (index - counter < 0 || display.text[index - counter] !in "0123456789+-*/()^%") {
                counter++
                if(index - counter < 0) {
                    break
                }
            }
            display.text = display.text.dropLast(counter)
        }
        if(display.text.isNotEmpty()) {
            lastChar = display.text.last().toString()
        } else {
            lastChar = ""
        }
        previousCalculation.text = calculateResult()
    }

    fun clearText(view: View) {
        if(display.text.isEmpty()) {
            previousCalculation.text = ""
            return
        }
        display.text = ""
        paranthesisCounter = 0
        dotCounter = 0
    }

    fun allClearText(view: View) {
        display.text = ""
        previousCalculation.text = ""
        paranthesisCounter = 0
        dotCounter = 0
    }

    fun equalsExpression(view: View) {
        if(display.text.isEmpty() && previousCalculation.text.isNotEmpty()) {
            display.text = previousCalculation.text
            previousCalculation.text = ""
            paranthesisCounter = 0
            if("." !in previousCalculation.text.toString())  {
                dotCounter = 0
            } else {
                dotCounter = 1
            }
            return
        }
        val result = calculateResult()
        if(result.isNotEmpty()) {
            if(result == "NaN") {
                showToast(this, "Something went wrong")
                display.text = ""
            } else {
                display.text = result
            }
            previousCalculation.text = ""
            paranthesisCounter = 0
            if("." !in display.text.toString())  {
                dotCounter = 0
            } else {
                dotCounter = 1
            }
        } else {
            if(showZeroFlag == 1) {
                showToast(this,"Division by zero")
                showZeroFlag = 0
            } else {
                showToast(this, "Expression is incorrect")
            }
        }
    }

    private fun calculateResult() : String {
        val out: Double
        var outExpression  = display.text.toString()
        var paranthesisTemp = paranthesisCounter
        while (paranthesisTemp != 0) {
            outExpression = outExpression.plus(")")
            paranthesisTemp--
        }
        outExpression = outExpression.replace("%", "*0.01")
        println(outExpression)
        try {
            val expression = ExpressionBuilder(outExpression).build()
            out = expression.evaluate()
            println(out)
        } catch(e: Exception) {
            when(e) {
                is ArithmeticException -> showZeroFlag = 1
//                is IllegalArgumentException -> println("Illegal arguments")
            }
            return ""
        }
        return out.toString()
    }

    // display error message
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}