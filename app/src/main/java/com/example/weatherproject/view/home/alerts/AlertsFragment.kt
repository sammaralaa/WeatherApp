package com.example.weatherproject.view.home.alerts

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherproject.Permission
import com.example.weatherproject.Permission.requestNotificationPermission
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentAlertsBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.ApiState
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.view.favorite.WeatherFavAdapter
import com.example.weatherproject.view_model.alerts.AlertsViewModel
import com.example.weatherproject.view_model.alerts.AlertsViewModelFactory
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import com.example.weatherproject.view_model.favorite.FavFragmentViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class AlertsFragment : Fragment(),OnRemoveAlertListener {
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

    lateinit var AViewModel : AlertsViewModel
    lateinit var AFactory : AlertsViewModelFactory
    lateinit var alertAdapter: AlertsAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    lateinit var recyclerView: RecyclerView
    private val REQUEST_CODE_OVERLAY_PERMISSION = 500
    private var fromCalendar = Calendar.getInstance()
    private var toCalendar = Calendar.getInstance()

    lateinit var fab : FloatingActionButton
    lateinit var messageFromRemote : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AFactory = AlertsViewModelFactory(
            WeatherRepository.getInstance(
                WeatherRemoteDataSource(RetrofitHelper.service),
                WeatherLocalDataSource(
                    WeatherDataBase.getInstance(requireContext()).getWeatherDao(),
                    WeatherDataBase.getInstance(requireContext()).getAlertDao()),
                SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
            ))
        AViewModel = ViewModelProvider(this, AFactory).get(AlertsViewModel::class.java)
//        var alert = AlarmData(time="12:32",date = "21-3-2002", workerId = "34y4y")
//        AViewModel.insertAlert(alert)
        AViewModel.getAllAlerts()
        recyclerView = binding.alertRecycler
        alertAdapter = AlertsAdapter(this)
        mLayoutManager = GridLayoutManager(requireContext(),1)
        recyclerView.apply {
            adapter = alertAdapter
            layoutManager = mLayoutManager
        }
        AViewModel.AlertItem.observe(viewLifecycleOwner , Observer { weather ->
            alertAdapter.submitList(weather)
        })
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


        getMessageFromRemote(AViewModel.getDataFromSharedPref().first,AViewModel.getDataFromSharedPref().second,AViewModel.getStringFromSharedPref("lang")?:"en",AViewModel.getStringFromSharedPref("units")?:"metric").toString()


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
            var type : String = ""
            val isAlarmSelected = rbAlarm.isChecked
            val isNotificationSelected = rbNotification.isChecked
            if(isAlarmSelected){
                type = "a"
            } else {
                type = "n"
            }
            if (type == "a") {
                if (!Settings.canDrawOverlays(requireContext())) {
                    requestOverlayPermission()
                } else {
                    saveAlert(type)
                    dialog.dismiss()
                }
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestNotificationPermission(requireActivity())
                } else {
                    saveAlert(type)
                    dialog.dismiss()
                }
            }


             // Close dialog after saving
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

    override fun removeAlert(alert: AlarmData) {
        AViewModel.deleteAlert(alert)
        alertAdapter.notifyDataSetChanged()
    }

    // to schedule and get work id as string
    fun scheduleNotification(context: Context, alarmTime: Long,alarm: AlarmData): String {
        val currentTime = System.currentTimeMillis()
        val delay = alarmTime - currentTime

        val data = Data.Builder() // to pass data to worker
            .putString("id",alarm.workerId) //need fix
            .putString("message", alarm.message)
            .putString("type", alarm.type)
            .build()
        val notificationWork = OneTimeWorkRequestBuilder<AlertWorker>() // schedule
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(notificationWork) //enqueue

        return notificationWork.id.toString() //workid
    }
    @SuppressLint("NewApi")
    fun getMessageFromRemote(lat: Double, lon: Double,lang : String,unit:String){
        var message : String? = null
        lifecycleScope.launch {

            AViewModel.getCurrentWeather(lat,lon,lang,unit)
            Log.i("TAG", "getCurrentWeather: frament ")

            AViewModel.weatherStateFlow.collectLatest { state ->
                when (state) {
                    is ApiState.Loading -> {
                        Log.i("TAG", "Loading products...")
                    }
                    is ApiState.Success -> {
                        message = state.data.weather[0].description
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load weather, please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TAG", "Error loading weather: ${state.msg}")
                    }

                }
            }
        }

    }
    fun saveAlert(type :String){
        lifecycleScope.launch {
            AViewModel.weatherStateFlow.collectLatest { state->
                when (state) {
                    is ApiState.Loading -> {
                    }
                    is ApiState.Success -> {
                        messageFromRemote = state.data.weather[0].description
                    }
                    is ApiState.Failure -> {
                    }

                }
            }}

        // Handle saving logic here
        val newAlert = AlarmData(0,fromCalendar.timeInMillis,fromCalendar.timeInMillis,type,messageFromRemote," ")
        newAlert.workerId = scheduleNotification(requireContext(),fromCalendar.timeInMillis,newAlert)
        AViewModel.insertAlert(newAlert)
    }

    private fun requestOverlayPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireContext().packageName}"))
        startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
    }
}

