## Fragment最佳实践：一个简易版的新闻应用

前面提到过，Fragment很多时候是在平板开发当中使用的，因为它可以解决屏幕空间不能充分利用的问题。那是不是就表明，我们开发的程序都需要提供一个手机版和一个平板版呢？确实有不少公司是这么做的，但是这样会耗费很多的人力物力财力。因为维护两个版本的代码成本很高：每当增加新功能时，需要在两份代码里各写一遍；每当发现一个bug时，需要在两份代码里各修改一次。因此，今天实践内容就是编写兼容手机和平板的应用程序。

首先我们要准备好一个新闻的实体类，新建类News，代码如下所示：

```kotlin
package work.icu007.fragmentbestpractice


/*
 * Author: Charlie_Liao
 * Time: 2023/11/8-14:07
 * E-mail: rookie_l@icu007.work
 */

class News(val title: String, val content: String)
```

News类的代码非常简单，title字段表示新闻标题，content字段表示新闻内容。接着新建NewsContentFragment类并编辑其布局文件fragment_news_content.xml，作为新闻内容的布局：

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".NewsContentFragment">

    <LinearLayout
        android:id="@+id/contentLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:visibility="invisible">

        <TextView
            android:id="@+id/newsTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:padding="10dp"
            android:textSize="25sp"/>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/black"/>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:padding="15dp"
            android:textSize="18sp"/>

    </LinearLayout>

    <View
        android:layout_width="1dp"
        android:layout_height="match_parent"
        android:layout_alignParentLeft="true"
        android:background="@color/black"/>

</RelativeLayout>
```

新闻内容的布局主要可以分为两个部分：头部部分显示新闻标题，正文部分显示新闻内容，中间使用一条水平方向的细线分隔开。除此之外，这里还使用了一条垂直方向的细线，它的作用是在双页模式时将左侧的新闻列表和右侧的新闻内容分隔开。细线是利用View来实现的，将View的宽或高设置为1 dp，再通过background属性给细线设置一下颜色就可以了，这里我们把细线设置成黑色。

另外，我们还要将新闻内容的布局设置成不可见。因为在双页模式下，如果还没有选中新闻列表中的任何一条新闻，是不应该显示新闻内容布局的。

接下来新建一个NewsContentFragment类，继承自Fragment，代码如下所示：

```kotlin
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
```

这里首先在onCreateView()方法中加载了我们刚刚创建的news_content_frag布局，接下来又提供了一个refresh()方法，用于将新闻的标题和内容显示在我们刚刚定义的界面上。注意，当调用了refresh()方法时，需要将我们刚才隐藏的新闻内容布局设置成可见。

这样我们就把新闻内容的Fragment和布局都创建好了，但是它们都是在双页模式中使用的，如果想在单页模式中使用的话，我们还需要再创建一个Activity。右击com.example.fragmentbestpractice包→New→Activity→Empty Activity，新建一个NewsContentActivity，布局名就使用默认的activity_news_content即可。然后修改activity_news_content.xml中的代码，如下所示：

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".NewsContentActivity">

    <fragment
        android:id="@+id/newsContentFrag"
        class="work.icu007.fragmentbestpractice.NewsContentFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

这里充分发挥了代码的复用性，直接在布局中引入了NewsContentFragment。这样相当于把news_content_frag布局的内容自动加了进来。

然后修改NewsContentActivity中的代码，如下所示：

```kotlin
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
        }
    }
}
```

在onCreate()方法中我们通过Intent获取到了传入的新闻标题和新闻内容，然后使用kotlin-android-extensions插件提供的简洁写法得到了NewsContentFragment的实例，接着调用它的refresh()方法，将新闻的标题和内容传入，就可以把这些数据显示出来了。注意，这里我们还提供了一个actionStart()方法用于外部调用启动activity。

接下来还需要再创建一个用于显示新闻列表的布局，新建news_title_frag.xml

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/newsTitleRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />

</LinearLayout>
```

这个布局的代码就非常简单了，里面只有一个用于显示新闻列表的RecyclerView。既然要用到RecyclerView，那么就必定少不了子项的布局。新建news_item.xml作为RecyclerView子项的布局，代码如下所示：

```xml
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/newsTitle"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:maxLines="1"
    android:ellipsize="end"
    android:textSize="18sp"
    android:paddingLeft="10dp"
    android:paddingRight="10dp"
    android:paddingTop="15dp"
    android:paddingBottom="15dp" />
```

子项的布局也非常简单，只有一个TextView。仔细观察TextView，你会发现其中有几个属性是我们之前没有学过的：android:padding表示给控件的周围加上补白，这样不至于让文本内容紧靠在边缘上；android:maxLines设置为1表示让这个TextView只能单行显示；android:ellipsize用于设定当文本内容超出控件宽度时文本的缩略方式，这里指定成end表示在尾部进行缩略。

既然新闻列表和子项的布局都已经创建好了，那么接下来我们就需要一个用于展示新闻列表的地方。这里新建NewsTitleFragment作为展示新闻列表的Fragment，代码如下所示：

```kotlin
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import work.icu007.fragmentbestpractice.databinding.FragmentNewsContentBinding
import work.icu007.fragmentbestpractice.databinding.FragmentNewsTitleBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsTitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsTitleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var isTwoPane = false
    private var _binding: FragmentNewsTitleBinding? = null
    private val binding get() = _binding!!
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
        // Inflate the layout for this fragment
        _binding = FragmentNewsTitleBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTwoPane = activity?.findViewById<View>(R.id.newsContentLayout) != null
    }
}
```

NewsTitleFragment中并没有多少代码，在onCreateView()方法中加载了news_title_frag布局，这个没什么好说的。我们注意看一下onActivityCreated()方法，这个方法通过在Activity中能否找到一个id为newsContentLayout的View，来判断当前是双页模式还是单页模式，因此我们需要让这个id为newsContentLayout的View只在双页模式中才会出现。注意，由于在Fragment中调用getActivity()方法有可能返回null，所以在上述代码中我们使用了一个?.操作符来保证代码的安全性。

那么怎样才能实现让id为newsContentLayout的View只在双页模式中才会出现呢？其实并不复杂，只需要借助我们刚刚学过的限定符就可以了。首先修改activity_main.xml中的代码，如下所示：

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/newsTitleFrag"
        android:name="work.icu007.fragmentbestpractice.NewsTitleFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />

</androidx.constraintlayout.widget.ConstraintLayout>
```

上述代码表示在单页模式下只会加载一个新闻标题的Fragment。

然后新建layout-sw600dp文件夹，在这个文件夹下再新建一个activity_main.xml文件，代码如下所示：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >
    <fragment
        android:id="@+id/newsTitleFrag"
        android:name="work.icu007.fragmentbestpractice.NewsTitleFragment"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1" />
    <FrameLayout
        android:id="@+id/newsContentLayout"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="3" >
        <androidx.fragment.app.FragmentContainerView
            android:id="@+id/newsContentFrag"
            android:name="work.icu007.fragmentbestpractice.NewsContentFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </FrameLayout>
</LinearLayout>
```

可以看出，在双页模式下，我们同时引入了两个Fragment，并将新闻内容的Fragment放在了一个FrameLayout布局下，而这个布局的id正是newsContentLayout。因此，能够找到这个id的时候就是双页模式，否则就是单页模式。

现在我们已经将绝大部分的工作完成了，但还剩下至关重要的一点，就是在NewsTitleFragment中通过RecyclerView将新闻列表展示出来。我们在NewsTitleFragment中新建一个内部类NewsAdapter来作为RecyclerView的适配器，如下所示：

```kotlin
package work.icu007.fragmentbestpractice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import work.icu007.fragmentbestpractice.databinding.FragmentNewsContentBinding
import work.icu007.fragmentbestpractice.databinding.FragmentNewsTitleBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsTitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsTitleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var isTwoPane = false
    private var _binding: FragmentNewsTitleBinding? = null
    private val binding get() = _binding!!


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
        // Inflate the layout for this fragment
        _binding = FragmentNewsTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTwoPane = activity?.findViewById<View>(R.id.newsContentLayout) != null
        val layoutManager = LinearLayoutManager(activity)
        binding.newsTitleRecyclerView.layoutManager = layoutManager
        val adapter = NewsAdapter(getNews())
        binding.newsTitleRecyclerView.adapter = adapter
    }

    private fun getNews(): List<News> {
        val newsList = ArrayList<News>()
        for (i in 1..50) {
            val news = News("This is news title $i", getRandomLengthString("This is news content $i. "))
                    newsList.add(news)
        }
        return newsList
    }
    private fun getRandomLengthString(str: String): String {
        val n = (1..20).random()
        val builder = StringBuilder()
        repeat(n) {
            builder.append(str)
        }
        return builder.toString()
    }

    inner class NewsAdapter(val newsList: List<News>): RecyclerView.Adapter<NewsAdapter.ViewHolder>(){
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            val newsTitle: TextView = view.findViewById(R.id.newsTitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item,parent,false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val news = newsList[holder.bindingAdapterPosition]
                if(isTwoPane) {
                    val fragment = parentFragmentManager.findFragmentById(R.id.newsContentFrag) as NewsContentFragment
                    fragment.refresh(news.title, news.content)
                }else {
                    NewsContentActivity.actionStart(parent.context,news.title,news.content)
                }
            }
            return holder
        }

        override fun getItemCount(): Int {
            return newsList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newsList[position]
            holder.newsTitle.text = news.title
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsTitleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsTitleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
```

之前我们都是将适配器写成一个独立的类，其实也可以写成内部类。这里写成内部类的好处就是可以直接访问NewsTitleFragment的变量，比如isTwoPane。onCreateViewHolder()方法中注册的点击事件，首先获取了点击项的News实例，然后通过isTwoPane变量判断当前是单页还是双页模式。如果是单页模式，就启动一个新的Activity去显示新闻内容；如果是双页模式，就更新NewsContentFragment里的数据。

，onActivityCreated()方法中添加了RecyclerView标准的使用方法。在Fragment中使用RecyclerView和在Activity中使用几乎是一模一样的。这里调用了getNews()方法来初始化50条模拟新闻数据，同样使用了一个getRandomLengthString()方法来随机生成新闻内容的长度，以保证每条新闻的内容差距比较大。