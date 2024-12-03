package com.example.vitesse.fragments

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.example.vitesse.R
import com.example.vitesse.data.viewModel.SharedViewModel
import com.example.vitesse.utils.CandidateAction
import com.example.vitesse.data.viewModel.LocalCandidatViewModel
import com.example.vitesse.databinding.FragmentEditCandidateBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

class EditCandidateFragment : Fragment() {

    private var _binding: FragmentEditCandidateBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: LocalCandidatViewModel by activityViewModels()

    // Handle image selection
    private var selectedImageUri: Uri? = null
    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.imageCandidat.setImageURI(uri)
        }
    }

    // Handle permission request for storage access
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openFilePicker() // Proceed with file picker if permission is granted
        } else {
            Toast.makeText(requireContext(), "Permission required to access files", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditCandidateBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Handles a candidate's picture path to a valid Uri
    private fun getImageUri(picturePath: String?): Uri? {
        return picturePath?.let {
            when {
                it.startsWith("http://") || it.startsWith("https://") -> {
                    Uri.parse(it)
                }
                it.startsWith("content://") -> {
                    // content URI
                    try {
                        Uri.parse(it)
                    } catch (e: Exception) {
                        null
                    }
                }
                else -> {
                    // local file path
                    try {
                        Uri.fromFile(File(it))
                    } catch (e: Exception) {
                        null
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.candidateFlow.onEach { candidats ->
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
        sharedViewModel.selectedCandidat.collect { candidat ->
            candidat?.let {
                // Fill in the fields with the selected candidate's information
                binding.outlinedTextField.editText?.setText(it.firstName)
                binding.outlinedTextField2.editText?.setText(it.lastName)
                binding.outlinedTextField3.editText?.setText(it.phone)
                binding.outlinedTextField4.editText?.setText(it.email)
                binding.outlinedTextField5.editText?.setText(it.description)
                binding.outlinedTextField11.editText?.setText(it.note)

                binding.outlinedDate.editText?.setText(it.anivDat)

                // Set the image if available, else use default image
                val imageUri = getImageUri(it.picture)
                Glide.with(this@EditCandidateFragment)
                    .load(imageUri ?: R.drawable.personicon) // Fallback if URI is null
                    .placeholder(R.drawable.personicon) // Show placeholder while loading
                    .error(R.drawable.personicon) // Fallback if the loading fails
                    .into(binding.imageCandidat)
            }
        }
    }

        // Set up the toolbar with back navigation
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }

        // Set up the calendar icon click listener
        binding.calendarIcon.setOnClickListener {
            showDatePickerDialog()
        }

        // Set click listener on image placeholder to open the gallery
        binding.imageCandidat.setOnClickListener {
            openFilePicker()
        }

        // Set up listeners for buttons and other interactive views
        binding.buttonUpdate.setOnClickListener {
            val updatedCandidat = sharedViewModel.selectedCandidat.value?.copy(
                firstName = binding.outlinedTextField.editText?.text.toString(),
                lastName = binding.outlinedTextField2.editText?.text.toString(),
                phone = binding.outlinedTextField3.editText?.text.toString(),
                email = binding.outlinedTextField4.editText?.text.toString(),
                description = binding.outlinedTextField5.editText?.text.toString(),
                anivDat = binding.outlinedDate.editText?.text.toString(),
                note = binding.outlinedTextField11.editText?.text.toString(),
                picture = selectedImageUri?.toString() ?: "https://xsgames.co/randomusers/assets/avatars/female/36.jpg"
            )

            if (updatedCandidat != null) {
                lifecycleScope.launch {
                    viewModel.performAction(CandidateAction.UpdateCandidate(updatedCandidat))
                    Toast.makeText(requireContext(), "Candidate updated", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homePageFragment)
                }
            }
        }
    }

    private fun openFilePicker() {
        // Check if permission is granted to read storage
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
            == PermissionChecker.PERMISSION_GRANTED
        ) {
            // Launch file picker for any type of file
            pickFileLauncher.launch("*/*")
        } else {
            // Request permission if not granted
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
