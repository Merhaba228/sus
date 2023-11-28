import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sus.PerformanceActivity
import com.example.sus.R
import com.example.sus.activity.logic.auth.retrofit.dto.Discipline
import com.example.sus.activity.logic.auth.retrofit.dto.RecordBooks_StudentSemester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
interface OnItemClickListener {
    fun onItemClick(disciplineId: String)
}


class DisciplinesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recordBooksAdapter: RecordBooksAdapter
    private lateinit var progressBar: ProgressBar
    private var isNotFirstEntering: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.disciplines_fragment, container, false)
        isNotFirstEntering = true
        SharedPrefManager.getInstance(requireContext())

        recyclerView = view.findViewById(R.id.disciplines_recyclerView)
        progressBar = view.findViewById(R.id.loadingIndicator)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        updateRecyclerView()

        val backButton: ImageButton = view.findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }

    internal fun updateRecyclerView() {

            lifecycleScope.launch {
                launch(Dispatchers.IO) {
                    SharedPrefManager.refreshStudentSemesterUsingRefreshToken()
                }

                delay(400)
            }

        isNotFirstEntering = false

        recordBooksAdapter = RecordBooksAdapter(
            SharedPrefManager.getStudentSemester()?.recordBooks,
            object : OnItemClickListener {
                override fun onItemClick(disciplineId: String) {
                    progressBar.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        suspendCoroutine { continuation ->
                            SharedPrefManager.refreshStudentRatingPlanUsingRefreshToken(disciplineId) { result ->
                                continuation.resume(result)
                            }
                        }

                        suspendCoroutine { continuation ->
                            SharedPrefManager.refreshCurrentDisciplineUsingRefreshToken(disciplineId) { result ->
                                continuation.resume(result)
                            }
                        }

                        progressBar.visibility = View.GONE
                        val intent = Intent(requireActivity(), PerformanceActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        )
        recyclerView.adapter = recordBooksAdapter
    }
}

class RecordBooksAdapter(
    private var recordBookList: List<RecordBooks_StudentSemester>?,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecordBooksAdapter.RecordBooksViewHolder>() {

    inner class RecordBooksViewHolder(itemView: View, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val recordBookTitleTextView: TextView = itemView.findViewById(R.id.recordBookTitle_textView)
        val disciplinesRecyclerView: RecyclerView = itemView.findViewById(R.id.disciplinesRecyclerView)

        fun bind(recordBook: RecordBooks_StudentSemester?) {
            recordBookTitleTextView.text = recordBook?.faculty
            val disciplinesAdapter = DisciplinesAdapter(recordBook?.discipline, itemClickListener)
            disciplinesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            disciplinesRecyclerView.adapter = disciplinesAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordBooksViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_record_books, parent, false)
        return RecordBooksViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecordBooksViewHolder, position: Int) {
        val recordBook = recordBookList?.getOrNull(position)
        holder.bind(recordBook)
    }

    override fun getItemCount(): Int {
        return recordBookList?.size ?: 0
    }
}

class DisciplinesAdapter(
    private var disciplineList: List<Discipline>?,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DisciplinesAdapter.DisciplinesViewHolder>() {

    inner class DisciplinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val disciplineNameTextView: TextView = itemView.findViewById(R.id.disciplineName_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisciplinesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_disciplines, parent, false)
        return DisciplinesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisciplinesViewHolder, position: Int) {
        val discipline = disciplineList?.getOrNull(position)
        holder.disciplineNameTextView.text = discipline?.title

        holder.itemView.setOnClickListener {
            discipline?.id?.let { id -> itemClickListener.onItemClick(id.toString()) }
        }
    }

    override fun getItemCount(): Int {
        return disciplineList?.size ?: 0
    }
}
