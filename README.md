# Les dépendances necessaires

- java v>17
- Maven

*Accessoirement **sqlite3** et **python 3** pour explorer et construire la bd via les scripts python fournis bien que cela soit possible directement dans le code java, une dépendance sqlite étant inclus dans le pom.xml*

**Pour installer python 3 et sqlite3**
- Sur Linux
> ```sudo apt-get install python3```
> ```sudo apt-get install sqlite3```

- Sur MacOs
> rendez-vous sur le site officiel python.org. Ensuite, vous allez cliquer sur « Download »
>


# Compiler l'application

À la racine du projet (ou se trouve le fichier pom.xml), lancer la commande : ```mvn compile```

# Excécuter l'application

## Initialiser et peupler la base de donnée

Vous pouvez le faire de deux manières : 

> METHODE RECOMMENDÉE
**Directement dans le code java**
Dans le fichier *App.java*, vous avez deux lignes de code commentées : 
- SQLiteManager.initialize();
- SQLiteManager.loadTestData();

Déommenter les les lors de la première excécution de code puis recommenter les (Sinon, le bd sera détruite et reconstruite à chaque démarrage de l'application)

La DB sera crée dans un dossier /db au niveau d'où vous lancez l'application.

**Grâce aux scripts python fournis**
Si vous avez python3 et sqlite3 installé, vous pouvez initialiser la base de donnée et la peupler en vous plaçant dans le dossier projet-gl/src/main/resources puis en lançant : ```python3 install_bd.py``` puis ```python3 populate.py```

La bd sera crée dans le dossier /resources/db et il sera necessaire de déplacer le dossier /db au niveau de la racine d'où vous lancer l'application afin que l'application puisse la lire.

## Lancer l'application

Vous pouvez le faire de deux manières : 
- En utilisant maven : ```mvn compile exec:java```
- En lançant directement le fichier *App.java*



# Commandes utiles
nettoyer le projet : ```mvn clean```
reconstruire le projet : ```mvn compile```
executer le projet ```mvn compile exec:java```