package com.demo.habitcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.demo.habitcheck.service.NotifyWorker
import com.demo.habitcheck.ui.addnote.AddNoteActivity
import com.demo.habitcheck.ui.editnote.EditTaskFragment
import com.demo.habitcheck.ui.home.HomeFragment
import com.demo.habitcheck.utils.UtilExtensions.openActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
        observeField()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.extras != null) {
            var id = intent.getIntExtra(NotifyWorker.NOTIFICATION_ID, 0)
            Log.d("--->", "id: $id")
            viewModel.getTask(id)
        }
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            openActivity(AddNoteActivity::class.java)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_done, R.id.nav_slideshow), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initData() {
        if (intent.extras != null) {
            var id = intent.getIntExtra(NotifyWorker.NOTIFICATION_ID, 0)
            Log.d("--->", "id: $id")
            viewModel.getTask(id)
        }
    }

    private fun observeField() {
        // Handle notification
        viewModel.isGetTaskSuccess.observe(this@MainActivity, {
            if (it == true) {
                val bundle = Bundle()
                bundle.putParcelable(EditTaskFragment.TASK_ARG, viewModel.task)
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_edit_task, bundle)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_task ->
                showTask()
            R.id.action_task_not_done ->
                showTaskNotDone()
            R.id.action_clear_task ->
                clearNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showTask() {
        val fragmentID = findNavController(R.id.nav_host_fragment).currentDestination?.id
        if (fragmentID == R.id.nav_home) {
            val navHostFragment: Fragment? =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val fragment: HomeFragment =
                navHostFragment?.childFragmentManager?.fragments?.get(0) as HomeFragment
            fragment.getAllTask()
        }
    }

    private fun showTaskNotDone() {
        val fragmentID = findNavController(R.id.nav_host_fragment).currentDestination?.id
        if (fragmentID == R.id.nav_home) {
            val navHostFragment: Fragment? =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val fragment: HomeFragment =
                navHostFragment?.childFragmentManager?.fragments?.get(0) as HomeFragment
            fragment.getAllTaskNotDone()
        }
    }

    private fun clearNote() {
        TODO("Not yet implemented")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}