# ProjetIHM OBIS 3D 🌎

Le but du projet est de réaliser une application permettant de visualiser ces observations sur un globe terrestre
en 3D. Cette application devra permettre de visualiser pour une espèce donnée le nombre d’observations par
zone géographique, d’abord à partir d’un fichier, puis en faisant des requêtes à cette base de données.

## Fonctionnalité
* On peut afficher un globe en 3D et on permet à l’utilisateur de tourner autour grâce à la souris. 
<img src="https://user-images.githubusercontent.com/98128042/177739370-869289fe-c2f2-4e3b-b3a8-00548a35d840.png"  height="300">

* On permet à l'utilisateur d’afficher les occurrences d’une espèce en passant le nom scientifique exact saisi par l’utilisateur. Si le nom n’est pas reconnu, alors l'autocomplétion aidera l'utilisateur à prendre son choix avec la combobox
<img src="https://user-images.githubusercontent.com/98128042/177740111-5b4c91ad-6e13-40ee-a5e7-6bb957746682.png"  height="300">

* on donne a l'utilisateur la possibilité de
saisir deux années pour n’afficher que
le nombre d'occurrences d’une espèce
entre ces deux années
<img src="https://user-images.githubusercontent.com/98128042/177740763-4f05f23b-9438-4353-adba-e5818253f997.png"  height="300">

* Avec Ctrl + double clic sur n’importe
quel endroit sur la map cela permet
d'afficher les signalements

<em><img src="https://user-images.githubusercontent.com/98128042/177741466-25cc1aa0-672d-4fc3-a713-62398b7076ef.png"  height="300"></em>

* Avec Shift + double clic sur la map on
permet à l'utilisateur d'afficher les
listes d'espèce dans cet emplacement

<img src="https://user-images.githubusercontent.com/98128042/177742078-68d3db9c-8694-40d3-a396-fdceb2cecc69.png"  height="300">

* Histogramme 3D, dont la hauteur et
la couleur sera fonction des nombres
d’occurrences de l’espèce

<img src="https://user-images.githubusercontent.com/98128042/177742704-0fae62a4-d8f9-4cc7-8244-f92e17370b8e.png"  height="300">



## À améliorer
* Afficher l’
évolution du nombre de signalements
d’une espèce entre deux années à
l’aide d’une interface permettant de
démarrer, mettre en pause et stopper

* Optimisation de l'interface graphique afin de mieux visualiser les Listes des especes et les listes des signalements.
* Amelioration des requetes pour chercher que les information demandee afin d'optimiser le temps d'attente de l'utilisateur.


