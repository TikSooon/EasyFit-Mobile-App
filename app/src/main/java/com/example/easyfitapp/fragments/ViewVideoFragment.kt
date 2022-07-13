package com.example.easyfitapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.easyfitapp.GlideLoader
import com.example.easyfitapp.R


class ViewVideoFragment : Fragment(), View.OnClickListener
{

    private lateinit var thumbnail: ImageView
    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var link: TextView
    private lateinit var url: String
    private lateinit var thumb: String

    private lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_video, container, false)
        val b = this.arguments
        title = b?.getString("title").toString()
        url = b?.getString("url").toString()
        thumb = b?.getString("thumb").toString()
        desc = b?.getString("desc").toString()
        v.findViewById<TextView>(R.id.lblVidTitle).text = title
        v.findViewById<TextView>(R.id.lblDesc).text = "Description:\n" + desc
        link =  v.findViewById(R.id.link)
        link.text = getString(R.string.link)
        link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        val btnBackV: ImageView = v.findViewById(R.id.btnBackV)
        val btnShareVideo: ImageView = v.findViewById(R.id.btnShareVideo)

        thumbnail = v.findViewById(R.id.thumb)
        GlideLoader(v.context).loadImage(thumb,thumbnail)

        btnBackV.setOnClickListener(this)
        btnShareVideo.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnBackV -> {
                    val activity= context as AppCompatActivity
                    val myFragment: Fragment = VideoFragment()
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, myFragment).addToBackStack(null).commit()
                }
                R.id.btnShareVideo -> {
                    val share = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TITLE, title)
                        putExtra(Intent.EXTRA_TEXT, url)
                        data = Uri.parse(url)
                        setType("text/plain")
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }, null)
                    startActivity(share)
                }
            }
        }
    }


}