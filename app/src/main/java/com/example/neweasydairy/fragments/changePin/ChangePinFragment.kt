package com.example.neweasydairy.fragments.changePin

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.databinding.FragmentChangePinBinding

class ChangePinFragment : Fragment() {

    private var _binding: FragmentChangePinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePinBinding.inflate(inflater, container, false)

        setupEditTextNavigation()
        setupDoneButton()

        return binding.root
    }

    private fun setupEditTextNavigation() {
        val editTexts = arrayOf(
            binding.edTextOne, binding.edTextTwo, binding.edTextThree, binding.edTextFour,
            binding.edTextOneNew, binding.edTextTwoNew, binding.edTextThreeNew, binding.edTextFourNew
        )

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    }
                }
            })
        }
    }

    private fun setupDoneButton() {
        binding.btnDone.setOnClickListener {
            val oldPin = getPinFromEditTexts(
                binding.edTextOne, binding.edTextTwo, binding.edTextThree, binding.edTextFour
            )
            val newPin = getPinFromEditTexts(
                binding.edTextOneNew, binding.edTextTwoNew, binding.edTextThreeNew, binding.edTextFourNew
            )
            val savedPin = getPinFromSharedPreferences()

            if (oldPin == savedPin) {
                if (newPin.length == 4) {
                    savePinToSharedPreferences(newPin)
                    Toast.makeText(requireContext(), "PIN Changed Successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(), "New PIN must be 4 digits.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Old PIN is incorrect.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.icBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getPinFromEditTexts(vararg editTexts: androidx.appcompat.widget.AppCompatEditText): String {
        val pinBuilder = StringBuilder()
        for (editText in editTexts) {
            pinBuilder.append(editText.text.toString())
        }
        return pinBuilder.toString()
    }

    private fun savePinToSharedPreferences(pin: String) {
        val sharedPreferences = context?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("UserPin", pin)?.apply()
    }

    private fun getPinFromSharedPreferences(): String? {
        val sharedPreferences = context?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("UserPin", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
