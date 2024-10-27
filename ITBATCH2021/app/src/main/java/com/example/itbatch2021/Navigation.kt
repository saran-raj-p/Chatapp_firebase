package com.example.itbatch2021


import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class Navigation:AppCompatActivity() {
    lateinit var tog: ActionBarDrawerToggle
    lateinit var draw: DrawerLayout
    override fun setContentView(view: View) {


        draw = layoutInflater.inflate(R.layout.nav_act,null) as DrawerLayout
        val toolbar:Toolbar = draw.findViewById(R.id.toolbar)
        val nav: NavigationView = draw.  findViewById(R.id.nav_view)
        val container:FrameLayout = draw.findViewById(R.id.container)
        container.addView(view)
        super.setContentView(draw)
        tog = ActionBarDrawerToggle(this,draw,toolbar,R.string.nav_open,R.string.nav_close)
        draw.addDrawerListener(tog)
        setSupportActionBar(toolbar)
        tog.syncState()
        nav.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> Intent(this,Loginacc::class.java).also { startActivity(it)}
                R.id.login -> Intent(this,Loginacc::class.java).also { startActivity(it) }
                R.id.Dashboard-> Intent(this,Dashboardact::class.java).also { startActivity(it) }
                R.id.Chat -> Intent(this,Chat::class.java).also { startActivity(it) }
                R.id.Notice -> Intent(this,Notice::class.java).also { startActivity(it) }//Toast.makeText(applicationContext,"go to Notice", Toast.LENGTH_SHORT).show()
                R.id.About -> Toast.makeText(applicationContext,"go to About", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }
    fun setTitle(title:String){
        supportActionBar?.setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (tog.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}