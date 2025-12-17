import sqlite3
import os
import sys

# ===============================
# Configuration
# ===============================
DB_FOLDER = "db"
DB_NAME = "jeu_de_role.db"
SQL_FILE = "install_bd.sql"

# Utiliser os.path.join pour créer le chemin relatif "db/jeu_de_role.db"
DB_FULL_PATH = os.path.join(DB_FOLDER, DB_NAME)

def load_test_data():
    print("Création des tables...")

    # 1. Localiser le fichier SQL (Code rétabli)
    # Assurez-vous que les fichiers SQL sont dans le même répertoire que ce script Python.
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
        # AJOUT : Assurer l'existence du dossier 'db'
        if not os.path.exists(DB_FOLDER):
            os.makedirs(DB_FOLDER)
            print(f"Dossier de base de données créé : {DB_FOLDER}")
            
        # 3. Connexion à la base de données (Utilise DB_FULL_PATH = "db/jeu_de_role.db")
        conn = sqlite3.connect(DB_FULL_PATH)
        cursor = conn.cursor()

        # Activer les foreign keys (important pour SQLite)
        # cursor.execute("PRAGMA foreign_keys = ON;")

        # 4. Exécution du script
        cursor.executescript(sql_script)
        
        # Valider les changements
        conn.commit()
        print("Tables crées avec succès !")

    except sqlite3.IntegrityError as e:
        error_msg = str(e)
        if "UNIQUE constraint failed" in error_msg or "is not unique" in error_msg:
            print("Info : Les données semblent déjà présentes (Contrainte d'unicité déclenchée).")
        else:
            print(f"Erreur d'intégrité : {e}", file=sys.stderr)
            
    except sqlite3.Error as e:
        print(f"Erreur SQL inattendue : {e}", file=sys.stderr)

    finally:
        # 5. Fermeture propre
        if conn:
            conn.close()

if __name__ == "__main__":
    load_test_data()