package com.example.neweasydairy.fragments.reminderFragment

import ReminderDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.easydiaryandjournalwithlock.databinding.FragmentReminderBinding
import com.example.neweasydairy.alarm.AlarmSchedulerImpl
import com.example.neweasydairy.data.ReminderDao
import com.example.neweasydairy.utilis.toast
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import java.util.Calendar

@AndroidEntryPoint
class ReminderFragment : Fragment() {
    private var _binding: FragmentReminderBinding? = null
    val binding get() = _binding
    lateinit var requestNotificationPermission: ActivityResultLauncher<String>
    private val reminderViewModel: ReminderViewModel by activityViewModels()
    lateinit var calendar: Calendar
    var reminderTextDialogBinding: ReminderDialog? = null
    private var adapter: ReminderAdapter? = null
    private lateinit var alarmSchedulerImpl: AlarmSchedulerImpl


    var title = "Title"
    var description = "Description"

    @Inject
    lateinit var reminderDao: ReminderDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendar = Calendar.getInstance()
        requestNotificationPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    setReminder(
                        this,
                        activity ?: return@registerForActivityResult,
                        reminderDao,
                        alarmSchedulerImpl
                    )
                } else {
                    activity?.toast("Notification permission is required to set reminders.")
                }
            }
        alarmSchedulerImpl = AlarmSchedulerImpl(context?.applicationContext ?: return)
        reminderTextDialogBinding = ReminderDialog(
            activity = activity ?: return,
            lifecycleOwner = this,
            viewModel = reminderViewModel,
            onSaveClicked = { dialogTitle, dialogDescription ->
                title = dialogTitle
                description = dialogDescription
                reminderViewModel.setReminderData(title, description)
                binding?.txtAuto?.text = description
                Log.e("Value", "onCreate: title $title :::: description:$description")
            })

        adapter =
            ReminderAdapter(list = emptyList(), context = context ?: return, onItemClick = { pair ->

                adapter?.removeItem(pair)
                reminderViewModel.deleteReminderById(tagId = pair.id)
                //   val reminderId = pair.first
                //    val action = pair.second
//                when (action) {
//                    DELETE_ACTION_REMINDER -> {
//                        reminderViewModel.deleteReminderById(tagId = reminderId.id)
//                    }
//                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            reminderRecyclerView.adapter = adapter
            reminderViewModel.description.observe(viewLifecycleOwner) { description ->
                txtAuto.text = description ?: "Auto"
            }
            clickListener(activity ?: return, this@ReminderFragment, reminderDao,alarmSchedulerImpl)
            observeViewModel()
        }
    }


    private fun observeViewModel() {
        reminderViewModel.allReminders.observe(viewLifecycleOwner) { reminders ->
            binding?.apply {
                if (reminders.isNotEmpty()) {
                    adapter?.updateReminderList(reminders)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}