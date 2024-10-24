package com.app.vakna.ui.Projets
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.AjouterActivity
import com.app.vakna.GererActivity
import com.app.vakna.R
import com.app.vakna.adapters.ListAdapter
import com.app.vakna.adapters.ListAdapterProgress
import com.app.vakna.controller.ControllerTaches
import com.app.vakna.databinding.FragmentProjetsBinding
import com.app.vakna.databinding.FragmentTachesBinding
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO

class ProjetsFragment : Fragment() {
    private var _binding: FragmentProjetsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjetsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // ControllerProjets(binding)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}