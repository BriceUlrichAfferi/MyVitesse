package com.example.vitesse.fragments

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vitesse.R
import com.example.vitesse.utils.CandidateAction
import com.example.vitesse.data.viewModel.LocalCandidatViewModel
import com.example.vitesse.databinding.FragmentAddBinding
import com.example.vitesse.model.Candidate
import com.example.vitesse.utils.ValidationUtil
import kotlinx.coroutines.launch
import java.util.Calendar

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocalCandidatViewModel by activityViewModels()

    // To handle image selection
    private var selectedImageUri: Uri? = null

    // Initialize ActivityResultLauncher for file picker
    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.imageCandidat.setImageURI(uri) // Display selected image
        }
    }

    // Handle permission request for storage access
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openFilePicker() // Proceed with file picker if permission is granted
        } else {
            Toast.makeText(requireContext(), getString(R.string.permission_access_files), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the Toolbar as the ActionBar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set up navigation click listener for the toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.homePageFragment) // Navigate back
        }

        // Accessing the views using binding
        binding.textAPropos1.text = getString(R.string.sélect_date)
        binding.textAPropos2.text = getString(R.string.enter_date)

        // Set up the calendar icon click listener
        binding.calendarIcon.setOnClickListener {
            showDatePickerDialog()
        }

        // Set click listener on image placeholder to open the gallery
        binding.imageCandidat.setOnClickListener {
            openFilePicker()
        }

        // Set up listeners for buttons or other interactive views
        binding.buttonAjout.setOnClickListener {
            saveCandidat()
        }
    }

    private fun openFilePicker() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
            == PermissionChecker.PERMISSION_GRANTED
        ) {
            pickFileLauncher.launch("*/*")
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.outlinedDate.editText?.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun saveCandidat() {
        val firstName = binding.outlinedTextField.editText?.text.toString()
        val lastName = binding.outlinedTextField2.editText?.text.toString()
        val identifiant = binding.outlinedTextField11.editText?.text.toString()
        val description = binding.outlinedTextField5.editText?.text.toString()
        val phone = binding.outlinedTextField3.editText?.text.toString()
        val email = binding.outlinedTextField4.editText?.text.toString()
        val anivDate = binding.outlinedDate.editText?.text.toString()
        val imageUriString = selectedImageUri?.toString() ?: "https://xsgames.co/randomusers/assets/avatars/female/36.jpg"

        var isValid = true

        // Validate form fields using ValidationUtil
        binding.outlinedTextField.error = ValidationUtil.validateFirstName(firstName)
        if (binding.outlinedTextField.error != null) isValid = false

        binding.outlinedTextField2.error = ValidationUtil.validateLastName(lastName)
        if (binding.outlinedTextField2.error != null) isValid = false

        binding.outlinedTextField5.error = ValidationUtil.validateDescription(description)
        if (binding.outlinedTextField5.error != null) isValid = false

        binding.outlinedTextField3.error = ValidationUtil.validatePhone(phone)
        if (binding.outlinedTextField3.error != null) isValid = false

        binding.outlinedDate.error = ValidationUtil.validateDate(anivDate)
        if (binding.outlinedDate.error != null) isValid = false

        binding.outlinedTextField4.error = ValidationUtil.validateEmail(email)
        if (binding.outlinedTextField4.error != null) isValid = false

        binding.outlinedTextField11.error = ValidationUtil.validateNote(identifiant)
        if (binding.outlinedTextField.error != null) isValid = false

        // Proceed if all fields are valid
        if (isValid) {
            val candidate = Candidate(
                picture = imageUriString,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                email = email,
                description = description,
                note = identifiant,
                anivDat = anivDate,
                isFavorite = false // Ensure isFavorite is set to false when adding
            )

            lifecycleScope.launch {
                try {
                    viewModel.performAction(CandidateAction.AddCandidate(candidate))
                    showToast(getString(R.string.added_canidate_success))
                    findNavController().navigate(R.id.homePageFragment)
                } catch (e: Exception) {
                    showToast(getString(R.string.error_saving_canidate))
                }
            }
        } else {
            showToast(getString(R.string.all_field_vailidity))

        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}