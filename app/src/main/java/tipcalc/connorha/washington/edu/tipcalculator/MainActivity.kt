package tipcalc.connorha.washington.edu.tipcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var tipPercentageOptions = arrayOf("10%", "15%", "18%", "20%")
    var tipPercentage = 15
    var billAmount: String = "0000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        billAmountText.setSelection(6)
        calculateTipButton.isEnabled = false
        initializeSpinner()
        setBillAmountTextChangeListener()
        setCalculateTipButtonListener()
    }

    private fun initializeSpinner() {
        var tipPercentageSpinner = findViewById(R.id.tipPercentageSpinner) as Spinner
        var arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipPercentageOptions)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipPercentageSpinner.adapter = arrayAdapter
        tipPercentageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tipPercentage = tipPercentageOptions[position].substring(0,2).toInt()
            }
        }
        //sets default tip amount to 15%
        tipPercentageSpinner.setSelection(1)
    }

    private fun setBillAmountTextChangeListener() {
        billAmountText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var billInput = billAmountText.text.toString()
                var billAmountBeforeUpdate = billAmount
                updateBillAmountFromInput(billInput)
                var billAmountAfterUpdate = billAmount
                if (billDisplayNeedsToUpdate(billInput, billAmountBeforeUpdate, billAmountAfterUpdate)) {
                    updateBillAmountDisplay()
                    calculateTipButton.isEnabled = true
                }
            }
        })
    }

    private fun setCalculateTipButtonListener() {
        calculateTipButton.setOnClickListener {
            var tipAmount = calculateTip()
            displayTipAmount(tipAmount)
        }
    }


    private fun billDisplayNeedsToUpdate(billInput: String, billAmountBeforeUpdate: String, billAmountAfterUpdate: String): Boolean {
        return incorrectNumberOfCharactersInDisplay(billInput) || beforeStringDoesNotEqualAfterString(billAmountBeforeUpdate, billAmountAfterUpdate)
    }

    private fun incorrectNumberOfCharactersInDisplay(inputString : String): Boolean {
        return inputString.length != 6
    }

    private fun beforeStringDoesNotEqualAfterString(billAmountBeforeUpdate: String, billAmountAfterUpdate: String): Boolean {
        return billAmountBeforeUpdate != billAmountAfterUpdate
    }

    private fun updateBillAmountFromInput (inputText: String) {
        var filteredString = ""
        for (character: Char in inputText) {
            if (character.isDigit()) {
                filteredString += character
            }
        }
        if (filteredString.length < 4) {
            billAmount = "0$filteredString"
        } else {
            billAmount =  filteredString.takeLast(4)
        }
    }

    private fun updateBillAmountDisplay() {
        var stringToDisplay = "$"
        stringToDisplay += (billAmount.substring(0, 2))
        stringToDisplay += ('.')
        stringToDisplay += (billAmount.substring(2,4))
        billAmountText.setText(stringToDisplay)
        billAmountText.setSelection(6)
    }

    private fun calculateTip (): String {
        return  "%.2f".format(((billAmount.toDouble() * tipPercentage) / 10000))
    }

    private fun displayTipAmount (tipAmount: String) {
        Toast.makeText(this@MainActivity, "Tip: $$tipAmount", Toast.LENGTH_LONG).show()
    }

}
