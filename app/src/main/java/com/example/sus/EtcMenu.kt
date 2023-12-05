package com.example.sus

import SharedPrefManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.net.Uri

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EtcMenu : Fragment() {
    // TODO: Rename and change types of parameters
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
        val view: View = inflater.inflate(R.layout.etc_menu_fragment, container, false)
        SharedPrefManager.getInstance(requireContext()).refreshDataUsingRefreshToken()

        val button = view.findViewById<View>(R.id.menu_allEvents_button)
        button.setOnClickListener()
        {
            val intent = Intent(requireContext(), AllEventsActivity::class.java)
            startActivity(intent)
        }

//        val button2 = view.findViewById<View>(R.id.menu_disciplines_button)
//        button2.setOnClickListener()
//        {
//            val intent = Intent(requireContext(), DisciplinesActivity::class.java)
//            startActivity(intent)
//        }

//
//        val button4 = view.findViewById<View>(R.id.events_button)
//        button4.setOnClickListener()
//        {
//            val intent = Intent(requireContext(), ActualEventsActivity::class.java)
//            startActivity(intent)
//        }
//
//        val button5 = view.findViewById<View>(R.id.polls_button)
//        button5.setOnClickListener()
//        {
//            val intent = Intent(requireContext(), polls_activity::class.java)
//            startActivity(intent)
//        }
//
//        val button6 = view.findViewById<View>(R.id.newsButton)
//        button6.setOnClickListener()
//        {
//            SharedPrefManager.getInstance(requireContext()).refreshNewsListUsingRefreshToken(){news ->  }
//            val intent = Intent(requireContext(), NewsActivity::class.java)
//            startActivity(intent)
//        }

        val button7 = view.findViewById<View>(R.id.menu_exit_button)
        button7.setOnClickListener()
        {
            SharedPrefManager.clearData()
            val intent = Intent(requireContext(), login_activity::class.java)
            startActivity(intent)
        }

        val buttonGuide = view.findViewById<View>(R.id.menu_guide_button)
        buttonGuide.setOnClickListener {
            val url = "https://www.fld.mrsu.ru/wp-content/uploads/2018/11/Rukovodstvo-polzovatelya-EIOS_ver_1.21-1-1.pdf"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
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