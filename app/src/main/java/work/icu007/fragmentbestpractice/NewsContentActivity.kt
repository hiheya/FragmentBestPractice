package work.icu007.fragmentbestpractice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import work.icu007.fragmentbestpractice.databinding.ActivityNewsContentBinding
import work.icu007.fragmentbestpractice.databinding.FragmentNewsContentBinding

class NewsContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsContentBinding
    private lateinit var contentFragmentBinding: FragmentNewsContentBinding

    companion object{
        const val TAG = "NewsContentActivity"
        fun actionStart(context: Context, title: String, content: String){
            val intent = Intent(context, NewsContentActivity::class.java).apply{
                putExtra("news_title", title)
                putExtra("news_content", content)
                Log.d(TAG, "actionStart: title: $title, content: $content")
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("news_title")
        val content = intent.getStringExtra("news_content")
        Log.d("NewsContentActivity", "onCreate: title: $title, content: $content")

        if (title != null && content != null){
            val fragment = supportFragmentManager.findFragmentById(R.id.newsContentFrag) as NewsContentFragment
            fragment.refresh(title,content)
            /*val fragment = NewsContentFragment()
            val bundle = Bundle()
            bundle.putString("title",title)
            bundle.putString("content",content)
            fragment.arguments = bundle*/
//            val fragment = NewsContentFragment.newInstance(title,content)
//            supportFragmentManager.beginTransaction().replace(R.id.newsContentLayout,fragment).commit()
        }
    }
}