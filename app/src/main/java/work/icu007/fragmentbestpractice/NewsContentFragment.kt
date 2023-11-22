package work.icu007.fragmentbestpractice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import work.icu007.fragmentbestpractice.databinding.FragmentNewsContentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "title"
private const val ARG_PARAM2 = "content"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsContentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsContentFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentNewsContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            Log.d(TAG, "onCreate: title: $param1, content: $param2")
            Log.d(TAG, "onCreate: title: ${arguments?.getString("title")},content: ${arguments?.getString("content")}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewsContentBinding.inflate(inflater,container,false)
        val bundle = arguments
        val title = bundle?.getString("title")
        val content = bundle?.getString("content")
        Log.d(TAG, "onCreateView: title: $title, content: $content")
        if ( title != null && content!=null ) {
            refresh(title,content)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun refresh(title: String, content: String){
        Log.d(TAG, "refresh: step in")
        binding.contentLayout.visibility = View.VISIBLE
        binding.newsTitle.text = title
        binding.newsContent.text = content
    }

    companion object {
        const val TAG = "NewsContentFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsContentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String): NewsContentFragment{
            val fragment = NewsContentFragment()
            val bundle = Bundle()
            bundle.putString("title",param1)
            bundle.putString("content",param2)
            Log.d(TAG, "newInstance: title: $param1, content: $param2")
//            fragment.arguments = bundle
            return fragment
        }
    }
}