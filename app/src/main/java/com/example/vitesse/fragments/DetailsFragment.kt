package com.example.vitesse.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vitesse.R
import com.example.vitesse.data.viewModel.SharedViewModel
import com.example.vitesse.databinding.FragmentDetailsBinding
import com.example.vitesse.data.service.apiConversion.CurrencyApiService
import com.example.vitesse.data.service.apiConversion.CurrencyRepository
import com.example.vitesse.data.service.apiConversion.CurrencyViewModel
import com.example.vitesse.data.service.apiConversion.CurrencyViewModelFactory
import com.example.vitesse.data.service.apiConversion.RetrofitInstance
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var currencyViewModel: CurrencyViewModel
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Initialize ExchangeRateViewModel
        val currencyApiService = RetrofitInstance.createService(CurrencyApiService::class.java)
        val repository = CurrencyRepository(currencyApiService)
        val viewModelFactory = CurrencyViewModelFactory(repository)
        currencyViewModel = ViewModelProvider(this, viewModelFactory).get(CurrencyViewModel::class.java)

        // Observe the converted salary from the ViewModel
        currencyViewModel.convertedSalary.observe(viewLifecycleOwner) { convertedSalary ->
            binding.textSalaire3.text = convertedSalary

        }
        // Observe any errors in the currency conversion
        currencyViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Observe the currency rates and trigger conversion once the rate is available
        currencyViewModel.currencyRates.observe(viewLifecycleOwner) { gbpRate ->
            if (gbpRate != null && gbpRate > 0) {
                // Only trigger conversion if a valid GBP rate is available
                sharedViewModel.selectedCandidat.value?.let { candidat ->
                    val salaryInEuros = candidat.description.toDoubleOrNull() // Convert to double
                    salaryInEuros?.let { euros ->
                        // Convert user-entered salary from EUR to GBP
                        currencyViewModel.convertSalaryToPounds(euros)
                    } ?: Toast.makeText(context, getString(R.string.invalid_salary_input), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, getString(R.string.currency_conversion), Toast.LENGTH_SHORT).show()
            }
        }
        // Fetch currency rates
        currencyViewModel.fetchCurrencyRates()
        // Show salary in EUR
        binding.textSalaire2.text = getString(R.string.amount)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.detailsToolbar)

        // Enable the back button in the toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set up navigation click listener for the toolbar
        binding.detailsToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }

        // Observe the selectedCandidat and update UI
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.selectedCandidat.collect { candidate ->
                candidate?.let {

                    // Set the toolbar title to "First Name LAST NAME"
                    val formattedTitle = "${it.firstName} ${it.lastName.uppercase()}"
                    (activity as AppCompatActivity).supportActionBar?.title = formattedTitle

                    binding.textAppel.text
                    binding.textSms.text
                    binding.textEmail.text

                    // Display salary in EUR
                    binding.textSalaire2.text = "${it.description} ${getString(R.string.euros)}"
                    binding.textNotes2.setText(it.note)
                    binding.textAPropos2.text = formatBirthdateWithAge(it.anivDat)

                    val salaryInEuros = it.description.toDoubleOrNull()
                    salaryInEuros?.let { euros ->
                        currencyViewModel.convertSalaryToPounds(euros)
                    }

                    val imageUri = getImageUri(it.picture)
                    Glide.with(this@DetailsFragment)
                        .load(imageUri ?: R.drawable.personicon)
                        .placeholder(R.drawable.personicon)
                        .error(R.drawable.personicon)
                        .into(binding.imageCandidat)

                    // Set initial favorite state
                    isFavorite = sharedViewModel.isFavorite(it)
                    activity?.invalidateOptionsMenu()

                    // Set up the call button
                    binding.buttonCall.setOnClickListener {
                        val candidate = sharedViewModel.selectedCandidat.value
                        val phoneNumber = candidate?.phone

                        if (!phoneNumber.isNullOrEmpty()) {
                            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phoneNumber")
                            }
                            startActivity(dialIntent)
                        } else {
                            Toast.makeText(context, getString(R.string.unavailable_phone_number), Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Set up the sms button
                    binding.buttonSms.setOnClickListener {
                        val candidate = sharedViewModel.selectedCandidat.value
                        val phoneNumber = candidate?.phone
                        if (!phoneNumber.isNullOrEmpty()) {
                            val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("smsto:$phoneNumber")
                            }
                            startActivity(smsIntent)
                        } else {
                            Toast.makeText(context, getString(R.string.unavailable_phone_number), Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Set up the email button click listener
                    binding.buttonMail.setOnClickListener {
                        val candidate = sharedViewModel.selectedCandidat.value
                        val emailAddress = candidate?.email
                        if (!emailAddress.isNullOrEmpty()) {
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$emailAddress")
                            }

                            // Check if there is an app to handle the email intent
                            if (emailIntent.resolveActivity(requireContext().packageManager) != null) {
                                startActivity(emailIntent)
                            } else {
                                Toast.makeText(context, getString(R.string.unavailable_email), Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.unavailable_email_address), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_detailsFragment_to_homePageFragment)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)  // Inflate your custom menu
        val favoriteItem = menu.findItem(R.id.boutonFavoris)
        favoriteItem.icon = if (isFavorite) {
            resources.getDrawable(R.drawable.baseline_star_rate_24, null)
        } else {
            resources.getDrawable(R.drawable.star_16dp, null)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.boutonFavoris -> {

                             // Get the selected candidate
                sharedViewModel.selectedCandidat.value?.let { candidat ->
                    val isCurrentlyFavorite = sharedViewModel.isFavorite(candidat)
                    sharedViewModel.toggleFavorite(candidat)

                    item.icon = if (isCurrentlyFavorite) {
                        resources.getDrawable(R.drawable.star_16dp, null)  // Not favorite
                    } else {
                        resources.getDrawable(R.drawable.baseline_star_rate_24, null)  // Favorite
                    }
                }
                true
            }
            R.id.BouttonEdit -> {
                // Handle Edit button click
                findNavController().navigate(R.id.editPageFragment)
                true
            }
            R.id.bouttonSupprimer -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm_deletion))
        builder.setMessage(getString(R.string.delete_alert_dialog))
        builder.setPositiveButton(getString(R.string.delete)) { _, _ ->
            sharedViewModel.selectedCandidat.value?.let { candidat ->
                sharedViewModel.deleteCandidat(candidat)
                findNavController().navigate(R.id.action_detailsFragment_to_homePageFragment)
            }
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
    private fun getImageUri(picturePath: String?): Uri? {
        return picturePath?.let {
            when {
                it.startsWith("http://") || it.startsWith("https://") -> Uri.parse(it)
                it.startsWith("content://") -> Uri.parse(it)
                else -> Uri.fromFile(File(it))
            }
        }
    }
    private fun formatBirthdateWithAge(birthdate: String): String {
        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val birthDate = dateFormat.parse(birthdate)
        birthDate?.let {
            val today = Calendar.getInstance()
            val birthCalendar = Calendar.getInstance().apply { time = it }

            var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            return "$birthdate ($age ${getString(R.string.years_old)})"
        }
        return birthdate
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
