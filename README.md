# 🧩 Jeu de Taquin en Java 🧩 


Ce projet est un jeu de taquin implémenté en Java. Le jeu consiste en un plateau de tuiles numérotées, qui doivent être déplacées pour les remettre dans l'ordre correct. Le jeu comporte plusieurs niveaux de difficulté et peut être personnalisé avec des images personnalisées.

## 🚀 Installation


Pour exécuter le jeu, vous aurez besoin des éléments suivants :

!!!! Nous avons tous recommencer pour l'instant la commande __java -jar TaquinFX.jar__ suffit !!!!!!



Java 8 ou supérieur
Pour exécuter le jeu, vous pouvez télécharger le fichier JAR exécutable à partir de la page des versions du projet sur GitHub. Une fois le fichier téléchargé, vous pouvez exécuter le jeu en ouvrant un terminal et en exécutant la commande suivante :

java --module-path /path/to/javafx/lib --add-modules=javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web  -jar TaquinFX.jar com.monpackage.TaquinFX

Exemple : java --module-path "/data/Documents/ING1/Java/javafx-sdk-17.0.6/lib" --add-modules=javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web  -jar TaquinFX.jar com.monpackage.TaquinFX

Testé avec :

openjdk version "11.0.18" 2023-01-17

OpenJDK Runtime Environment (build 11.0.18+10-post-Ubuntu-0ubuntu120.04.1)

OpenJDK 64-Bit Server VM (build 11.0.18+10-post-Ubuntu-0ubuntu120.04.1, mixed mode, sharing)

## 🎮 Utilisation


Une fois le jeu lancé, vous pouvez sélectionner le niveau de difficulté souhaité ou charger une image personnalisée. Pour déplacer les tuiles, cliquez sur la tuile que vous souhaitez déplacer. Si la tuile peut être déplacée, elle se déplacera automatiquement à sa nouvelle position. Continuez à déplacer les tuiles jusqu'à ce que vous ayez remis le puzzle dans l'ordre correct.

## 🤝 Contribution


Si vous souhaitez contribuer à ce projet, vous pouvez soumettre des demandes de pull sur GitHub. Nous sommes toujours à la recherche de nouvelles fonctionnalités et de corrections de bugs pour améliorer notre jeu de taquin.

## 📝 Licence


Ce projet est sous aucune license pour le moment. Voir le fichier __LICENSE__ pour plus de détails.

Nous espérons que vous apprécierez notre jeu de taquin et n'hésitez pas à nous contacter si vous avez des questions ou des commentaires. 😊

## Taches à faire :

- [X] Avoir une équipe de BG :grin:

 Cahier des Charges : 
 - [ ] differents niveaux (au moins 10) 
 - [ ] chaque niveau : au moins une case vide (ou + selon difficulté)
 - [ ] des niveaux ont des cases inexistantes ou indestructibles 
 - [ ] afficher le niveau resolu 
 - [ ] melange des cases : soit tout aleatoire soit qql case aleatoire (et afficher le type de melange)
 - [ ] aucune case sur sa position de depart (meme si melangé aleatoirement) sinon afficher un message 
 - [ ] detecter si le niveau est jouable et afficher une information 
 - [ ] deplacer les cases une par une, le compteur sera incrémenté 
 - [ ] sauvegarder les nombres de coup a la fin d'un niveau pour afficher le meilleur score 
 - [ ] mettre un oeuvre un systeme de deblocage de niveau (finir niveau 1 pour passer au niveau 2) 
 - [ ] le joueur peut : demander de resoudre le niveau automatiquement et afficher chaque etape de la solution !!! niveau non debloqué !!!
 - [ ] resolution automatique : faire defiler les étapes automatiquement ou utiliser un bouton pour chaque etape (choisir la vitesse) 
