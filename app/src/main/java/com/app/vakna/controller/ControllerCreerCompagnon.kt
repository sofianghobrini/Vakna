package com.app.vakna.controller

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.vakna.vue.CreerCompagnonActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityCreerCompagnonBinding
import com.app.vakna.modele.dao.compagnon.Compagnon
import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.gestionnaires.GestionnaireDeCompagnons
import com.app.vakna.modele.gestionnaires.Inventaire
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.Personnalite
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.gestionnaires.ShopCompagnons
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.compagnon.CompagnonDAO
import com.app.vakna.modele.dao.compagnonstore.CompagnonStoreDAO
import com.app.vakna.modele.dao.objet.ObjetDAO
import com.app.vakna.modele.dao.refuge.RefugeDAO
import com.app.vakna.modele.dao.refugestore.RefugeStoreDAO
import com.bumptech.glide.Glide

/**
 * Contrôleur pour gérer la création d'un nouveau compagnon
 * @param binding Le binding associé à l'activité de création du compagnon
 */
class ControllerCreerCompagnon(private val binding: ActivityCreerCompagnonBinding) {
    private val REQUEST_CODE = 101
    private val context: Context = binding.root.context
    private val shopDAO = ObjetDAO(context)
    private val compagnonDAO = CompagnonDAO(context)
    private val refugeDAO = RefugeDAO(context)
    private val refugeStoreDAO = RefugeStoreDAO(context)
    private val inventaire = Inventaire(context)
    private val gestionnaireCompagnon = GestionnaireDeCompagnons(context)
    private val compagnonStoreDAO = CompagnonStoreDAO(context)
    private var shopCompagnons = ShopCompagnons(context)
    private var dernierId = compagnonDAO.obtenirTous().maxOfOrNull { it.id } ?: 0 // obtenir l'ID max existant
    init {
        checkAndRequestPermissions()
        val dragon = CompagnonStore(1, "Dragon", 750)
        val lapin = CompagnonStore(2, "Lapin", 450)
        val chat = CompagnonStore(3, "Chat", 500)
        val licorne = CompagnonStore(4, "Licorne", 600)
        val serpent = CompagnonStore(5, "Serpent", 650)
        val ecureuil = CompagnonStore(6, "Ecureuil", 400)
        val compagnonsList = listOf(dragon, lapin, chat, licorne, serpent, ecureuil)

        compagnonsList.forEach {
            shopCompagnons.ajouterCompagnon(it)
        }

        val especeList = listOf("Dragon", "Lapin", "Chat", "Licorne", "Serpent", "Ecureuil")

        val jouet1  = Objet(0, "Bateau" , 15 , 5 , TypeObjet.JOUET, "Attention à ne pas imiter le Titanic ...", "file:///android_asset/jouets/jouet_bateau.png")
        val jouet2  = Objet(1, "Dinosaure" , 20, 6 , TypeObjet.JOUET, "Grrr ! Je suis le roi méchants !", "file:///android_asset/jouets/jouet_dinosaure.png")
        val jouet3  = Objet(2, "Ourson" , 25, 7 , TypeObjet.JOUET, "Câlins gratuits !", "file:///android_asset/jouets/jouet_ourson.png")
        val jouet4  = Objet(3, "Robot" , 30, 8 , TypeObjet.JOUET, "Bip bip bop skibidi bop", "file:///android_asset/jouets/jouet_robot.png")
        val jouet5  = Objet(4, "Voiture" , 35, 9 , TypeObjet.JOUET, "Vrouuuuum, vrouuuuum!", "file:///android_asset/jouets/jouet_voiture.png")

        val jouetsItems = listOf(jouet1, jouet2, jouet3, jouet4, jouet5)

        val kebab    = Objet(10, "Kebab"   , 8 , 10, TypeObjet.NOURRITURE, "Le cholestérole est mon meilleur ami", "file:///android_asset/nourritures/nourriture_kebab.png")
        val pizza    = Objet(11, "Pizza"   , 12, 15, TypeObjet.NOURRITURE, "Un pizza dé la mama, prego.", "file:///android_asset/nourritures/nourriture_pizza.png")
        val burger   = Objet(12, "Burger"  , 10, 8 , TypeObjet.NOURRITURE, "Can I get some burger ? With some peanut butter.", "file:///android_asset/nourritures/nourriture_plumberger.png")
        val sandwich = Objet(13, "Sandwich", 7 , 5 , TypeObjet.NOURRITURE, "L'une des nourritures les plus saine de ce jeu.", "file:///android_asset/nourritures/nourriture_sandwich.png")
        val salade   = Objet(14, "Salade"  , 6 , 4 , TypeObjet.NOURRITURE, "Sérieusement vous voulez manger du vert ?", "file:///android_asset/nourritures/nourriture_salade.png")
        val pasta    = Objet(15, "Pasta"   , 11, 9 , TypeObjet.NOURRITURE, "Mamamia c'est la pasta dé la pizza !", "file:///android_asset/nourritures/nourriture_pasta.png")
        val sushi    = Objet(16, "Sushi"   , 14, 13, TypeObjet.NOURRITURE, "Non on ne va faire de blague raciste ... quoique ...", "file:///android_asset/nourritures/nourriture_sushi.png")
        val steak    = Objet(17, "Steak"   , 20, 18, TypeObjet.NOURRITURE, "De la viande à l'état pure (c'est quand même mieux que la salade)", "file:///android_asset/nourritures/nourriture_steak.png")
        val nourritureItems = listOf(kebab, pizza, burger, sandwich, salade, pasta, sushi, steak)

        val allItems = jouetsItems + nourritureItems

        allItems.forEach {
            shopDAO.inserer(it)
        }

        refugeStoreDAO.inserer(RefugeStore(1, "Cantine", 1.5f, 1.0f, 1.0f, 1.0f, 250))

        refugeDAO.inserer(Refuge(1, "Tresor", 1.0f, 1.0f, 1.0f, 1.5f, true))

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, especeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.EspeceSelect.adapter = adapter

        // Charger l'image initiale pour la première espèce
        afficherImageCompagnon(especeList.first())

        // Écouter les changements dans le Spinner
        binding.EspeceSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val especeSelectionnee = especeList[position]
                afficherImageCompagnon(especeSelectionnee)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Désactiver le bouton de confirmation au début
        binding.boutonCreerCompagnon.isEnabled = false

        // Configurer un TextWatcher pour écouter les changements de texte
        binding.inputNomCompagnon.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Activer ou désactiver le bouton selon le contenu du champ
                binding.boutonCreerCompagnon.isEnabled = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ne rien faire avant la modification du texte
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Ne rien faire pendant la modification du texte
            }
        })

        // Configurer le bouton de confirmation de création du compagnon
        binding.boutonCreerCompagnon.setOnClickListener {
            creerCompagnon()
            Log.e("test","gdfgdhbfbgcf")
            val sharedPreferences = context.getSharedPreferences("AppPreferences",
                AppCompatActivity.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstLaunch", false)
            editor.apply()
            navigateToMainActivity()
        }

        // Configurer l'éditeur de texte pour détecter l'appui sur "Enter"
        binding.inputNomCompagnon.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                if (binding.boutonCreerCompagnon.isEnabled) {
                    creerCompagnon()
                    Log.e("test","gdfgdhbfbgcf")
                    val sharedPreferences = context.getSharedPreferences("AppPreferences",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isFirstLaunch", false)
                    editor.apply()
                    navigateToMainActivity()
                }
                true
            } else {
                false
            }
        }
    }

    /**
     * Fonction privée pour créer un nouveau compagnon
     */
    private fun creerCompagnon()  {
        val context = binding.root.context
        val nomCompagnon = binding.inputNomCompagnon.text.toString().trim()
        val nomEspece = binding.EspeceSelect.selectedItem.toString()
        dernierId += 1
        // Créer une nouvelle instance de compagnon avec des valeurs par défaut
        val nouveauCompagnon = Compagnon(
            id = dernierId,
            nom = nomCompagnon,
            faim = 50,
            humeur = 50,
            xp = 0,
            espece = nomEspece,
            personnalite = Compagnon.personnalite_compagnon(),
            actif = true
        )

        // Instancier le DAO pour manipuler les données du compagnon dans la base
        val compagnonDAO = CompagnonDAO(context)

        // Insérer le compagnon dans la base et vérifier le succès
        val insertionReussie = compagnonDAO.inserer(nouveauCompagnon)

        // Afficher un message en fonction du résultat de l'insertion
        if (insertionReussie) {
            Toast.makeText(context, context.getString(R.string.compagnon_creation_valide), Toast.LENGTH_SHORT).show()
            inventaire.ajouterPieces(300) //Pièce offert des le depart
        } else {
            Toast.makeText(context, context.getString(R.string.compagnon_existe_erreur), Toast.LENGTH_SHORT).show()
            dernierId -= 1
        }
    }



    /**
     * Fonction privée pour affiche le visuel du campagnon
     */
    private fun afficherImageCompagnon(espece: String) {
        val imageRes = when (espece) {
            "Dragon" -> shopCompagnons.obtenirCompagnon("Dragon")?.apparenceDefaut()
            "Lapin" -> shopCompagnons.obtenirCompagnon("Lapin")?.apparenceDefaut()
            "Chat" -> shopCompagnons.obtenirCompagnon("Chat")?.apparenceDefaut()
            "Licorne" -> shopCompagnons.obtenirCompagnon("Licorne")?.apparenceDefaut()
            "Serpent" -> shopCompagnons.obtenirCompagnon("Serpent")?.apparenceDefaut()
            "Ecureuil" -> shopCompagnons.obtenirCompagnon("Ecureuil")?.apparenceDefaut()
            else -> shopCompagnons.obtenirCompagnon("Dragon")?.apparenceDefaut()
        }
        Glide.with(context)
            .load(imageRes)
            .into(binding.dragonGif)
    }


    /**
     * Fonction privée pour naviguer vers l'activité principale après création
     */
    private fun navigateToMainActivity() {
        val context = binding.root.context
        if (context is CreerCompagnonActivity) {
            val intent = Intent(context, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            context.startActivity(intent)
            context.finish()
        }
    }



    private fun checkAndRequestPermissions() {
        val permissionsNeeded = arrayOf(
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.USE_EXACT_ALARM
        )

        val permissionsToRequest = permissionsNeeded.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        // If there are permissions to request, do so
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as CreerCompagnonActivity, permissionsToRequest, REQUEST_CODE)
        }
    }
}
