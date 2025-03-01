package com.example.neweasydairy.fragments.calendarFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentCalendarBinding
import com.example.neweasydairy.fragments.homeFragment.HomeAdapter
import com.example.neweasydairy.fragments.homeFragment.HomeViewModel
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.CLICKEDITEMDATA
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.gone
import com.example.neweasydairy.utilis.visible
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

/*@AndroidEntryPoint
class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding
    private lateinit var homeAdapter: HomeAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeAdapter = HomeAdapter(emptyList(), onItemClick = { note ->
            val bundle = Bundle()
            bundle.putString(CHECK_NAVIGATION, FROM_HOME_FRAGMENT)
            bundle.putParcelable(CLICKEDITEMDATA, note)
            findNavController().navigate(R.id.createNotesFragment, bundle)
            Log.e("CheckItem", "Clicked Item id ${note}")
        }, onItemLongClick = {}
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            calendarRecyclerView.adapter = homeAdapter

            val currentDate = getCurrentDate()
            loadNotesForDate(currentDate)
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth, 0, 0, 0)
                val selectedDate = calendar.timeInMillis
                loadNotesForDate(selectedDate)
            }

        }
    }

    private fun getCurrentDate(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }


    private fun loadNotesForDate(date: Long) {
        val (startOfDay, endOfDay) = calculateDayBounds(date)
        homeViewModel.getNotesForDate(startOfDay, endOfDay).observe(viewLifecycleOwner) { notes ->
            Log.e("loadNotesForDate", "Notes for $date: ${notes.size}")
            binding?.apply {
                if (notes.isNotEmpty()) {
                    txtNoData.visibility = View.GONE
                    calendarRecyclerView.visibility = View.VISIBLE
                    homeAdapter.updateList(notes)
                } else {
                    txtNoData.visibility = View.VISIBLE
                    calendarRecyclerView.visibility = View.GONE
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateDayBounds(date: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }

}*/

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val markedDates = HashSet<Long>() // Dates with notes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeAdapter = HomeAdapter(emptyList(), onItemClick = { note ->
            val bundle = Bundle()
            bundle.putString(CHECK_NAVIGATION, FROM_HOME_FRAGMENT)
            bundle.putParcelable(CLICKEDITEMDATA, note)
            findNavController().navigate(R.id.createNotesFragment, bundle)
            Log.e("CheckItem", "Clicked Item id ${note}")
        }, onItemLongClick = {})

        observeMarkedDates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            calendarRecyclerView.adapter = homeAdapter
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val selectedDate = getDateInMillis(year, month, dayOfMonth)
                loadNotesForDate(selectedDate)
            }
        }

        loadNotesForDate(getCurrentDate())
    }

    private fun observeMarkedDates() {
        homeViewModel.allNotes.observe(this) { notes ->
            markedDates.clear()
            notes.forEach { note ->
                val date = getDateInMillisFromTimestamp(note.timestamp)
                markedDates.add(date)
            }
            binding.calendarView.invalidate() // Redraw calendar
        }
    }

    private fun loadNotesForDate(date: Long) {
        val (startOfDay, endOfDay) = calculateDayBounds(date)
        homeViewModel.getNotesForDate(startOfDay, endOfDay).observe(viewLifecycleOwner) { notes ->
            binding.apply {
                if (notes.isNotEmpty()) {
                    txtNoData.gone()
                    calendarRecyclerView.visible()
                    homeAdapter.updateList(notes)
                } else {
                    txtNoData.visible()
                    calendarRecyclerView.gone()
                }
            }
        }
    }

    private fun getCurrentDate(): Long {
        return Calendar.getInstance().timeInMillis
    }

    private fun getDateInMillis(year: Int, month: Int, day: Int): Long {
        return Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
        }.timeInMillis
    }

    private fun getDateInMillisFromTimestamp(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        return getDateInMillis(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    private fun calculateDayBounds(date: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

