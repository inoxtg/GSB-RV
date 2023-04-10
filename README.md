# GSB-RV-Visiteur

## Application de consultation et renseignements de rapports visites

		--SDK 
			-- 33 lvl 5
		--JDK
			-- 11.0.8
		--Kotlin
			-- LATEST
		--Graddle
			-- LATEST
		--Volley
			-- LATEST
## Entités (Kotlin)

	--Medicament
		var depotLegal: String,
	    var nom: String,
	    var code: String,
	    var composition: String,
	    var effet: String,
	    var indication: String

	--MedicamentOffert
		var leMedicament: Medicament,
		var quantite: Int

	--Motif
	    var numero: Int,
		var libelle: String

	--Praticien
		var numero: Int,
	    var nom: String,
	    var prenom: String,
	    var ville: String,
	    var cp: String,

	--Visiteur
	    var matricule: String,
	    var password: String,
	    var prenom: String,
	    var nom: String

	--RapportVisite
		var leVisiteur: Visiteur,
	    var lePraticien: Praticien,
	    var numero: Int,
	    var dateVisite: String,
	    var dateRedac: String,
	    var bilan: String,
	    var motif: String,
	    var coefConfiance: Int,
	    var lu: Boolean

## Adapteurs (Java)

	"" 
	En programmation Android, un adaptateur (adapter en anglais) est une classe qui permet de lier des données à une vue. Les adaptateurs sont souvent utilisés pour fournir des données à des listes déroulantes, des listes ou des grilles. Ils peuvent être créés en utilisant une implémentation personnalisée de l'interface Adapter ou en utilisant une classe de base comme ArrayAdapter, BaseAdapter, CursorAdapter ou SimpleAdapter. Les adaptateurs personnalisés peuvent être utilisés pour obtenir des fonctionnalités supplémentaires telles que la mise en cache des données pour une utilisation ultérieure, la personnalisation de la disposition des éléments de la liste, la définition de modèles de liste personnalisés et bien plus encore. Les adaptateurs sont un élément clé de la création d'interfaces utilisateur dynamiques et interactives dans les applications Android. 
	""

	--MedicamentsAdapter
		Entitée concernée : MedicamentOffert
		Used at : Consulter & Saisir

	--MedicamentsCompletAdapter
		Entitée concernée : Medicament
		Used at : Saisir

	--MotifAdapter
		Entitée concernée : Motif
		Used at : Saisir

	--PraticienAdapter
		Entitée concernée : Praticien
		Used at : Saisir

	--RapportAdapter
		Entitée concernée : RapportVisite
		Used at : Consulter

## Dialog (Kotlin)

	""
	Un dialog est une fenêtre pop-up qui permet d'interagir avec l'utilisateur dans une application Android. Les dialogues peuvent être utilisés pour afficher des messages d'erreur, des invitations de confirmation, des boîtes de dialogue pour la saisie de données, et bien d'autres choses encore. Les dialogues sont souvent utilisés dans les cas où il est nécessaire d'interrompre le flux normal de l'application et d'attendre une entrée de l'utilisateur avant de pouvoir continuer.  

	Il existe deux types de dialogues dans Android : les dialogues systèmes et les dialogues personnalisés. Les dialogues systèmes sont fournis par Android et ont un aspect et un comportement cohérents sur toutes les plateformes Android. Les dialogues personnalisés, quant à eux, permettent aux développeurs de créer des dialogues spécifiques à leur application, avec un aspect et un comportement personnalisables en fonction des besoins de l'application. Les dialogues personnalisés peuvent être créés en utilisant la classe Dialog ou AlertDialog, ou en créant une vue personnalisée à l'aide de la classe DialogFragment.
	""

		--DeconnectionDialog
			Confirmation de déconnection

		--RetourDialog 
			Confirmation de retour à l'activité précedente

		--HelpDialog
			Explication de Consulter & Saisir

		--MédicamentsInformationDialog
			Information relative à un médicament en particulier

## Services 

	""
	Les services sont pour l'instant dans les activités, ils ne sont pas implémentés en dehors, dans un package particulier mais à terme oui.
	""

## Activités 

				!!!
					L'utilisateur, une fois authentifié est stocké dans une Session
					Idem pour le rapport dans le chemin consulter 
						Le passage d'objet personnalisés dans un Intent sont compliqués car nécessite de "parcer" -> action couteuse
				!!!
	
	### MainActivity
		--Login 
			Entries : Matricule, Mot de passe
			Appel au web service de connection
				Reponse + : Passage a l'activité suivant (MenuActivity) et création d'une entitée visiteur
				Reponse - : Toast "matricule ou mdp invalide"


	### MenuActivity
		--Information utilisateurs
			NOM
			Prenom

		--Bouton Deconnection
			Dialog DeconnectionDialog()
				Retour à MainActivity

		--Bouton Information
			Dialog HelpDialog()

		--Bouton Consulter
			Passage à l'activité suivante (ConsulterActivity)

		--Bouton Saisir
			Passage à l'activité suivante (SaisirActivity)

	### ConsulterActivity
		--Information utilisateurs
			NOM
			Prenom

		--Bouton Deconnection
			Dialog DeconnectionDialog()
				Retour à MainActivity

		--Bouton Retour
			Dialog RetourDialog()
				Retour à l'activité précedente (MenuActivity)

		--DatePicker
			--Entries : Mois, Année         !!! JOUR EST INUTILE !!!
			--Appel au web service de recupération du rapport en fonction de mois, année et matricule
				-réponse + : Affichage dans une listView clicables des rapports correspondants (affichage du numéro, date visite et praticien)
					On click sur ce rapport : 
						Création d'une entité rapportVisite
						Ajout dans sa Session
						Passage à l'activité suivante (RapportActivity)

				-réponse - : Toast "Aucun rapport à la date sélectionnée"

	### RapportActivity
		--Information utilisateurs
			NOM
			Prenom

		--Bouton Deconnection
			Dialog DeconnectionDialog()
				Retour à MainActivity

		--Bouton Retour
			Dialog RetourDialog()
				Retour à l'activité précedente (ConsulterActivity)

		--Affichages des informations relatives au rapport obtenues précedements, récupération grâce à la session
			Numéro
			DateVisite
			Praticien
				Nom
				Prenom
				Ville
				Cp
			DateRedaction
			Motif
			Bilan
			Medicaments offerts              !!! Optionnel( car pas forcéments de médicaments offerts avec un rapport) !!!
				--ListView cliquable des médicaments offerts (affichage du nom commercial et de la quantitée offerte)
					On Click sur un médicament : affichage des informations relatives au médicament :
						Nom commercial
						Depot legal
						Code
						Composition
						Effets
						Contre-indications


	### SaisirActivity

	!!!
		Les requêtes se de recupération des 
			praticiens,
			motifs,
			médicaments,
		se font au moment de l'initialisation de l'activité, rechagement manuel si problème
	!!!

	!!!
		Les boutons + rouges sont cliquables
	!!!
		--Information utilisateurs
			NOM
			Prenom

		--Bouton Deconnection
			Dialog DeconnectionDialog()
				Retour à MainActivity

		--Bouton Retour
			Dialog RetourDialog()
				Retour à l'activité précedente (ConsulterActivity)

		--TextInput Bilan
			Zone de texte permettant de décrire le bilan de la visite

		--Bouton + Praticien
			Bouton affichant un spinner type dialog avec tous les praticiens référencés dans la BDD   			!!! Premier élèment = Praticiens : (on click toast : "choississez un praticien...") !!!!
				NOM Prenom VILLE CP
				On Click :
					Modifie les textView d'en dessous pour y afficher les informations du praticien séléctionner                   !!!L'utilisateur ne peut doonc pas ajouter de lui même un nouveau praticien !!!
					Nom Prenom
					VILLE Cp

		--DatePicker DateVisite
			Selectionne la date de la visite                        !!! Cliquable pour remplir le champs plus facilement !!!

		--TextView DateRedaction 
			Affichage de la date du jour
			non modifiable

		--Bouton + Motif & spinner dropdown motifs
			- + Pour les motifs personnalisés
				Dialog ajout motif
					On ok
						TextInput de son motif
						Cache le spinner dropdown des motifs pré enregistrés
						Affiche le nouveau motif à la place
					On annuler
						Ne fait rien sauf si déjà un motif personnalisé : le supprimer et remet le spinner dropdown des motifs pré enregistrés
			-Spinner dropdown pour motifs pré enregristrés														!!! Premier élèment = Motifs : (on click toast : "choississez un motif...") !!!
				On click 
					Affecte le motif

		--Spinner dropdown CoefConfiance
			Spinner de integer allant de 0 à 5 (inclus)
			défaut sur 0

		--Spinner Dialog de médicaments offerts
			-Affiche tous les médicaments référencés dans la BDD            										 !!! Premier élèment = Médicaments : (on click toast :" choississez un médicament...") !!!
			 NOM COMMERCIAL    DEPOT LEGAL
				On click
					Dialog ajoutMedicament permet d'ajouter la quantité
						TextInput de la quantité (number only)
						On ok 
							Ajoute à la ListView des Médicaments Offerts Choisis

							!!!
							Si le médicament est déjà présent 
								Suppression puis réajout avec la nouvelle quantité
							Si la quantité est à 0 ou vide 
								Rien + Toast : "Entrez une quantité"
							!!!


						On annuler 
							rien

		--ListView des médicamentsOfferts
			-Affiche la liste des médicaments ajoutés
				NOM COMMERCIAL  QUANTITE
				On click :
					Dialog confirmation de suppression du médicament de la liste
						On ok :
							Suppression
						On annuler : 
							Rien

		!!!
		PAS DE CONFIRMATION POUR VALIDER SI BON VALIDE DIRECT
		!!!

		--Bouton Valider
			Verifie conformité des champs :
				praticien
				bilan différent de vide
				motif
			Si non : 
				Toast + praticien ou bilan ou motif invalide

			Ajoute le rapport en faisant appel au web service correspondant
				Toast "Rapport Crée"

		!!!
		AJOUTE TOUJOURS DES MEDICAMENTS OFFERTS
		Enfin essaye toujours d'y accèder
			si aucun : rien
			si any : enregistrement
		!!!




# GSB-RV-SERVEUR 

## Serveur de web services pour l'application GSB-RV-Visiteur

		--Python 
			-- 3.9
		--Flask
			-- 2.2.3
		--Mysql-connector-python
			-- 8.0.32
		--pip
			-- 22.3.1
		--MariaDB Server
			-- 10.11.2
		-- MariaDB Connector JDBC 
			-- 3.0.7

## BDD SQL (GSB-RV.sql)

	### TABLES  :
		+-----------------+
		| Tables_in_gsbrv |
		+-----------------+
		| activitecompl   |
		| composant       |
		| connaitre       |
		| constituer      |
		| dosage          |
		| famille         |
		| formuler        |
		| interagir       |
		| inviter         |
		| laboratoire     |
		| medicament      |
		| motif           |
		| offrir          |
		| posseder        |
		| praticien       |
		| prescrire       |
		| presentation    |
		| rapportvisite   |
		| realiser        |
		| region          |
		| secteur         |
		| specialite      |
		| travailler      |
		| typeindividu    |
		| typepraticien   |
		| visiteur        |
		+-----------------+

## Requetes (modeleGsbRv.py)

	### Particularité :
		--GenererNumeroRapportVisite
			Entries : <> matricule
			Output : <> dernier numero de rapport + 1 

			Genere un nouveau numero de rapport pour le prochain rapport à enregistrer -> Appel par Ajout d'un rapport.
		
## App (appGsbRv.py (port=5000))

	!!!
	Entries sous forme de :
	 	String dans url <> 
	 	jsonObject dans body [] 
	 	JsonArray dans body [{}]

	Output sous forme de : 
		JsonObject []
		JsonArray [{}]
	!!!

	## CONNECTION

		--Connection utilisateur
			Entries : <> matricule, mdp
			Route : @app.route('/visiteur/<matricule>/<mdp>', methods=['GET'])
			Output : [matricule, nom, prenom]

	## RECUPERATION

		--Rapports visites
			Entries : <> matricule, mois, annee
			@app.route('/rapports/<matricule>/<mois>/<annee>', methods=['GET'])
			Output : [{rap_num, rap_dateVisi, rap_bilan, pra_nom, pra_prenom, pra_cp, pra_ville, rap_date_redaction, mot_libelle, rap_coefconfiance, rap_lu}]

		--Echantillons Offerts
			Entries : <> matricule, rapportNumero
			@app.route('/rapports/echantillons/<matricule>/<numRapport>', methods=['GET'])
			Output : [{med_nom_commercial, off_quantite, med_depotlegal}]

		--Praticiens
			Entries : 
			@app.route('/praticiens', methods=['GET'])
			Output : [{pra_num, pra_nom, pra_prenom, pra_ville, pra_cp}]
		
		--TOUS les medicaments
			Entries :
			@app.route('/medicaments', methods=['GET'])
			Output : [{med_depotlegal ,med_nomcommercial ,fam_code ,med_composition ,med_effets , med_contreindic}]

		--UN médicament
			Entries : <> med_depotLegal
			@app.route('/medicament/<depotlegal>', methods=['GET'])
			Output : [{med_depotlegal ,med_nomcommercial ,fam_code ,med_composition ,med_effets , med_contreindic}]

		--Motifs
			Entries : 
			@app.route('/motifs', methods=['GET'])
			Output : [{mot_num , mot_libelle}]

	## AJOUT

		--Ajout d'un rapport
			Entries : [] mat, praNum, motNum, dateVisite, dateRedac, bilan, coefConfiance
			@app.route('/ajouter/rapports', methods=['POST'])
			Output : [numRapport]

		--Ajout des echantillons offerts
			Entries : <> vis_matricule , rap_num , 
					  [{}] med_depotlegal , off_quantite 
			@app.route('/rapports/echantillons/<matricule>/<numRapport>', methods=['POST'])
			Output : [{nombreEchantillons}]






