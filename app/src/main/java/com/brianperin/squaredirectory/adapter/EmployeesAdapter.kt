package com.brianperin.squaredirectory.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brianperin.squaredirectory.R
import com.brianperin.squaredirectory.`interface`.EmployeeClickListener
import com.brianperin.squaredirectory.model.response.Employee
import com.brianperin.squaredirectory.util.toDisplay
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


/**
 * Standard recycler view
 */
class EmployeesAdapter : RecyclerView.Adapter<EmployeesAdapter.ViewHolder>() {
    private var context: Context? = null
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
     * Get context here never pass context into an adapter
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        context = null
    }

    /**
     * Custom click listener
     */
    fun setClickListener(employeeClickListener: EmployeeClickListener) {
        this.itemClickListener = employeeClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val employeeView = LayoutInflater.from(parent.context).inflate(R.layout.list_employee, parent, false)
        return ViewHolder(employeeView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val employee = employees[position]

        context?.let {
            Glide.with(it)
                .load(employee.photoSmall)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(holder.thumbnail);
        }

        holder.name.text = employee.fullName
        holder.email.text = employee.emailAddress

        //nullables
        employee.formattedNumber?.let {
            holder.phone.text = employee.formattedNumber
            holder.phone.visibility = View.VISIBLE
        } ?: kotlin.run {
            holder.phone.visibility = View.GONE
        }
        employee.biography?.let {
            holder.bio.text = employee.biography
            holder.phone.visibility = View.VISIBLE
        } ?: run {
            holder.phone.visibility = View.GONE
        }

        holder.team.text = employee.team + " " + employee.employeeType.toDisplay()

        holder.itemView.setOnClickListener { view ->
            itemClickListener.onClick(employee, position, view)
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
        var bio: TextView = itemView.findViewById<TextView>(R.id.tvListBio)
    }
}