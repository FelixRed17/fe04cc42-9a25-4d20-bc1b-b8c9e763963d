package com.glucode.about_you.about

import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glucode.about_you.about.views.ProfileCardView
import com.glucode.about_you.about.views.QuestionCardView
import com.glucode.about_you.databinding.FragmentAboutBinding
import com.glucode.about_you.mockdata.MockData

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private lateinit var profileCardView: ProfileCardView
    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfileCard()
        setUpQuestions()
    }

    private fun setProfileCard() {
        // Retrieve engineer's name and tech role from arguments
        val engineerName = arguments?.getString("name")
        val techRole = arguments?.getString("role")

        // Find engineer data from mock data based on the name
        val engineer = MockData.engineers.firstOrNull { it.name == engineerName }

        // If engineer data is found, proceed
        engineer?.let {
            // Create a new instance of ProfileCardView passing the context
            profileCardView = ProfileCardView(requireContext())

            // Set engineer's name and tech role on the profile card view
            profileCardView.engineerName = engineerName
            profileCardView.techRole = techRole

            // Set quick stats (years, coffees, bugs) on the profile card view
            profileCardView.years = it.quickStats.years.toString()
            profileCardView.coffee = it.quickStats.coffees.toString()
            profileCardView.bugs = it.quickStats.bugs.toString()

            // Set default image if available
            val defaultImageUri = it.defaultImageName
            if (defaultImageUri != null) {
                profileCardView.setImage(defaultImageUri)
            }

            // Add the profile card view to the container in the binding
            binding.container.addView(profileCardView)

            // Set click listener for the image view within profile card view to pick an image
            profileCardView.imageView.setOnClickListener {
                // Create an intent to pick an image from external storage
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_IMAGE_PICK)
            }
        }
    }

    private fun setUpQuestions() {
        val engineerName = arguments?.getString("name")
        val engineer = MockData.engineers.firstOrNull { it.name == engineerName }

        engineer?.questions?.forEach { question ->
            val questionView = QuestionCardView(requireContext())
            questionView.title = question.questionText
            questionView.answers = question.answerOptions
            questionView.selection = question.answer.index

            binding.container.addView(questionView)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the result is from image pick request and if it was successful
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            // Retrieve the URI of the selected image from the intent data
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                // Log the URI of the selected image for debugging purposes
                Log.d("AboutFragment", "Selected Image URI: $it")

                // Ensure profileCardView is initialized before proceeding
                if (::profileCardView.isInitialized) {
                    // Set the selected image on the profileCardView
                    profileCardView.setImage(it)

                    // Update the defaultImageName in MockData for the corresponding engineer
                    MockData.engineers.find { it.name == arguments?.getString("name") }?.defaultImageName =
                        it

                    // Update the profile card view to reflect the changes (refresh)
                    //setProfileCard()
                }
            }
        }
    }
}

