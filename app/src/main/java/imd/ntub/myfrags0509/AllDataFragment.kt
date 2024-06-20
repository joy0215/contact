package imd.ntub.myfrags0509

import android.database.CursorWindow
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import imd.ntub.myfrags0509.databinding.FragmentAllDataBinding
import java.lang.reflect.Field

class AllDataFragment : Fragment() {

    private var _binding: FragmentAllDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ContactAdapter
    private lateinit var contactList: MutableList<Contact>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 增加 CursorWindow 大小
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) // 100MB
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setupRecyclerView()

        binding.buttonRefresh.setOnClickListener {
            refreshData()
        }
    }

    private fun setupRecyclerView() {
        val dbHelper = DBHelper(requireContext())
        contactList = dbHelper.getAllContacts().toMutableList()

        adapter = ContactAdapter(contactList, (activity as MainActivity))
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
    }

    private fun refreshData() {
        val dbHelper = DBHelper(requireContext())
        contactList.clear()
        contactList.addAll(dbHelper.getAllContacts())
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}