
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    private val fragment: Fragment,
    private val fragmentList: List<Fragment>,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]


}