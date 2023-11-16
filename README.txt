# Tennis Application

## Introduction
Ce document décrit comment configurer, exécuter et tester l'application Tennis Application]. Cette application est un service RESTful développé avec Spring Boot pour gérer des informations sur les joueurs de tennis.


## Prérequis
- Java JDK 11 ou supérieur
- Maven 3.6 ou supérieur
- Un compte Google Cloud Platform (pour le déploiement)


## Configuration
Placez votre fichier JSON dans le dossier src/main/resources de votre projet.
Configurez le nom du fichier JSON dans le fichier application.properties de votre projet. Par exemple:
json.file.name=votre_nom_de_fichier.json
Si ce champ n'est pas défini, l'application utilisera headtohead.json par défaut.


## Lancement de l'application
Pour exécuter l'application localement, utilisez la commande suivante :
mvn spring-boot:run


## Tests
Pour exécuter les tests, utilisez la commande suivante :
mvn test


## Utilisation de l'API
L'application expose plusieurs endpoints RESTful. Voici quelques exemples d'utilisation :

Obtenir tous les joueurs : GET /api/players
Obtenir un joueur par ID : GET /api/players/{id}
Obtenir des statistiques : GET /api/statistics

Ce dernier endpoint retourne des statistiques telles que le pays avec le plus grand ratio de parties gagnées, l'IMC moyen des joueurs, et la médiane de la taille des joueurs.


## Contact
Pour toute question ou suggestion, veuillez contacter Ksenia Shults à ksenija.schulz@gmail.com.