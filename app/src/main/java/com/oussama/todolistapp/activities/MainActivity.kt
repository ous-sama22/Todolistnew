package com.oussama.todolistapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.oussama.todolistapp.R
import com.oussama.todolistapp.data.AppData
import com.oussama.todolistapp.utils.Constants
import com.oussama.todolistapp.viewmodels.TaskViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var addTaskButton: FloatingActionButton
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)

        if (AppData.currentUser == null) {
            startAuthActivity()
            finish()
            return
        }

        setupNavigation()
        setupViews()
        setupFabBehavior()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.taskListFragment -> addTaskButton.show()
                else -> addTaskButton.hide()
            }
        }
    }

    private fun setupViews() {
        addTaskButton = findViewById(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            navController.navigate(R.id.action_taskListFragment_to_taskCreationFragment)
        }
    }

    private fun setupFabBehavior() {
        val params = addTaskButton.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = HideBottomViewOnScrollBehavior<FloatingActionButton>()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            R.id.action_sort_date -> {
                viewModel.setSortOrder(Constants.SORT_BY_DATE)
                true
            }
            R.id.action_sort_priority -> {
                viewModel.setSortOrder(Constants.SORT_BY_PRIORITY)
                true
            }
            R.id.action_filter_all -> {
                viewModel.setFilterType(Constants.FILTER_ALL)
                true
            }
            R.id.action_filter_completed -> {
                viewModel.setFilterType(Constants.FILTER_COMPLETED)
                true
            }
            R.id.action_filter_pending -> {
                viewModel.setFilterType(Constants.FILTER_PENDING)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun logout() {
        AppData.currentUser = null
        startAuthActivity()
        finish()
    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}