import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.example.easydiaryandjournalwithlock.databinding.ReminderTextDialogBinding
import com.example.neweasydairy.fragments.reminderFragment.ReminderViewModel

class ReminderDialog(
    activity: Activity,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: ReminderViewModel,
    private val onSaveClicked: (String, String) -> Unit
) : Dialog(activity) {
    private val inflater = activity.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE
    ) as LayoutInflater
    private val binding = ReminderTextDialogBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        setCancelable(true)
        setCanceledOnTouchOutside(true)

        // Observe LiveData from ViewModel
        observeViewModel()

        binding.apply {
            btnSave.setOnClickListener {
                val title = edReminderTitle.text.toString().trim()
                val description = edReminderDescription.text.toString().trim()
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    onSaveClicked(title, description)
                    dismiss()
                } else {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.title.observe(lifecycleOwner) { updatedTitle ->
            binding.edReminderTitle.setText(updatedTitle)
        }

        viewModel.description.observe(lifecycleOwner) { updatedDescription ->
            binding.edReminderDescription.setText(updatedDescription)
        }
    }
}
