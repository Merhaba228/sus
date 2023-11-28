package com.example.sus

import SharedPrefManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.os.Handler
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.loginapp.activity.logic.auth.retrofit.api.*
import com.example.loginapp.activity.logic.auth.retrofit.dto.*
import com.example.sus.activity.logic.auth.retrofit.dto.*
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class profile1 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var myEventsButton: AppCompatButton
    private lateinit var niokrButton: AppCompatButton
    private lateinit var eorButton: AppCompatButton
    private lateinit var publicationsButton: AppCompatButton

    private lateinit var myEventsRecyclerViewContainer: FrameLayout
    private lateinit var niokrRecyclerViewContainer: FrameLayout
    private lateinit var eorRecyclerViewContainer: FrameLayout
    private lateinit var publicationsRecyclerViewContainer: FrameLayout

    private lateinit var recyclerViewMyEvents: RecyclerView
    private lateinit var recyclerViewNiokr: RecyclerView
    private lateinit var recyclerViewEor: RecyclerView
    private lateinit var recyclerViewPublications: RecyclerView

    private val myEventsAdapter = GrantAdapter()
    private val niokrAdapter = NIOKRAdapter()
    private val eorAdapter = DigitalEducationalResourceAdapter()
    private val publicationsAdapter = PublicationAdapter()

    private lateinit var myEventsEmptyTextView: TextView
    private lateinit var niokrEmptyTextView: TextView
    private lateinit var eorEmptyTextView: TextView
    private lateinit var publicationsEmptyTextView: TextView

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
        profilePictureImageView.startAnimation(rotateAnimation)

        myEventsButton = view.findViewById(R.id.myEvents_button)
        niokrButton = view.findViewById(R.id.niokrButton)
        eorButton = view.findViewById(R.id.eorButton)
        publicationsButton = view.findViewById(R.id.publications_button)

        myEventsRecyclerViewContainer = view.findViewById(R.id.myEventsRecyclerViewContainer)
        niokrRecyclerViewContainer = view.findViewById(R.id.niokrRecyclerViewContainer)
        eorRecyclerViewContainer = view.findViewById(R.id.eorRecyclerViewContainer)
        publicationsRecyclerViewContainer = view.findViewById(R.id.publicationsRecyclerViewContainer)

        recyclerViewMyEvents = view.findViewById(R.id.recyclerViewMyEvents)
        recyclerViewNiokr = view.findViewById(R.id.recyclerViewNiokr)
        recyclerViewEor = view.findViewById(R.id.recyclerViewEor)
        recyclerViewPublications = view.findViewById(R.id.recyclerViewPublications)

        myEventsEmptyTextView = view.findViewById(R.id.myEventsEmptyTextView)
        niokrEmptyTextView = view.findViewById(R.id.niokrEmptyTextView)
        eorEmptyTextView = view.findViewById(R.id.eorEmptyTextView)
        publicationsEmptyTextView = view.findViewById(R.id.publicationsEmptyTextView)

        fillAdaptersFromSharedPreferences()

        myEventsButton.setOnClickListener {
            toggleVisibility(myEventsRecyclerViewContainer)
        }

        niokrButton.setOnClickListener {
            toggleVisibility(niokrRecyclerViewContainer)
        }

        eorButton.setOnClickListener {
            toggleVisibility(eorRecyclerViewContainer)
        }

        publicationsButton.setOnClickListener {
            toggleVisibility(publicationsRecyclerViewContainer)
        }

        return view
    }

    private fun fillAdaptersFromSharedPreferences() {
        val niokrList = SharedPrefManager.getNIOKR()
        val grantList = SharedPrefManager.getGrants()
        val resourceList = SharedPrefManager.getDigitalEducationalResources()
        val publicationList = SharedPrefManager.getPublications()

        myEventsAdapter.submitList(grantList)
        niokrAdapter.submitList(niokrList)
        eorAdapter.submitList(resourceList)
        publicationsAdapter.submitList(publicationList)

        myEventsAdapter.notifyDataSetChanged()
        niokrAdapter.notifyDataSetChanged()
        eorAdapter.notifyDataSetChanged()
        publicationsAdapter.notifyDataSetChanged()

        recyclerViewMyEvents.adapter = myEventsAdapter
        recyclerViewNiokr.adapter = niokrAdapter
        recyclerViewEor.adapter = eorAdapter
        recyclerViewPublications.adapter = publicationsAdapter
    }

    private fun hideContainers() {
        myEventsRecyclerViewContainer.visibility = View.GONE
        niokrRecyclerViewContainer.visibility = View.GONE
        eorRecyclerViewContainer.visibility = View.GONE
        publicationsRecyclerViewContainer.visibility = View.GONE
    }

    private fun toggleVisibility(container: FrameLayout) {
        hideContainers()
        if (container.visibility == View.VISIBLE) {
            container.visibility = View.GONE
        } else {
            container.visibility = View.VISIBLE

            val recyclerView: RecyclerView? = when (container) {
                myEventsRecyclerViewContainer -> recyclerViewMyEvents
                niokrRecyclerViewContainer -> recyclerViewNiokr
                eorRecyclerViewContainer -> recyclerViewEor
                publicationsRecyclerViewContainer -> recyclerViewPublications
                else -> null
            }

            recyclerView?.adapter?.let { adapter ->
                if (adapter.itemCount == 0) {
                    showEmptyTextView(container)
                }
            }
        }
    }

    private fun showEmptyTextView(container: FrameLayout) {
        val emptyTextView: TextView = when (container) {
            myEventsRecyclerViewContainer -> myEventsEmptyTextView
            niokrRecyclerViewContainer -> niokrEmptyTextView
            eorRecyclerViewContainer -> eorEmptyTextView
            publicationsRecyclerViewContainer -> publicationsEmptyTextView
            else -> return
        }

        emptyTextView.visibility = View.VISIBLE

        Handler().postDelayed({
            emptyTextView.visibility = View.GONE
        }, 1500)
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

class NIOKRAdapter : ListAdapter<NIOKR, NIOKRAdapter.NIOKRViewHolder>(NIOKRDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NIOKRViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_niokr, parent, false)
        return NIOKRViewHolder(view)
    }

    override fun onBindViewHolder(holder: NIOKRViewHolder, position: Int) {
        Log.d("Check_bind_1", "test")
        val niokr = getItem(position)
        holder.bind(niokr)
    }

    inner class NIOKRViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)

        fun bind(niokr: NIOKR) {
            if (niokr != null) {
                numberTextView.text = "Записи не найдены"
                amountTextView.text = ""
            } else {
                numberTextView.text = "Номер НИОКР: ${niokr.number}"
                amountTextView.text = "Сумма: ${niokr.amount}"
            }
        }
    }

    private class NIOKRDiffer : DiffUtil.ItemCallback<NIOKR>() {
        override fun areItemsTheSame(oldItem: NIOKR, newItem: NIOKR): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: NIOKR, newItem: NIOKR): Boolean {
            return oldItem == newItem
        }
    }
}

class GrantAdapter : ListAdapter<Grant, GrantAdapter.GrantViewHolder>(GrantDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grant, parent, false)
        return GrantViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrantViewHolder, position: Int) {
        val grant = getItem(position)
        holder.bind(grant)
    }

    inner class GrantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeStringTextView: TextView = itemView.findViewById(R.id.typeStringTextView)
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)

        fun bind(grant: Grant) {
            if (grant != null) {
                typeStringTextView.text = "Тип записи: ${grant.typeString}"
                amountTextView.text = "Сумма: ${grant.amount}"
            } else {
                typeStringTextView.text = "Записи не найдены"
            }
        }
    }

    private class GrantDiffer : DiffUtil.ItemCallback<Grant>() {
        override fun areItemsTheSame(oldItem: Grant, newItem: Grant): Boolean {
            return oldItem.typeString == newItem.typeString
        }

        override fun areContentsTheSame(oldItem: Grant, newItem: Grant): Boolean {
            return oldItem == newItem
        }
    }
}

class DigitalEducationalResourceAdapter : ListAdapter<DigitalEducationalResource, DigitalEducationalResourceAdapter.ResourceViewHolder>(ResourceDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_educational_resource, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val resource = getItem(position)
        holder.bind(resource)
    }

    inner class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeStringTextView: TextView = itemView.findViewById(R.id.typeStringTextView)
        private val authorsTextView: TextView = itemView.findViewById(R.id.authorsTextView)

        fun bind(resource: DigitalEducationalResource) {
            typeStringTextView.text = "Тип записи: ${resource.typeString}"
            authorsTextView.text = "Авторы: ${resource.authors}"
        }
    }

    private class ResourceDiffer : DiffUtil.ItemCallback<DigitalEducationalResource>() {
        override fun areItemsTheSame(oldItem: DigitalEducationalResource, newItem: DigitalEducationalResource): Boolean {
            return oldItem.typeString == newItem.typeString
        }

        override fun areContentsTheSame(oldItem: DigitalEducationalResource, newItem: DigitalEducationalResource): Boolean {
            return oldItem == newItem
        }
    }
}

class PublicationAdapter : ListAdapter<Publication, PublicationAdapter.PublicationViewHolder>(PublicationDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_publication, parent, false)
        return PublicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        val publication = getItem(position)
        holder.bind(publication)
    }

    inner class PublicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorsTextView: TextView = itemView.findViewById(R.id.authorsTextView)

        fun bind(publication: Publication) {
            titleTextView.text = "Заголовок: ${publication.moderatedItemInfo.title}"
            authorsTextView.text = "Авторы: ${publication.authors}"
        }
    }

    private class PublicationDiffer : DiffUtil.ItemCallback<Publication>() {
        override fun areItemsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem.moderatedItemInfo.title == newItem.moderatedItemInfo.title
        }

        override fun areContentsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem == newItem
        }
    }
}