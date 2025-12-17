# Guide d'installation 

## Prérequis

Avant de commencer, assurez-vous que votre environnement dispose des éléments suivants :

- **Java** : version 17 ou supérieure
- **Maven** : pour la gestion des dépendances et la compilation

### Dépendances optionnelles

Pour l'exploration et la construction de la base de données via les scripts Python fournis :

- **Python 3**
- **SQLite3**

> **Note** : Ces outils sont optionnels car la gestion de la base de données peut être effectuée directement depuis le code Java. Une dépendance SQLite est incluse dans le fichier `pom.xml`.

#### Installation des dépendances optionnelles

**Sous Linux :**
```bash
sudo apt-get install python3
sudo apt-get install sqlite3
```

**Sous macOS :**
- Téléchargez Python depuis le site officiel [python.org](https://www.python.org), puis cliquez sur « Download ».

- SQLite est généralement installé par défaut sur macOS. Vérifiez avec :
```bash
sqlite3 --version
```
---

## Compilation du projet

Pour compiler l'application, placez-vous à la racine du projet (là où se trouve le fichier `pom.xml`) et exécutez la commande suivante :

```bash
mvn compile
```

---

## Initialisation de la base de données

Vous disposez de deux méthodes pour initialiser et peupler la base de données.

### Méthode 1 : Initialisation via le code Java (recommandée)

1. Ouvrez le fichier `App.java`
2. Localisez les deux lignes suivantes (commentées par défaut) :
   ```java
   // SQLiteManager.initialize();
   // SQLiteManager.loadTestData();
   ```
3. Décommentez ces lignes lors de la **première exécution**
4. Après la première exécution, **recommentez-les** pour éviter que la base de données ne soit recréée à chaque démarrage

> **Emplacement** : La base de données sera créée dans un dossier `/db` à l'emplacement d'où vous lancez l'application.

### Méthode 2 : Initialisation via les scripts Python

Si vous avez installé Python 3 et SQLite3, suivez ces étapes :

1. Placez-vous dans le répertoire `projet-gl/src/main/resources`
2. Exécutez les commandes suivantes :
   ```bash
   python3 install_bd.py
   python3 populate.py
   ```

> **Important** : La base de données sera créée dans `/resources/db`. Vous devrez déplacer le dossier `/db` à la racine du projet (là où vous lancez l'application) pour que celle-ci puisse y accéder.

---

## Exécution de l'application

Deux options sont disponibles pour lancer l'application :

### Option 1 : Avec Maven
```bash
mvn compile exec:java
```

### Option 2 : Exécution directe
Lancez directement le fichier `App.java` depuis votre IDE.

---

## Commandes utiles

| Action | Commande |
|--------|----------|
| Nettoyer le projet | `mvn clean` |
| Compiler le projet | `mvn compile` |
| Exécuter le projet | `mvn compile exec:java` |

---

## Support

Pour toute question ou problème rencontré lors de l'installation ou de l'utilisation, n'hésitez pas à consulter la documentation du projet ou à contacter l'équipe de développement.