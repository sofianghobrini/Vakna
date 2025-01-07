package com.app.vakna.modele.api

import java.io.File
import android.content.Context
import android.util.Log
import com.app.vakna.R
import com.app.vakna.modele.dao.objet.Objet
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.FileOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class XmlObjetsManager(private val context: Context) {
    private val objetsFichier = mapOf(
        "en" to "objets_en.xml",
        "fr" to "objets_fr.xml"
    )

    fun creerFichiers() {
        objetsFichier.forEach { (lang, nomFichier) ->
            val fichier = File(context.filesDir, nomFichier)
            if (!fichier.exists()) {
                val inputStream = when (lang) {
                    "en" -> context.resources.openRawResource(R.raw.objets_en)
                    "fr" -> context.resources.openRawResource(R.raw.objets_fr)
                    else -> throw IllegalArgumentException("Langue non supportée")
                }
                val outputStream = FileOutputStream(fichier)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
                Log.d("xml", "Fichier $nomFichier initialisé dans le stockage interne")
            }
        }
    }

    fun remplirFichiers(objets: List<Objet>) {
        supprimerObjets("fr")
        supprimerObjets("en")
        for (i in objets.indices) {
            val regex = Regex("(.+)\\[(.+)]")
            val matchNom = regex.matchEntire(objets[i].getNom())
            val matchDetails = regex.matchEntire(objets[i].getDetails())
            
            if (matchNom != null && matchDetails != null) {
                ajouterObjet("fr", i, matchNom.groupValues[1], matchDetails.groupValues[1])
                ajouterObjet("en", i, matchNom.groupValues[2], matchDetails.groupValues[2])
            }
        }

        afficherFichier(getFichier("fr"))
        afficherFichier(getFichier("en"))
    }

    private fun ajouterObjet (lang: String, id: Int, nom: String, detail: String) {
        val fichier = getFichier(lang)

        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fichier)

        val nouvelObjetNom = document.createElement("string")
        nouvelObjetNom.setAttribute("name", "objet_$id")
        nouvelObjetNom.textContent = nom
        document.documentElement.appendChild(nouvelObjetNom)

        val nouvelObjetDetails = document.createElement("string")
        nouvelObjetDetails.setAttribute("name", "objet_" + id + "_details")
        nouvelObjetDetails.textContent = detail
        document.documentElement.appendChild(nouvelObjetDetails)

        sauvegarderFichier(document, fichier)

        Log.d("xml", "Ajout de l'objet $nom ($detail) [$lang] à l'id $id ")
    }

    private fun supprimerObjets(lang: String) {
        val fichier = getFichier(lang)
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fichier)

        val objetsASupprimer = mutableListOf<Element>()

        val objets = document.getElementsByTagName("string")
        for (i in 0 until objets.length) {
            val node = objets.item(i) as Element
            if (node.getAttribute("name").contains("objet_")) {
                objetsASupprimer.add(node)
            }
        }

        objetsASupprimer.forEach { node -> node.parentNode?.removeChild(node)
        }
        sauvegarderFichier(document, fichier)
    }

    private fun getFichier(lang: String): File {
        val nomFichier = objetsFichier[lang] ?: throw IllegalArgumentException("Langue $lang non supporté")
        return File(context.filesDir, nomFichier)
    }

    private fun sauvegarderFichier(document: Document, file: File) {
        supprimerEspacesInutiles(document.documentElement)

        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        val source = DOMSource(document)
        val result = StreamResult(file)
        transformer.transform(source, result)
    }

    private fun supprimerEspacesInutiles(node: Node) {
        val enfants = node.childNodes
        for (i in enfants.length - 1 downTo 0) { // Parcours inverse pour éviter les décalages lors des suppressions
            val enfant = enfants.item(i)
            if (enfant.nodeType == Node.TEXT_NODE && enfant.textContent.trim().isEmpty()) {
                node.removeChild(enfant) // Supprime les nœuds de texte vides ou composés uniquement d'espaces
            } else if (enfant.nodeType == Node.ELEMENT_NODE) {
                supprimerEspacesInutiles(enfant) // Appelle récursivement pour les sous-éléments
            }
        }
    }

    private fun afficherFichier(file: File) {
        try {
            if (file.exists()) {
                for (line in file.readLines()) {
                    Log.d("xml", line)
                }
            } else {
                Log.e("xml", "Le fichier n'existe pas")
            }
        } catch (e: Exception) {
            Log.e("xml", "Erreur loirs de la lecture du fichier : ${e.message}")
        }
    }

}