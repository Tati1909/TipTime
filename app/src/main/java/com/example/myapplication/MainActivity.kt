package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    private fun calculateTip() {
        // получаем текст из view costOfService(из разметки макета) и делаем его строкой
        val stringInTextField = binding.costOfService.text.toString()
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
}
