package com.example.sus

import SharedPrefManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class profile1 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        SharedPrefManager.refreshDataUsingRefreshToken()
        val view: View = inflater.inflate(R.layout.profile_fragment, container, false)
        val name: TextView = view.findViewById(R.id.textView9)
        val studentIDTextView: TextView = view.findViewById(R.id.textView10)
        val profilePictureImageView: ImageView = view.findViewById(R.id.imageView_profile)
        name.text = SharedPrefManager.getUserData()?.email
        studentIDTextView.text = "ID: ${SharedPrefManager.getUserData()?.studentCod}"
        val profilePhotoUrl = SharedPrefManager.getUserData()?.photo?.urlMedium
        Glide.with(this)
            .load(profilePhotoUrl)
            .placeholder(R.drawable.cot_profile)
            .transform(CenterCrop(), RoundedCorners(40))
            .into(profilePictureImageView)
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 500
        rotateAnimation.repeatCount = Animation.INFINITE
        profilePictureImageView.startAnimation(rotateAnimation);


        val button3 = view.findViewById<View>(R.id.turnstiles_button)
        button3.setOnClickListener {
            val intent = Intent(requireContext(), security_activity::class.java)
            startActivity(intent)
        }

        val button4 = view.findViewById<View>(R.id.performance_button)
        button4.setOnClickListener {
            val intent = Intent(requireContext(), DisciplinesActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profile1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}