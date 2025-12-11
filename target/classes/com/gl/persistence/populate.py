import sqlite3
import os
import sys

# Configuration
DB_NAME = "jeu_de_role.db"
SQL_FILE = "populate.sql"

def load_test_data():
    print("Chargement des données de test...")

    # 1. Localiser le fichier SQL
    # On cherche le fichier dans le même dossier que ce script python
    base_dir = os.path.dirname(os.path.abspath(__file__))
    sql_path = os.path.join(base_dir, SQL_FILE)

    if not os.path.exists(sql_path):
        print(f"Erreur : Fichier {SQL_FILE} introuvable à l'emplacement : {sql_path}", file=sys.stderr)
        return

    # 2. Lire le contenu du fichier SQL
    try:
        with open(sql_path, 'r', encoding='utf-8') as f:
            sql_script = f.read()
    except IOError as e:
        print(f"Erreur lors de la lecture du fichier : {e}", file=sys.stderr)
        return

    conn = None
    try:
        # 3. Connexion à la base de données
        conn = sqlite3.connect(DB_NAME)
        cursor = conn.cursor()

        # Activer les foreign keys (important pour SQLite)
        # cursor.execute("PRAGMA foreign_keys = ON;")

        # 4. Exécution du script
        # Note : 'executescript' est spécifique à Python pour exécuter plusieurs instructions SQL d'un coup
        cursor.executescript(sql_script)
        
        # Valider les changements
        conn.commit()
        print("Données insérées avec succès !")

    except sqlite3.IntegrityError as e:
        # Correspond à votre bloc catch spécifique pour les contraintes uniques
        error_msg = str(e)
        if "UNIQUE constraint failed" in error_msg or "is not unique" in error_msg:
            print("Info : Les données semblent déjà présentes (Contrainte d'unicité déclenchée).")
        else:
            print(f"Erreur d'intégrité : {e}", file=sys.stderr)
            
    except sqlite3.Error as e:
        # Autres erreurs SQL génériques
        print(f"Erreur SQL inattendue : {e}", file=sys.stderr)

    finally:
        # 5. Fermeture propre
        if conn:
            conn.close()

if __name__ == "__main__":
    load_test_data()