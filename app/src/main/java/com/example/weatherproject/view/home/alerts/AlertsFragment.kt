package com.example.weatherproject.view.home.alerts

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentAlertsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AlertsFragment : Fragment() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    lateinit var binding: FragmentAlertsBinding
    private lateinit var btnFrom: Button
    private lateinit var btnTo: Button
    private lateinit var txtFromDate: TextView
    private lateinit var txtFromTime: TextView
    private lateinit var txtToDate: TextView
    private lateinit var txtToTime: TextView
    private lateinit var btnSave: Button
    private lateinit var rbAlarm: RadioButton
    private lateinit var rbNotification: RadioButton

    private var fromCalendar = Calendar.getInstance()
    private var toCalendar = Calendar.getInstance()

    lateinit var fab : FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        fab = binding.fab
        fab.setOnClickListener {
           showCustomDialog()
        }
    }
    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alarm_custom_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        btnFrom = dialogView.findViewById(R.id.btnFrom)
        btnTo = dialogView.findViewById(R.id.btnTo)
        txtFromDate = dialogView.findViewById(R.id.txtFrom_date)
        txtFromTime = dialogView.findViewById(R.id.txtFrom_time)
        txtToDate = dialogView.findViewById(R.id.txt_to_date)
        txtToTime = dialogView.findViewById(R.id.txt_to_time)
        btnSave = dialogView.findViewById(R.id.btnSave)
        rbAlarm = dialogView.findViewById(R.id.rbAlarm)
        rbNotification = dialogView.findViewById(R.id.rbNotification)


        // Date and Time pickers for 'From'
        btnFrom.setOnClickListener {
            pickDateTime(fromCalendar) { updateText(txtFromDate,txtFromTime, fromCalendar) }
        }

        // Date and Time pickers for 'To'
        btnTo.setOnClickListener {
            pickDateTime(toCalendar) { updateText(txtToDate,txtToTime, toCalendar) }
        }

        // Save button click listener
        btnSave.setOnClickListener {
            val isAlarmSelected = rbAlarm.isChecked
            val isNotificationSelected = rbNotification.isChecked
            // Handle saving logic here

            dialog.dismiss() // Close dialog after saving
        }
    }

    private fun pickDateTime(calendar: Calendar, onDateTimeSet: () -> Unit) {
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Time picker
        TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            // Date picker
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                onDateTimeSet()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }, currentHour, currentMinute, false).show()
    }

    private fun updateText(textViewDate: TextView,textViewTime: TextView ,calendar: Calendar) {
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

        textViewTime.text = timeFormat.format(calendar.time)
        textViewDate.text = dateFormat.format(calendar.time)

    }
}

