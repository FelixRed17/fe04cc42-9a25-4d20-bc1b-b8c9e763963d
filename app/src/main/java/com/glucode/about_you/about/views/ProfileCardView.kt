package com.glucode.about_you.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.glucode.about_you.R
import com.glucode.about_you.databinding.ViewProfileCardBinding
import com.glucode.about_you.databinding.ViewQuestionCardBinding
class ProfileCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): CardView(context, attrs, defStyleAttr){
    private val binding: ViewProfileCardBinding =
        ViewProfileCardBinding.inflate(LayoutInflater.from(context), this)
    var engineerName: String? = null
        set(value){
            field = value
            binding.engineerName.text = value
        }
    var techRole: String? = null
        set(value){
            field = value
            binding.techRole.text = value
        }
    var years : String? = null
        set(value){
            field = value
            binding.years.text = value
        }
    var coffee : String? = null
        set(value){
            field = value
            binding.coffee.text = value
        }
    var bugs : String? = null
        set(value){
            field = value
            binding.bugs.text = value
        }
}