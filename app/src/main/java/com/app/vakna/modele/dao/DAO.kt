package com.app.vakna.modele.dao

/**
 * Interface permettant la liaison BDD - Classes
 * @param <T> Type des entités
 * @param <P> Type des
 */
interface DAO<T,P> {
    /** Récupère toutes les entités dans la BDD */
    fun obtenirTous(): List<T>

    /** Récupère l'entité avec l'id P associé si elle existe (null sinon) */
    fun obtenirParId(id: P): T?

    /** Insère l'entité T dans la BDD */
    fun inserer(entite : T): Boolean

    /** Remplace l'entite avec l'id P par l'entite T */
    fun modifier(id: P, entite : T): Boolean

    /** Supprime l'entité avec l'id P associé */
    fun supprimer(id : P): Boolean
}