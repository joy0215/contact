package imd.ntub.myfrags0509

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        class ViewPagerAdater(activity: MainActivity): FragmentStateAdapter(activity){
            override fun getItemCount() = 3

            override fun createFragment(position: Int) =
                when(position){
                    0-> FirstFragment.newInstance()
                    1-> SecondFragment.newInstance()
                    2-> ThirdFragment.newInstance("今天天氣不錯啊")
                    else-> FirstFragment.newInstance()
                }
        }
        // 程式: https://github.com/leonjyentub/MyFrags0509
        val viewPager = findViewById<ViewPager2>(R.id.viewpager)
        viewPager.adapter = ViewPagerAdater(this)
        findViewById<Button>(R.id.btn1).setOnClickListener {
            viewPager.currentItem = 0
        }
        findViewById<Button>(R.id.btn2).setOnClickListener {
            viewPager.setCurrentItem(1, true)
        }
        findViewById<Button>(R.id.btn3).setOnClickListener {
            viewPager.setCurrentItem(2, true)
        }
    }
}