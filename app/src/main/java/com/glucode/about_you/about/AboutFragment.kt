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
        val engineerName = arguments?.getString("name")
        val techRole = arguments?.getString("role")
        val engineer = MockData.engineers.firstOrNull { it.name == engineerName }

        engineer?.let {
            profileCardView = ProfileCardView(requireContext())
            profileCardView.engineerName = engineerName
            profileCardView.techRole = techRole
            profileCardView.years = it.quickStats.years.toString()
            profileCardView.coffee = it.quickStats.coffees.toString()
            profileCardView.bugs = it.quickStats.bugs.toString()


            binding.container.addView(profileCardView)

            // Set click listener for image view to pick an image
            profileCardView.imageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
    private fun checkPermissionsAndPickImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
            )
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                Log.d("AboutFragment", "Selected Image URI: $it")
                if (::profileCardView.isInitialized) {
                    profileCardView.setImage(it)
                }
            }
        }
    }

}
