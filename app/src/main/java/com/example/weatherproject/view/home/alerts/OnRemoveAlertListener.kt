package com.example.weatherproject.view.home.alerts

import com.example.weatherproject.model.AlarmData

interface OnRemoveAlertListener {
    fun removeAlert(alert : AlarmData)
}