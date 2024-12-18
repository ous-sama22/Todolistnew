package com.oussama.todolistapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.oussama.todolistapp.R
import com.oussama.todolistapp.adapters.AuthPagerAdapter
import com.oussama.todolistapp.data.AppData

class AuthActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Check if user is already logged in
        if (AppData.currentUser != null) {
            startMainActivity()
            finish()
            return
        }

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = AuthPagerAdapter(this)
    }

    private fun setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) getString(R.string.login) else getString(R.string.sign_up)
        }.attach()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}