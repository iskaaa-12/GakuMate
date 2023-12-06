package com.labactivity.gakumate

import TasksSharedPreferencesManager
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.labactivity.gakumate.databinding.EditPopupBinding
import com.labactivity.gakumate.databinding.ListBinding
import com.orhanobut.dialogplus.DialogPlus
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskAdapter(
    private val context: Context,
    private val tasks: ArrayList<Tasks>,
    private val tasksSharedPreferencesManager: TasksSharedPreferencesManager // Add this parameter
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this, tasksSharedPreferencesManager)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
        holder.itemView.setOnClickListener {
            holder.showEditDialog(task, position)
        }
    }

    override fun getItemCount(): Int = tasks.size

    inner class ViewHolder(
        private val binding: ListBinding,
        private val adapter: TaskAdapter,
        private val tasksSharedPreferencesManager: TasksSharedPreferencesManager // Add this parameter
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        fun bind(task: Tasks) {
            binding.txtViewDate.text = dateFormat.format(task.date)
            binding.txtViewTime.text = timeFormat.format(task.time)
            binding.txtViewNotes.text = task.taskText
            binding.imgBtnDelete.setOnClickListener() {
                showDeleteDialog(task)
            }
        }

        fun showEditDialog(task: Tasks, position: Int) {
            val dialogView = EditPopupBinding.inflate(LayoutInflater.from(binding.root.context))
            val datePicker = dialogView.datePicker
            val timePicker = dialogView.timePicker
            val edtTxtEditNotes = dialogView.edtTxtEditNotes
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

            edtTxtEditNotes.setText(task.taskText)

            btnUpdate.setOnClickListener {
                val newDate = getDateFromDatePicker(datePicker)
                val newTime = getTimeFromTimePicker(timePicker)
                val newTaskText = edtTxtEditNotes.text.toString() // Extract text from EditText

                task.date = newDate
                task.time = newTime
                task.taskText = newTaskText // Update the taskText property

                adapter.notifyDataSetChanged()
                notifyItemChanged(position)



                dialogPlus.dismiss()
            }

            dialogPlus.show()
        }

        private fun showDeleteDialog(task: Tasks) {
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
            alertDialogBuilder.setTitle("Delete Task")
            alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                // Log statement to check the task being deleted
                println("Deleting task: $task")

                // Remove the task from the list
                tasks.remove(task)

                // Log statement to check the updated tasks list
                println("Updated tasks list: $tasks")

                // Notify the adapter
                notifyDataSetChanged()

                // Save the updated tasks list to SharedPreferences
                tasksSharedPreferencesManager.saveTasks(tasks)

                Toast.makeText(binding.root.context, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
            }
            alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
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
