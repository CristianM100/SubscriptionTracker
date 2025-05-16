package com.example.subscriptiontracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ItemUpdateFragment : Fragment() {

    private lateinit var viewModel: SubscriptionViewModel
    private var itemPosition: Int = -1
    private var currentItem: SubscriptionItem? = null  // Hold the item once loaded

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
            viewModel.items.observe(viewLifecycleOwner, Observer { items ->
                if (items != null && itemPosition in items.indices) {
                    val item = items[itemPosition]
                    currentItem = item  // Save for later

                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    view.findViewById<EditText>(R.id.updateName).setText(item.name)
                    view.findViewById<EditText>(R.id.updateDesc).setText(item.desc)
                    view.findViewById<EditText>(R.id.updateCat).setText(item.cat)
                    view.findViewById<EditText>(R.id.updatePay).setText(formatter.format(item.pay))
                    view.findViewById<EditText>(R.id.updateCycle).setText(item.cycle)
                    view.findViewById<EditText>(R.id.updateAmount).setText(String.format(Locale.US, "%.2f", item.amount))
                  //  view.findViewById<EditText>(R.id.updateCurrency).setText(item.currency)
                    view.findViewById<EditText>(R.id.updatePayMet).setText(item.payMet)
                    view.findViewById<EditText>(R.id.updateRemind).setText(item.remind)
                }
            })
        }

        val buttonUpdate: Button = view.findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            val item = currentItem
            if (item != null) {
                val subscriptionId = item.subscriptionId

                val name = view.findViewById<EditText>(R.id.updateName).text.toString()
                val desc = view.findViewById<EditText>(R.id.updateDesc).text.toString()
                val cat = view.findViewById<EditText>(R.id.updateCat).text.toString()

                val payString = view.findViewById<EditText>(R.id.updatePay).text.toString()
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val pay = try {
                    formatter.parse(payString) ?: Date()
                } catch (e: ParseException) {
                    Date()
                }

                val cycle = view.findViewById<EditText>(R.id.updateCycle).text.toString()
                val amountString = view.findViewById<EditText>(R.id.updateAmount).text.toString()
                val amount = amountString.toDoubleOrNull() ?: 0.0
                val currency = view.findViewById<EditText>(R.id.updateCurrency).text.toString()
                val payMet = view.findViewById<EditText>(R.id.updatePayMet).text.toString()
                val remind = view.findViewById<EditText>(R.id.updateRemind).text.toString()

                val updatedItem = SubscriptionItem(subscriptionId, name, desc, cat, pay, cycle, amount, currency, payMet, remind)
               // val updatedItem = SubscriptionItem(subscriptionId, name, desc, cat, pay, cycle, amount, payMet, remind)


                viewModel.updateItem(updatedItem)
                findNavController().navigateUp()
            }
        }

        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            val item = currentItem
            if (item != null) {
                viewModel.deleteItem(item)
                findNavController().navigateUp()
            }
        }
    }
}

/*
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

            // Observe the items list and retrieve the item at the position
            viewModel.items.observe(viewLifecycleOwner, Observer { items ->
                if (items != null && itemPosition in items.indices) {
                    val item = items[itemPosition]
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    view.findViewById<EditText>(R.id.updateName).setText(item.name)
                    view.findViewById<EditText>(R.id.updateDesc).setText(item.desc)
                    view.findViewById<EditText>(R.id.updateCat).setText(item.cat)
                    view.findViewById<EditText>(R.id.updatePay).setText(formatter.format(item.pay))
                    view.findViewById<EditText>(R.id.updateCycle).setText(item.cycle)
                    view.findViewById<EditText>(R.id.updateAmount).setText(String.format(Locale.US, "%.2f", item.amount))
                    view.findViewById<EditText>(R.id.updateCurrency).setText(item.currency)
                    view.findViewById<EditText>(R.id.updatePayMet).setText(item.payMet)
                    view.findViewById<EditText>(R.id.updateRemind).setText(item.remind)

                }
            })
        }
        val buttonUpdate: Button = view.findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.updateName).text.toString()
            val desc = view.findViewById<EditText>(R.id.updateDesc).text.toString()
            val cat = view.findViewById<EditText>(R.id.updateCat).text.toString()

            val payString = view.findViewById<EditText>(R.id.updatePay).text.toString()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val pay = try {
                formatter.parse(payString) ?: Date()
            } catch (e: ParseException) {
                Date()
            }

            val cycle = view.findViewById<EditText>(R.id.updateCycle).text.toString()

            val amountString = view.findViewById<EditText>(R.id.updateAmount).text.toString()
            val amount = amountString.toDoubleOrNull() ?: 0.0

            val currency = view.findViewById<EditText>(R.id.updateCurrency).text.toString()
            val payMet = view.findViewById<EditText>(R.id.updatePayMet).text.toString()
            val remind = view.findViewById<EditText>(R.id.updateRemind).text.toString()

            val updatedItem = SubscriptionItem(subscriptionId, name, desc, cat, pay, cycle, amount, currency, payMet, remind)
            viewModel.updateItem(updatedItem)

            findNavController().navigateUp()
        }

        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            // Use the updated item directly instead of just position
            val item = viewModel.items.value?.get(itemPosition)
            if (item != null) {
                viewModel.deleteItem(item)
                findNavController().navigateUp()
            }
        }
    }
}*/
/*
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
            // Observe the items list and retrieve the item at the position
            viewModel.items.observe(viewLifecycleOwner, Observer { items ->
                if (items != null && itemPosition in items.indices) {
                    val item = items[itemPosition]
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    view.findViewById<EditText>(R.id.updateName).setText(item.name)
                    view.findViewById<EditText>(R.id.updateDesc).setText(item.desc)
                    view.findViewById<EditText>(R.id.updateCat).setText(item.cat)
                    view.findViewById<EditText>(R.id.updatePay).setText(formatter.format(item.pay))
                    view.findViewById<EditText>(R.id.updateCycle).setText(item.cycle)
                    view.findViewById<EditText>(R.id.updateAmount).setText(String.format(Locale.US, "%.2f", item.amount))
                    view.findViewById<EditText>(R.id.updateCurrency).setText(item.currency)
                    view.findViewById<EditText>(R.id.updatePayMet).setText(item.payMet)
                    view.findViewById<EditText>(R.id.updateRemind).setText(item.remind)
                }
            })
        }

        val buttonUpdate: Button = view.findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.updateName).text.toString()
            val desc = view.findViewById<EditText>(R.id.updateDesc).text.toString()
            val cat = view.findViewById<EditText>(R.id.updateCat).text.toString()

            val payString = view.findViewById<EditText>(R.id.updatePay).text.toString()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val pay = try {
                formatter.parse(payString) ?: Date()
            } catch (e: ParseException) {
                Date()
            }

            val cycle = view.findViewById<EditText>(R.id.updateCycle).text.toString()

            val amountString = view.findViewById<EditText>(R.id.updateAmount).text.toString()
            val amount = amountString.toDoubleOrNull() ?: 0.0

            val currency = view.findViewById<EditText>(R.id.updateCurrency).text.toString()
            val payMet = view.findViewById<EditText>(R.id.updatePayMet).text.toString()
            val remind = view.findViewById<EditText>(R.id.updateRemind).text.toString()

            val updatedItem = SubscriptionItem(name, desc, cat, pay, cycle, amount, currency, payMet, remind)
            viewModel.updateItem(updatedItem)

            findNavController().navigateUp()
        }

        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            // Use the updated item directly instead of just position
            val item = viewModel.items.value?.get(itemPosition)
            if (item != null) {
                viewModel.deleteItem(item)
                findNavController().navigateUp()
            }
        }
    }
}
*/