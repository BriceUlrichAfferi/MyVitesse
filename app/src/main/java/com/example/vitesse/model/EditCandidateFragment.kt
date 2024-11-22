package com.example.vitesse.model

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
import com.bumptech.glide.Glide
import com.example.vitesse.R
import com.example.vitesse.SharedViewModel
import com.example.vitesse.adapters.room.AppDatabase
import com.example.vitesse.candidats.CandidateAction
import com.example.vitesse.data.viewModel.LocalCandidatViewModel
import com.example.vitesse.data.viewModel.RemoteCandidatViewModel
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
    private val viewModel: LocalCandidatViewModel by activityViewModels() // Share ViewModel with other fragments
    private val remoteViewModel: RemoteCandidatViewModel by activityViewModels() // Share ViewModel with other fragments

    private lateinit var db: AppDatabase

    // To handle image selection
    private var selectedImageUri: Uri? = null
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
            Toast.makeText(requireContext(), "Permission required to access files", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_OPEN_DOCUMENT = 42
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentEditCandidateBinding.inflate(inflater, container, false)
        return binding.root // Access the binding root after it is properly initialized
    }

    private fun getImageUri(picturePath: String?): Uri? {
        return picturePath?.let {
            when {
                it.startsWith("http://") || it.startsWith("https://") -> {
                    // It's a URL
                    Uri.parse(it)
                }
                it.startsWith("content://") -> {
                    // It's a content URI, we need to resolve it
                    try {
                        Uri.parse(it)
                    } catch (e: Exception) {
                        Log.e("detailsFragment", "Invalid content URI: ${e.message}")
                        null
                    }
                }
                else -> {
                    // It's a local file path
                    try {
                        Uri.fromFile(File(it))
                    } catch (e: Exception) {
                        Log.e("detailsFragment", "Invalid local image path: ${e.message}")
                        null
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the local candidates from the ViewModel
        viewModel.candidateFlow.onEach { candidats ->
            // Update UI with the new list of candidates (you can use this for your RecyclerView or other UI elements)
            Log.d("EditCandidateFragment", "Local candidates updated: $candidats")
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // Observe the selected candidate from SharedViewModel
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
            findNavController().navigate(R.id.homePageFragment) // Navigate back
        }

        // Set up the calendar icon click listener
        binding.calendarIcon.setOnClickListener {
            showDatePickerDialog()
        }

        // Set click listener on image placeholder to open the gallery
        binding.imageCandidat.setOnClickListener {
            openFilePicker()
        }

        // Set up listeners for buttons or other interactive views
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
                    // Update candidate through ViewModel
                    viewModel.performAction(CandidateAction.UpdateCandidate(updatedCandidat))

                    Toast.makeText(requireContext(), "Candidate updated", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.detailsFragment2) // Navigate back
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
            pickFileLauncher.launch("*/*") // You can specify the file type (e.g., "image/*") if needed
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
        _binding = null // Clean up the binding reference to prevent memory leaks
    }
}
