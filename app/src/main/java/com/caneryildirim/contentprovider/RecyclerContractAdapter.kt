package com.caneryildirim.contentprovider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caneryildirim.contentprovider.databinding.RecyclerContractRowBinding

class RecyclerContractAdapter(private val contractList:ArrayList<Users>):RecyclerView.Adapter<RecyclerContractAdapter.ContractHolder>() {
    class ContractHolder(val binding:RecyclerContractRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractHolder {
        val binding=RecyclerContractRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ContractHolder(binding)
    }

    override fun getItemCount(): Int {
        return contractList.size
    }

    override fun onBindViewHolder(holder: ContractHolder, position: Int) {
        holder.binding.textViewUserName.text=contractList[position].username
        holder.binding.textViewPhoneNumber.text=contractList[position].phoneNumber
    }
}