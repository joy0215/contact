package imd.ntub.myfrags0509

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_all_data -> viewPager.currentItem = 0
                R.id.nav_add_edit -> viewPager.currentItem = 1
                R.id.nav_team -> viewPager.currentItem = 2
            }
            true
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> bottomNavigationView.selectedItemId = R.id.nav_all_data
                    1 -> bottomNavigationView.selectedItemId = R.id.nav_add_edit
                    2 -> bottomNavigationView.selectedItemId = R.id.nav_team
                }
            }
        })

        // Set default fragment
        if (savedInstanceState == null) {
            viewPager.currentItem = 0
        }
    }

    fun startEditDialog(contact: Contact) {
        val editDialog = EditContactDialogFragment(contact)
        editDialog.show(supportFragmentManager, "EditContactDialogFragment")
    }
}