package com.example.neweasydairy.fragments.pinFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentPinBinding

class PinFragment : Fragment() {

    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!
    private var isConfirmingPin = false
    private var firstPinEntry: String? = null
    private var savedPin: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Prevent back navigation
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedPin = getPinFromSharedPreferences()
        binding.btnSetPin.text = if (savedPin.isNullOrEmpty()) "Setup PIN" else "Enter PIN"

        binding.edTextOne.requestFocus()
        showSoftKeyboard(binding.edTextOne)

        setupEditTextListeners()

        binding.btnSetPin.setOnClickListener {
            val pin = getEnteredPin()
            Log.e("pin", "Entered PIN: $pin | First Entry: $firstPinEntry")

            if (pin.length == 4) {
                if (savedPin.isNullOrEmpty()) {
                    // First time PIN setup flow
                    Log.e("checkPin", "first time pin")
                    handleFirstTimePinSetup(pin)
                } else {
                    // Existing PIN validation flow
                    Log.e("checkPin", "second time pin")
                    if (pin == savedPin) {
                        navigateToWelcomeScreen()
                    } else {
                        Toast.makeText(context, "Invalid PIN. Please try again.", Toast.LENGTH_SHORT).show()
                        clearPinFields()
                    }
                }
            } else {
                Toast.makeText(context, "Please enter a 4-digit PIN.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleFirstTimePinSetup(pin: String) {
        if (!isConfirmingPin) {
            firstPinEntry = pin
            isConfirmingPin = true
            binding.txtPleaseEnterPassword.text = "Please confirm your PIN"
            clearPinFields()
        } else {
            if (pin == firstPinEntry) {
                savePinToSharedPreferences(pin)
                navigateToWelcomeScreen()
            } else {
                Toast.makeText(context, "PINs do not match. Try again.", Toast.LENGTH_SHORT).show()
                isConfirmingPin = false
                binding.txtPleaseEnterPassword.text = "Please enter your PIN"
                clearPinFields()
            }
        }
    }

    private fun navigateToWelcomeScreen() {
        if (findNavController().currentDestination?.id == R.id.pinFragment) {
            findNavController().navigate(R.id.action_pinFragment_to_welcomeFragment)
        }
    }

    private fun setupEditTextListeners() {
        val editTexts = arrayOf(binding.edTextOne, binding.edTextTwo, binding.edTextThree, binding.edTextFour)

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener {
                if (it?.length == 1 && i < editTexts.size - 1) {
                    editTexts[i + 1].requestFocus()
                } else if (it?.isEmpty() == true && i > 0) {
                    editTexts[i - 1].requestFocus()
                }
            }
        }
    }

    private fun getEnteredPin(): String {
        return binding.edTextOne.text.toString() +
                binding.edTextTwo.text.toString() +
                binding.edTextThree.text.toString() +
                binding.edTextFour.text.toString()
    }

    private fun clearPinFields() {
        binding.apply {
            edTextOne.text?.clear()
            edTextTwo.text?.clear()
            edTextThree.text?.clear()
            edTextFour.text?.clear()
            edTextOne.requestFocus()
            showSoftKeyboard(edTextOne)
        }
    }

    private fun savePinToSharedPreferences(pin: String) {
        val sharedPreferences = context?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("UserPin", pin)?.apply()
    }

    private fun getPinFromSharedPreferences(): String? {
        val sharedPreferences = context?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("UserPin", null)
    }

    private fun showSoftKeyboard(editText: EditText) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
