#!/usr/bin/python
# -*- coding: utf-8 -*-

import mysql.connector

connexionBD = None


def getConnexionBD():
    global connexionBD
    try:
        if connexionBD == None:
            connexionBD = mysql.connector.connect(
                host='localhost',
                user='admingsb',
                password='gsb1234',
                database='gsbrv'
            )
        return connexionBD
    except:
        return None


def seConnecter(matricule, mdp):
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                    SELECT vis_nom, vis_prenom                  
                    FROM Visiteur
                    WHERE Visiteur.vis_matricule LIKE %s
                    and Visiteur.vis_mdp LIKE %s 
                '''

        curseur.execute(requete, (matricule, mdp))

        enregistrement = curseur.fetchone()

        visiteur = {}
        if enregistrement != None:
            visiteur['vis_matricule'] = matricule
            visiteur['vis_nom'] = enregistrement[0]
            visiteur['vis_prenom'] = enregistrement[1]

        curseur.close()
        return visiteur

    except:
        return None



def getMedicamentByDepotLegal(depotlegal):
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                 SELECT 
                    med_depotlegal , 
                    med_nomcommercial , 
                    fam_code , 
                    med_composition , 
                    med_effets , 
                    med_contreindic
                FROM Medicament
                WHERE med_depotlegal LIKE %s
                    '''

        curseur.execute(requete, (depotlegal,))
        enregistrement = curseur.fetchone()

        unMedicament = {}
        if enregistrement != None:
            unMedicament['med_depotlegal'] = enregistrement[0]
            unMedicament['med_nomcommercial'] = enregistrement[1]
            unMedicament['fam_code'] = enregistrement[2]
            unMedicament['med_composition'] = enregistrement[3]
            unMedicament['med_effets'] = enregistrement[4]
            unMedicament['med_contreindic'] = enregistrement[5]

        curseur.close()
        return unMedicament

    except:
        return None

def getRapportsVisite(matricule, mois, annee):
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                    select 
                        rv.rap_num ,
                        rv.rap_date_visite ,
                        rv.rap_bilan ,
                        p.pra_nom ,
                        p.pra_prenom ,
                        p.pra_cp ,
                        p.pra_ville ,
                        rv.rap_date_redaction ,
                        m.mot_libelle ,
                        rv.rap_coef_confiance ,
                        rv.rap_lu 
                    from RapportVisite as rv
                    inner join Praticien as p
                    on p.pra_num = rv.pra_num
                    inner join Motif m on rv.mot_num = m.mot_num
                    where rv.vis_matricule = %s
                    and MONTH(rv.rap_date_visite) = %s
                    and YEAR(rv.rap_date_visite) = %s
                    order by rv.rap_date_visite
                '''

        curseur.execute(requete, (matricule, mois, annee))

        enregistrements = curseur.fetchall()

        rapports = []
        for unEnregistrement in enregistrements:
            unRapport = {}
            unRapport['rap_num'] = unEnregistrement[0]
            unRapport['rap_date_visite'] = '%04d-%02d-%02d' % (
                unEnregistrement[1].year, unEnregistrement[1].month, unEnregistrement[1].day)
            unRapport['rap_bilan'] = unEnregistrement[2]
            unRapport['pra_nom'] = unEnregistrement[3]
            unRapport['pra_prenom'] = unEnregistrement[4]
            unRapport['pra_cp'] = unEnregistrement[5]
            unRapport['pra_ville'] = unEnregistrement[6]
            unRapport['rap_date_redaction'] = '%04d-%02d-%02d' % (
                unEnregistrement[7].year, unEnregistrement[7].month, unEnregistrement[7].day)
            unRapport['mot_libelle'] = unEnregistrement[8]
            unRapport['rap_coef_confiance'] = unEnregistrement[9]
            unRapport['rap_lu'] = unEnregistrement[10]
            rapports.append(unRapport)

        curseur.close()
        return rapports

    except:
        return None


def getEchantillonsOfferts(matricule, numRapportVisite):
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                    select med_nomcommercial , off_quantite, o.med_depotlegal
                    from Offrir as o
                    inner join Medicament as m
                    on m.med_depotlegal = o.med_depotlegal
                    where o.vis_matricule = %s
                    and o.rap_num = %s
                '''

        curseur.execute(requete, (matricule, numRapportVisite))

        enregistrements = curseur.fetchall()

        offres = []
        for unEnregistrement in enregistrements:
            uneOffre = {}
            uneOffre['med_nomcommercial'] = unEnregistrement[0]
            uneOffre['off_quantite'] = unEnregistrement[1]
            uneOffre['med_depotlegal'] = unEnregistrement[2]
            offres.append(uneOffre)

        curseur.close()
        return offres

    except:
        return None

def getMedicaments():
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                SELECT
                    med_depotlegal ,
                    med_nomcommercial ,
                    fam_code ,
                    med_composition ,
                    med_effets ,
                    med_contreindic
                FROM Medicament
                  '''

        curseur.execute(requete, ())

        enregistrements = curseur.fetchall()

        medicaments = []
        for unEnregistrement in enregistrements:
            unMedicament = {}
            unMedicament['med_depotlegal'] = unEnregistrement[0]
            unMedicament['med_nomcommercial'] = unEnregistrement[1]
            unMedicament['fam_code'] = unEnregistrement[2]
            unMedicament['med_composition'] = unEnregistrement[3]
            unMedicament['med_effets'] = unEnregistrement[4]
            unMedicament['med_contreindic'] = unEnregistrement[5]
            medicaments.append(unMedicament)

        curseur.close()
        return medicaments

    except:
        return None
def getPraticiens():
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                    select pra_num , pra_nom , pra_prenom , pra_ville, pra_cp
                    from Praticien
                '''

        curseur.execute(requete, ())

        enregistrements = curseur.fetchall()

        praticiens = []
        for unEnregistrement in enregistrements:
            unPraticien = {}
            unPraticien['pra_num'] = unEnregistrement[0]
            unPraticien['pra_nom'] = unEnregistrement[1]
            unPraticien['pra_prenom'] = unEnregistrement[2]
            unPraticien['pra_ville'] = unEnregistrement[3]
            unPraticien['pra_cp'] = unEnregistrement[4]
            praticiens.append(unPraticien)

        curseur.close()
        return praticiens

    except:
        return None

def genererNumeroRapportVisite(matricule):
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                    select max(rap_num)
                    from RapportVisite
                    where vis_matricule = %s
                '''

        curseur.execute(requete, (matricule,))

        enregistrement = curseur.fetchone()

        if enregistrement[0] != None:
            return enregistrement[0] + 1
        else:
            return 1

        curseur.close()
        return visiteur

    except:
        return None


def enregistrerRapportVisite(matricule, numPraticien, dateVisite, bilan):
    numRapportVisite = genererNumeroRapportVisite(matricule)

    if numRapportVisite != None:

        try:
            curseur = getConnexionBD().cursor()

            requete = '''
                insert into RapportVisite( vis_matricule , rap_num , rap_date_visite , rap_bilan , pra_num )
                values( %s , %s , %s , %s , %s )
                '''

            curseur.execute(requete, (matricule, numRapportVisite, dateVisite, bilan, numPraticien))
            connexionBD.commit()
            curseur.close()

            return numRapportVisite

        except:
            return None

    else:
        return None

def enregistrerVraiRapportVisite(mat, praNum, motNum, dateVisite, dateRedac, bilan, coefConfiance):
    numRapportVisite = genererNumeroRapportVisite(mat)

    if numRapportVisite != None:

        try:
            curseur = getConnexionBD().cursor()

            requete = '''
                   insert into RapportVisite(
                   vis_matricule ,
                   rap_num ,
                   rap_date_visite ,
                   rap_date_redaction ,
                   rap_bilan ,
                   pra_num ,
                   rap_coef_confiance ,
                   mot_num
                    )
                   values( %s , %s , %s , %s , %s , %s , %s , %s )
                   '''
            curseur.execute(requete,
                            (mat,
                             numRapportVisite,
                             dateVisite, dateRedac,
                             bilan, praNum, coefConfiance, motNum))

            connexionBD.commit()
            curseur.close()

            return numRapportVisite

        except:
            return None

    else:
        return None
def enregistrerEchantillonsOfferts(matricule, numRapport, echantillons):
    try:
        curseur = getConnexionBD().cursor()

        requete = '''
            insert into Offrir( vis_matricule , rap_num , med_depotlegal , off_quantite )
            values( %s , %s , %s , %s )
            '''

        nbOffres = 0
        offreTab = []
        for offre in echantillons:
            curseur.execute(requete, (matricule, numRapport, offre['med_depotlegal'], offre['off_quantite']))
            nbOffres += curseur.rowcount

        connexionBD.commit()
        curseur.close()
        offreDic = {}
        offreDic['nbOfrres'] = nbOffres
        offreTab.append(offreDic)
        return offreTab

    except:
        return None


def getMotifs():
    try:
        curseur = getConnexionBD().cursor()
        requete = '''
                    select mot_num , mot_libelle
                    from Motif
                '''

        curseur.execute(requete, ())

        enregistrements = curseur.fetchall()

        motifs = []
        for unEnregistrement in enregistrements:
            unMotif = {}
            unMotif['mot_num'] = unEnregistrement[0]
            unMotif['mot_libelle'] = unEnregistrement[1]

            motifs.append(unMotif)

        curseur.close()
        return motifs

    except:
        return None


if __name__ == '__main__':
    '''
    print('Authentification du visiteur a131 :')
    print(seConnecter( 'a131' , '' ))
    print

    print ('Liste des rapports de visite du visiteur a131 :')
    for unRapport in getRapportsVisite( 'a131' , 4 , 2018 ) :
        print (unRapport)
    print

    print ('Liste des praticiens :')
    for unPraticien in getPraticiens() :
        print (unPraticien)
    print

    print ('Liste des medicaments :')
    for unMedicament in getMedicaments() :
        print (unMedicament)
    print

    print ('Générer numero rapport pour le visiteur a131 :')
    print (genererNumeroRapportVisite( 'a131' ))
    print

    print 'Générer numero rapport pour le visiteur t60 :'
    print genererNumeroRapportVisite( 't60' )
    print

    print 'Enregistrer un rapport de visite pour le visiteur a131 :'
    print enregistrerRapportVisite( 'a131' , 85 , '2018-07-01' , 'RAS' )
    print

    echantillons = {}
    echantillons[ 'EVILR7' ] = 2 ;
    echantillons[ 'PHYSOI8' ] = 1 ;
    print echantillons

    print 'Enregistrer les echantillons offerts par le visiteur a131 lors de sa 1ère visite :'
    print enregistrerEchantillonsOfferts( 'a131' , 1 , echantillons )
    print

    print ('Liste des medicaments offerts par le visiteur a131 lors de sa 1ère visite :')
    for uneOffre in getEchantillonsOfferts( 'a131' , 1 ) :
        print (uneOffre)
    print


    for unMotifs in getMotifs():
        print(unMotifs)
    print
    '''
    a = seConnecter("a131", "azerty")
    print(a)
