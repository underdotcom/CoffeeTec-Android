package com.example.coffetec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.coffetec.databinding.ActivityHomeBinding
import com.example.coffetec.fragments.HarvestFragment
import com.example.coffetec.fragments.ProfileFragment
import com.example.coffetec.fragments.ShowTreeFragment
import com.example.coffetec.fragments.TreesFragment
import com.example.coffetec.model.Tree
import com.example.coffetec.recycler.TreesViewHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity(), TreesViewHolder.Listener, ShowTreeFragment.Listener {

    private lateinit var harvestFragment: HarvestFragment
    private lateinit var profileFragment : ProfileFragment
    private lateinit var treesFragment: TreesFragment
    private lateinit var showTreeFragment: ShowTreeFragment
    private val binding : ActivityHomeBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        harvestFragment = HarvestFragment.newInstance()
        profileFragment = ProfileFragment.newInstance()

        //trees_section fragments
        treesFragment = TreesFragment.newInstance()
        treesFragment.listenerViewHolder = this
        loadCafetos()
        showTreeFragment = ShowTreeFragment.newInstance()
        showTreeFragment.listener = this

        binding.navigator.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.harvestmenu -> {showFragment(harvestFragment)}
                R.id.homemenu -> {showFragment(profileFragment)}
                R.id.treemenu -> {showFragment(treesFragment)}
            }
            true
        }
    }

    private fun loadCafetos(){
        val trees = ArrayList<Tree>()
        Firebase.firestore.collection("trees").get().addOnCompleteListener{ task ->
            for(document in task.result!!) {
                val treeFound = document.toObject(Tree::class.java)
                trees.add(treeFound)
            }
            treesFragment.trees = trees
            treesFragment.loadComplete()
        }
    }

    private fun showFragment (fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.homeFragment.id, fragment)
        transaction.commit()
    }

    // OBSERVER!!!!
    override fun onClickShowTree(tree: Tree) {
        showTreeFragment.tree = tree
        Log.e(">>>","Estoy en el home y recibi a: ${tree.name}")
        showFragment(showTreeFragment)
    }
    override fun onBackButtonShowTreeFragment() { showFragment(treesFragment) }
    override fun updateTreeInfo(tree: Tree) {
        Firebase.firestore.collection("trees").document(tree.id).set(tree).addOnSuccessListener {
            Toast.makeText(this, R.string.successfull_update, Toast.LENGTH_SHORT).show()
        }
    }
}