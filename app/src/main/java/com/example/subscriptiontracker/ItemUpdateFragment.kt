package com.example.subscriptiontracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController



class ItemUpdateFragment : Fragment() {

    private lateinit var viewModel: SubscriptionViewModel
    private var itemPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SubscriptionViewModel::class.java]

        arguments?.let {
            itemPosition = it.getInt("itemPosition", -1)
        }

        if (itemPosition != -1) {
            val item = viewModel.getItem(itemPosition)
            view.findViewById<EditText>(R.id.updateName).setText(item.name)
            view.findViewById<EditText>(R.id.updateDesc).setText(item.desc)
            view.findViewById<EditText>(R.id.updateCat).setText(item.cat)
            view.findViewById<EditText>(R.id.updatePay).setText(item.pay)
            view.findViewById<EditText>(R.id.updateCycle).setText(item.cycle)
            view.findViewById<EditText>(R.id.updateAmount).setText(item.amount)
            view.findViewById<EditText>(R.id.updateCurrency).setText(item.currency)
            view.findViewById<EditText>(R.id.updatePayMet).setText(item.payMet)
            view.findViewById<EditText>(R.id.updateRemind).setText(item.remind)
        }

        val buttonUpdate: Button = view.findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.updateName).text.toString()
            val desc = view.findViewById<EditText>(R.id.updateDesc).text.toString()
            val cat = view.findViewById<EditText>(R.id.updateCat).text.toString()
            val pay = view.findViewById<EditText>(R.id.updatePay).text.toString()
            val cycle = view.findViewById<EditText>(R.id.updateCycle).text.toString()
            val amount = view.findViewById<EditText>(R.id.updateAmount).text.toString()
            val currency = view.findViewById<EditText>(R.id.updateCurrency).text.toString()
            val payMet = view.findViewById<EditText>(R.id.updatePayMet).text.toString()
            val remind = view.findViewById<EditText>(R.id.updateRemind).text.toString()

            val updatedItem = SubscriptionItem(name, desc, cat, pay, cycle, amount, currency, payMet, remind)
            viewModel.updateItem(itemPosition, updatedItem)

            findNavController().navigateUp()
        }

        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            viewModel.deleteItem(itemPosition)

            findNavController().navigateUp()


        }
    }
}
