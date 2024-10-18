package com.app.vakna.ui.Taches

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
import com.app.vakna.databinding.FragmentTachesBinding
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO


class TachesFragment : Fragment() {

    private var _binding: FragmentTachesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapterJ: ListAdapter
    private lateinit var listAdapterH: ListAdapter
    private lateinit var listAdapterM: ListAdapter
    private lateinit var gestionnaire: GestionnaireDeTaches

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTachesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        gestionnaire = GestionnaireDeTaches(TacheDAO(this.requireContext()))

        SetUpRecyclerView()

        val imageButton: ImageButton = root.findViewById(R.id.boutonAjouterTache)
        imageButton.setOnClickListener {
            val intent = Intent(context, AjouterActivity::class.java)
            startActivity(intent)
        }

        val gererButton: ImageButton = root.findViewById(R.id.boutonGererTache)
        gererButton.setOnClickListener {
            val intent = Intent(context, GererActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun SetUpRecyclerView() {

        val dataJournalier = GestionnaireDeTaches.setToListDataArray(gestionnaire.obtenirTaches(Frequence.QUOTIDIENNE))
        listAdapterJ = ListAdapterProgress(dataJournalier, binding.progressTachesJournalier, requireContext())

        AjoutDividers(binding.listeTachesJournalier)

        binding.listeTachesJournalier.adapter = listAdapterJ

        // Separation diff Frequences

        val dataHebdo = GestionnaireDeTaches.setToListDataArray(gestionnaire.obtenirTaches(Frequence.HEBDOMADAIRE))
        listAdapterH = ListAdapterProgress(dataHebdo, binding.progressTachesHebdomadaire, requireContext())

        AjoutDividers(binding.listeTachesHebdomadaire)

        binding.listeTachesHebdomadaire.adapter = listAdapterH

        // Separation diff Frequences

        val dataMensuel = GestionnaireDeTaches.setToListDataArray(gestionnaire.obtenirTaches(Frequence.MENSUELLE))
        listAdapterM = ListAdapterProgress(dataMensuel, binding.progressTachesMensuel, requireContext())

        AjoutDividers(binding.listeTachesMensuel)

        binding.listeTachesMensuel.adapter = listAdapterM
    }

    private fun AjoutDividers(listeBinding: RecyclerView) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listeBinding.layoutManager = layoutManager

        listeBinding.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManager.orientation).apply {
                setDrawable(requireContext().getDrawable(R.drawable.divider_item)!!)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
