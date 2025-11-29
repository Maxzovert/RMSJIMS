package com.example.rmsjims.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rmsjims.data.model.BookingDates
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel() : ViewModel() {

    val bookedDate = LocalDate.now().plusDays(10)
    val availableDate = LocalDate.now().plusDays(10)
    val bookOnDate = LocalDate.now().plusDays(10)

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    var selectedStartDate by mutableStateOf<LocalDate?>(null)
        private set

    var selectedEndDate by mutableStateOf<LocalDate?>(null)
        private set

    // Keep selectedDate for backward compatibility (used for current month display)
    var selectedDate by mutableStateOf(today)
        private set

    var currentMonth by mutableStateOf(YearMonth.now())
        private set

    val daysOfWeek: List<DayOfWeek> = DayOfWeek.values().toList()

    fun isDateInPast(date: LocalDate): Boolean {
        return date.isBefore(today)
    }

    fun isDateBooked(date: LocalDate): Boolean {
        // Check if date matches bookedDate (for now, can be extended to check against list)
        return date == bookedDate
    }

    fun isDateHoliday(date: LocalDate): Boolean {
        return date.dayOfWeek == DayOfWeek.SUNDAY
    }

    fun selectDate(date: LocalDate) {
        // Prevent selecting past dates
        if (isDateInPast(date)) {
            return
        }

        when {
            // If no start date selected, set it
            selectedStartDate == null -> {
                selectedStartDate = date
                selectedEndDate = null
            }
            // If start date is selected and end date is null, set end date
            selectedEndDate == null -> {
                if (date.isBefore(selectedStartDate!!)) {
                    // If selected date is before start, make it the new start
                    selectedEndDate = selectedStartDate
                    selectedStartDate = date
                } else {
                    selectedEndDate = date
                }
            }
            // If both are selected, start a new selection
            else -> {
                selectedStartDate = date
                selectedEndDate = null
            }
        }
        selectedDate = date
    }

    fun isDateInRange(date: LocalDate): Boolean {
        return selectedStartDate != null && selectedEndDate != null &&
                !date.isBefore(selectedStartDate!!) && !date.isAfter(selectedEndDate!!)
    }

    fun isDateRangeStart(date: LocalDate): Boolean {
        return date == selectedStartDate
    }

    fun isDateRangeEnd(date: LocalDate): Boolean {
        return date == selectedEndDate
    }

    fun hasCompleteRange(): Boolean {
        return selectedStartDate != null && selectedEndDate != null
    }

    fun confirmSelection(): BookingDates? {
        return if (hasCompleteRange()) {
            BookingDates(
                fromDate = selectedStartDate!!.toString(),
                toDate = selectedEndDate!!.toString()
            )
        } else {
            null
        }
    }

    fun resetSelection() {
        selectedStartDate = null
        selectedEndDate = null
    }

    fun setMonth(month: YearMonth) {
        currentMonth = month
    }

    fun getMonthDates(): List<LocalDate> {
        val firstDayOfMonth = currentMonth.atDay(1)
        val lastDayOfMonth = currentMonth.atEndOfMonth()

        // How many days to show from previous month (0 = Sunday)
        val startOffset = firstDayOfMonth.dayOfWeek.value % 7 // Sunday=0, Monday=1 ...
        val dates = mutableListOf<LocalDate>()

        // Add previous month trailing days
        for (i in startOffset downTo 1) {
            dates.add(firstDayOfMonth.minusDays(i.toLong()))
        }

        // Add current month days
        for (day in 1..lastDayOfMonth.dayOfMonth) {
            dates.add(currentMonth.atDay(day))
        }

        // Add next month leading days to fill the last week
        val endOffset = (7 - (dates.size % 7)) % 7
        for (i in 1..endOffset) {
            dates.add(lastDayOfMonth.plusDays(i.toLong()))
        }

        return dates
    }


    fun getMonths(): List<YearMonth> {
        val now = YearMonth.now()
        return (0..11).map { now.plusMonths(it.toLong()) }
    }

    fun getHolidayCount(): Int {
        return getMonthDates().count { it.dayOfWeek == DayOfWeek.SUNDAY }
    }
}

