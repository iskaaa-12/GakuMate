package com.labactivity.gakumate

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.recyclerview.widget.RecyclerView
import com.labactivity.gakumate.databinding.EditPopupBinding
import com.labactivity.gakumate.databinding.ListBinding
import com.orhanobut.dialogplus.DialogPlus
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CategoryAdapter(private val tasks: ArrayList<Tasks>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
        holder.itemView.setOnClickListener {
            holder.showEditDialog(task, position)
        }

    }

    override fun getItemCount(): Int = tasks.size

    inner class ViewHolder(private val binding: ListBinding, private val adapter: CategoryAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        fun bind(task: Tasks) {
            binding.txtViewDate.text = dateFormat.format(task.date)
            binding.txtViewTime.text = timeFormat.format(task.time)
            binding.txtViewNotes.text = task.tasks
        }

        fun showEditDialog(task: Tasks, position: Int) {
            val dialogView =
                EditPopupBinding.inflate(LayoutInflater.from(binding.root.context))
            val datePicker = dialogView.datePicker
            val timePicker = dialogView.timePicker
            val edtTxtAddNotes = dialogView.edtTxtAddNotes
            val btnUpdate = dialogView.btnUpdate

            val dialogPlus = DialogPlus.newDialog(binding.root.context)
                .setContentHolder(com.orhanobut.dialogplus.ViewHolder(dialogView.root))
                .setCancelable(true)
                .setExpanded(true, 2000)
                .create()

            val calendar = Calendar.getInstance()
            calendar.time = task.date

            datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
            )

            val time = task.time
            timePicker.hour = time.hours
            timePicker.minute = time.minutes

            edtTxtAddNotes.setText(task.tasks)

            btnUpdate.setOnClickListener {
                val newDate = getDateFromDatePicker(datePicker)
                val newTime = getTimeFromTimePicker(timePicker)
                val newTaskText = edtTxtAddNotes.text.toString()

                task.date = newDate
                task.time = newTime
                task.tasks = newTaskText

                adapter.notifyDataSetChanged()
                notifyItemChanged(position)

                dialogPlus.dismiss()

            }

            dialogPlus.show()
        }

        private fun getDateFromDatePicker(datePicker: DatePicker): Date {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            return calendar.time
        }

        private fun getTimeFromTimePicker(timePicker: TimePicker): Time {
            val hour = timePicker.hour
            val minute = timePicker.minute

            val calendar = Calendar.getInstance()
            calendar.set(0, 0, 0, hour, minute)

            return Time(calendar.timeInMillis)
        }
    }
}