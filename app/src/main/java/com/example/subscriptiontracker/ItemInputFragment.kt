package com.example.subscriptiontracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




class ItemInputFragment : Fragment() {

    private lateinit var viewModel: SubscriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SubscriptionViewModel::class.java]

        val buttonSave: Button = view.findViewById(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.newName).text.toString()
            val desc = view.findViewById<EditText>(R.id.newDesc).text.toString()
            val cat = view.findViewById<EditText>(R.id.newCat).text.toString()

            val payString = view.findViewById<EditText>(R.id.newPay).text.toString()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val pay = try {
                formatter.parse(payString) ?: Date()
            } catch (e: ParseException) {
                Date()
            }

            val cycle = view.findViewById<EditText>(R.id.newCycle).text.toString()

            val amountString = view.findViewById<EditText>(R.id.newAmount).text.toString()
            val amount = amountString.toDoubleOrNull() ?: 0.0

            val currency = view.findViewById<EditText>(R.id.newCurrency).text.toString()
            val payMet = view.findViewById<EditText>(R.id.newPayMet).text.toString()
            val remind = view.findViewById<EditText>(R.id.newRemind).text.toString()

            val newItem = SubscriptionItem(0, name, desc, cat, pay, cycle, amount, currency, payMet, remind)

            viewModel.addItem(newItem)
            findNavController().navigateUp()

        }
    }
}
