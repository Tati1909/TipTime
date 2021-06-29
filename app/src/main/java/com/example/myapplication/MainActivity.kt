package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //устанавливаем прослушиватель на кнопку
        binding.calculateButton.setOnClickListener{ calculateTip() }
//слушатель кнопки enter после ввода стоимости услуги(скрыть клавиатуру)
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }
    }

    private fun calculateTip() {
        // получаем текст из view costOfService(из разметки макета) и делаем его строкой
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        //преобразуем текст стоимости заказа в 10-чное число (стоимость может быть не введена)
        val cost = stringInTextField.toDoubleOrNull()
        //Это очистит сумму чаевых перед возвратом из calculateTip()
        if (cost == null) {
            binding.tipResult.text = ""
            return
        }
        //процент чаевых, который выбрал пользователь
        val selectedId = binding.tipOptions.checkedRadioButtonId
        //получаем выбранный процент чаевых
        val tipPercentage = when (selectedId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        //значение чаевых
        var tip = tipPercentage * cost
        //boolean значение - округлять чаевые или не округлять
        //Для элемента `Switch` вы можете проверить атрибут` isChecked`, чтобы увидеть,
        // включен ли переключатель.
        val roundUp = binding.roundUpSwitch.isChecked

        //если включатель округления включен, то
        //нужно округлить чаевые в большую сторону
        if (roundUp) {
            tip = kotlin.math.ceil(tip)
        }

        //форматируем числа как валюту разных стран (1 234,56 доллара США, а в евро - 1,234,56 евро)
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
//отображаем чаевые внизу справа
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

    }

    //метод для скрытия клавиатуры после enter
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}
