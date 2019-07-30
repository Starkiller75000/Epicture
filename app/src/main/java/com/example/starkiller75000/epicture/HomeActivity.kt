package com.example.starkiller75000.epicture

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val bundle = Bundle()
    private var fragment: Fragment? = null

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when(p0.itemId) {
            R.id.navigation_profile -> {
                bundle.putString("data", username)
                fragment = ProfileFragment()
                (fragment as ProfileFragment).arguments = bundle
            }
            R.id.navigation_image -> {
                bundle.putString("token", accessToken)
                fragment = ImageFragment()
                (fragment as ImageFragment).arguments = bundle
            }
            R.id.navigation_search -> {
                bundle.putString("token", accessToken)
                fragment = SearchFragment()
                (fragment as SearchFragment).arguments = bundle
            }
            R.id.navigation_upload -> {
                bundle.putString("token", accessToken)
                fragment = UploadFragment()
                (fragment as UploadFragment).arguments = bundle
            }
        }

        return loadFragment(fragment)
    }

    private lateinit var username: String
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    private fun loadFragment(fragment: Fragment?) : Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
            return true
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(this)

        val i = intent
        val url: String = i.getStringExtra("url")
        splitUrl(url)

        bundle.putString("data", username)

        fragment = ProfileFragment()

        (fragment as ProfileFragment).arguments = bundle

        loadFragment(fragment)
    }

    private fun splitUrl(url: String) {
        val outerSplit: List<String> = url.split("#")[1].split("&")

        for ((index, s: String) in outerSplit.withIndex()) {
            val innerSplit: List<String> = s.split("=")

            when (index) {
                0 -> accessToken = innerSplit[1]
                3 -> refreshToken = innerSplit[1]
                4 -> username = innerSplit[1]
            }
        }
    }
}