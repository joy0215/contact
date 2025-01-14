package imd.ntub.myfrags0509

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllDataFragment()
            1 -> AddEditFragment()
            2 -> TeamFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}