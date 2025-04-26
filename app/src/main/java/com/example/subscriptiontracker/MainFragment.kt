package com.example.subscriptiontracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment(), SubscriptionAdapter.OnItemClickListener {

    private lateinit var viewModel: SubscriptionViewModel
    private lateinit var adapter: SubscriptionAdapter
    private lateinit var addItemLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SubscriptionViewModel::class.java]

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val emptyView: TextView = view.findViewById(R.id.emptyView)
        val createTaskButton: FloatingActionButton = view.findViewById(R.id.createTaskButton)

        adapter = SubscriptionAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter = SubscriptionAdapter(items, this)
            recyclerView.adapter = adapter
            emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }

        // Register ActivityResultLauncher
        addItemLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val name = data.getStringExtra("NEW_ITEM_NAME") ?: ""
                    val desc = data.getStringExtra("NEW_ITEM_DESC") ?: ""
                    val cat = data.getStringExtra("NEW_ITEM_CAT") ?: ""
                    val pay = data.getStringExtra("NEW_ITEM_PAY") ?: ""
                    val cycle = data.getStringExtra("NEW_ITEM_CYCLE") ?: ""
                    val amount = data.getStringExtra("NEW_ITEM_AMOUNT") ?: ""
                    val currency = data.getStringExtra("NEW_ITEM_CURRENCY") ?: ""
                    val payMet = data.getStringExtra("NEW_ITEM_PAYMET") ?: ""
                    val remind = data.getStringExtra("NEW_ITEM_REMIND") ?: ""

                    val newItem = SubscriptionItem(name, desc, cat, pay, cycle, amount, currency, payMet, remind)
                    viewModel.addItem(newItem)
                }
            }
        }

        createTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_itemInputFragment)
        }

    }
    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt("itemPosition", position)
        findNavController().navigate(R.id.action_mainFragment_to_itemUpdateFragment, bundle)
    }

}


