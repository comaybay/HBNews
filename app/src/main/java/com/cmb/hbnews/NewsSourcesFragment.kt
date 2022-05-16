package com.cmb.hbnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmb.hbnews.scrapers.NewsSource
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_news_sources.view.*

class NewsSourcesFragment(
    private var onNewsSourcesChange: (ArrayList<NewsSource>) -> Unit
) : Fragment() {
    private var newsSources: ArrayList<NewsSource> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_sources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        for (i in 0 until view.chip_group.childCount) {
            val chip = view.chip_group.getChildAt(i) as Chip
            chip.tag = NewsSource.valueOf(chip.tag as String)
            chip.isChecked = true

            chip.setOnCheckedChangeListener { chip, state ->
                if (state == true) {
                    newsSources.add(chip.tag as NewsSource)
                }
                else {
                    newsSources.remove(chip.tag as NewsSource)
                }

                onNewsSourcesChange(newsSources)
            }
        }

        val userID =  FirebaseAuth.getInstance().currentUser?.uid
        if (userID == null) {
            return
        }

        Firebase.firestore.collection("users").document(userID!!)
            .get().addOnSuccessListener { user ->
                newsSources.clear()
                newsSources.addAll(createNewsSources(user.getString("prefNewsSources")))
                onNewsSourcesChange(newsSources)

                for (i in 0 until view.chip_group.childCount) {
                    val chip = view.chip_group.getChildAt(i) as Chip
                    chip.isChecked = if (newsSources.contains(chip.tag as NewsSource)) true else false
                }
            }
    }

    fun createNewsSources(newsSourcesStr: String?): Array<NewsSource> {
        if (newsSourcesStr.isNullOrEmpty() || newsSourcesStr == "All") {
            return NewsSource.values()
        }

        return newsSourcesStr.split(",").map { code -> NewsSource.valueOf(code) }.toTypedArray()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsSourcesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(onNewsSourcesChange: (ArrayList<NewsSource>) -> Unit) =
            NewsSourcesFragment(onNewsSourcesChange)
    }
}
