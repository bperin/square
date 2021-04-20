package com.brianperin.squaredirectory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.brianperin.squaredirectory.R
import com.brianperin.squaredirectory.`interface`.EmployeeClickListener
import com.brianperin.squaredirectory.model.response.Employee


/**
 * Standard recycler view
 */
class EmployeesAdapter(context: Fragment) : RecyclerView.Adapter<EmployeesAdapter.ViewHolder>() {

    private var employees = listOf<Employee>() //avoid concurrent manipulation
    private lateinit var itemClickListener: EmployeeClickListener

    /**
     * Use an immutable list to avoid concurrency problems
     */
    fun setEmployees(employees: List<Employee>) {
        this.employees = emptyList()
        this.employees = employees
        notifyDataSetChanged()
    }

    fun clearList() {
        this.employees = emptyList()
        notifyDataSetChanged()
    }

    /**
     * Custom click listener
     */
    fun setListener(employeeClickListener: EmployeeClickListener) {
        this.itemClickListener = employeeClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val employeeView = LayoutInflater.from(parent.context).inflate(R.layout.list_employee, parent, false)
        return ViewHolder(employeeView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val employee = employees[position]

        val listener = itemClickListener

        holder.itemView.setOnClickListener {
            listener.onClick(employee, position, it)
        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    /**
     * View holder for our list item data
     */
    inner class ViewHolder(employeeView: View) : RecyclerView.ViewHolder(employeeView) {
        var thumbnail: ImageView = itemView.findViewById<ImageView>(R.id.ivListThumbnail)
        var name: TextView = itemView.findViewById<TextView>(R.id.tvListName)
        var email: TextView = itemView.findViewById<TextView>(R.id.tvListEmail)
        var phone: TextView = itemView.findViewById<TextView>(R.id.tvListPhone)
        var team: TextView = itemView.findViewById<TextView>(R.id.tvListTeam)
        var type: TextView = itemView.findViewById<TextView>(R.id.tvListEmployeeType)
        var bio: TextView = itemView.findViewById<TextView>(R.id.tvListTeam)
    }
}