package com.glucode.about_you.engineers

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.glucode.about_you.R
import com.glucode.about_you.databinding.FragmentEngineersBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData

class EngineersFragment : Fragment() {
    private lateinit var binding: FragmentEngineersBinding
    private lateinit var engineerList: MutableList<Engineer>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEngineersBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        engineerList = MockData.engineers.toMutableList()
        setUpEngineersList(engineerList)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_engineers, menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_years -> {
                setUpEngineersList(MockData.engineers.sortedBy { it.quickStats.years })
                return true
            }

            R.id.action_coffees -> {
                setUpEngineersList(MockData.engineers.sortedBy { it.quickStats.coffees })
                return true
            }

            R.id.action_bugs -> {
                setUpEngineersList(MockData.engineers.sortedBy { it.quickStats.bugs })
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setUpEngineersList(engineers: List<Engineer>) {
        binding.list.adapter = EngineersRecyclerViewAdapter(engineers) {
            goToAbout(it)
        }
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(dividerItemDecoration)
    }

    private fun goToAbout(engineer: Engineer) {
        val bundle = Bundle().apply {
            putString("name", engineer.name)
            putString("role", engineer.role)
        }
        findNavController().navigate(R.id.action_engineersFragment_to_aboutFragment, bundle)
    }

}