# 🧩 Jeu de Taquin en Java 🧩 


Ce projet est un jeu de taquin implémenté en Java. Le jeu consiste en un plateau de tuiles numérotées, qui doivent être déplacées pour les remettre dans l'ordre correct. Le jeu comporte plusieurs niveaux de difficulté et peut être personnalisé avec des images personnalisées.

## 🚀 Installation


Pour exécuter le jeu, vous aurez besoin des éléments suivants :

Java 8 ou supérieur
Pour exécuter le jeu, vous pouvez télécharger le fichier JAR exécutable à partir de la page des versions du projet sur GitHub. Une fois le fichier téléchargé, vous pouvez exécuter le jeu en ouvrant un terminal et en exécutant la commande suivante :

java --module-path /path/to/javafx/lib --add-modules=javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web  -jar TaquinFX.jar com.monpackage.TaquinFX

Exemple : java --module-path "/data/Documents/ING1/Java/javafx-sdk-17.0.6/lib" --add-modules=javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web  -jar TaquinFX.jar com.monpackage.TaquinFX

Testé avec :

openjdk version "11.0.18" 2023-01-17

OpenJDK Runtime Environment (build 11.0.18+10-post-Ubuntu-0ubuntu120.04.1)

OpenJDK 64-Bit Server VM (build 11.0.18+10-post-Ubuntu-0ubuntu120.04.1, mixed mode, sharing)

## 🎮 Utilisation

Lorsque l'exécutable est lancé, l'utilisateur arrive sur un menu où il peut soit se créer une nouvelle sauvegarde, en rentrant par exemple son prénom, soit continué sur une sauvegarde qui existe déjà.

Si vous n'avez pas choisi de sauvegarde, un message apparaîtra vous demandant d'en choisir une. 

Suite au choix de la sauvegarde, vous aurez accès à notre carte où se trouvent les différents niveaux concoctés par nos soins. 
Si un niveau ne vous ai pas accessible parce que vous n'avez pas débloqué ceux d'avant alors un message vous l'indiquera.


Lorsque que l'utilisateur choisit un niveau auquel il peut avoir accès, il pourra voir ses précédents meilleurs scores et meilleurs temps et il verra le nombre de coups actuel et son temps de la partie qu'il est en train de jouer.

Pendant 5 secondes, le taquin résolu apparaîtra.

Pour déplacer une case, il suffit de cliquer sur la case "classique" (contenant une valeur) que la personne souhaite échanger avec la case vide. Si la case choisit à plusieurs cases vides adjacentes alors un pop-up apparaîtra pour qu'il puisse choisir la case vide avec laquelle il souhaite échanger la case "classique".

Une fois terminé un pop-up apparaît et affiche le score du joueur et met à jour sa sauvegarde. Si le joueur souhaite utiliser le solver, il aura le choix entre une version automatique où les coups défileront à une certaine allure, et une autre option où le joueur pourra faire défiler lui-même les étapes, et même revenir en arrière.

## 🤝 Contribution


Si vous souhaitez contribuer à ce projet, vous pouvez soumettre des demandes de pull sur GitHub. Nous sommes toujours à la recherche de nouvelles fonctionnalités et de corrections de bugs pour améliorer notre jeu de taquin.

## 📝 Licence


Ce projet est sous aucune license pour le moment. 

Nous espérons que vous apprécierez notre jeu de taquin et n'hésitez pas à nous contacter si vous avez des questions ou des commentaires. 😊
