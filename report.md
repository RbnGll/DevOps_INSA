# Report 
Ce fichier synthétise les modifications que nous avons apporté au projet de base.

## Présentation
Nous avons présenté l'article *State of mutation testing at google*, notre présentation se trouve à la racine du projet.

## Code

Le code source est disponible sur Gitlab à l'adresse suivante : https://gitlab.insa-rennes.fr/Robin.Guill/devops-spoon-web.git

## Tests
### Backend 

**Coverage** : 

Pour tester le backend, nous avons définis les classes de test `TestTreeLevel`, `TestSpoonTreeCmdBase`, `TestSpoonASTImpl`, `TestCodeDTO` et `TestSpoonResource`. 

Avec Jacoco comme outil de couverture, nous obtenons les résultats suivants : 
 - 91% d'instructions couvertes ,
 - 87% de branches couvertes.
 Un rapport plus détaillé est disponible dans le dossier target/site/jacoco/index.html après un build réussi. (`mvn clean package`)
 Pour s'assurer une couverture satisfaisante nous avons défini un seuil minimum de 60% de couverture d'insructions et toutes les classes exceptées la classe Main couverte.
 To ensure code coverage we defined a minimum of 60% line coverage and 0 classes not testedin the pom.xml file. 
 
**Code style** :
 
- Checkstyle avec la configuration checktyle.xml
- Spotbugs en excluant les bugs définis dans le fichier spotbugs-exclude.xml
- Errorprone à la compilation, défini dans le fichier pom.xml

**Test par mutation**:
Avec le moteur de mutation extreme Descartes, nous obtenons un taux de mutants tués de 48%.
### Frontend
 
## Build
### Backend
A l'aide de Maven nous avons défini dans le fichier pom.xml l'enchainement d'opérations pour compiler le backend.  
On compile le backend avec la commande `mvn clean package`, le code est alors compilé et vérifié via errorprone. Les tests sont ensuite lancés, puis les tests par mutation. Ensuite nous avons utilisé le plugin shade pour créer une archive *.jar* rassemblant le backend et toutes ses dépendances. Le plugin assembly permet également d'exporter les fichiers sources. 

Tout cela est défini dans le fichier *pom.xml*

## Package
A la racine de chaque sous partie (spoon-backend et spoon-frontend) se trouvent des fichier Dockerfile permettant de construire des images docker pour faire tourner le backend et le frontend séparément (micro-services).
## Deploy
A chaque fois que le build réussit, les images construites sont poussées vers la Docker Registry de l'INSA Rennes.

## Run en local
Il y a plusieurs manières pour faire tourner l'application en local. 
- Soit en téléchargeant le dossier du projet et en lançant la commande `docker-compose up`
- Soit en téléchargeant les deux images docker et en les lançant séparément : 
   - `docker pull gitlab.insa-rennes.fr:5050/robin.guill/devops-spoon-web:spoon-backend-latest` 
   - `docker pull gitlab.insa-rennes.fr:5050/robin.guill/devops-spoon-web:spoon-frontend-latest`
   - `docker run -d --name=spoon-backend gitlab.insa-rennes.fr:5050/robin.guill/devops-spoon-web:spoon-backend-latest`
   - `docker run -d --name=spoon-frontend --network=host gitlab.insa-rennes.fr:5050/robin.guill/devops-spoon-web:spoon-frontend-latest`
   
 L'application est alors accessible à l'adresse http://localhost:8080/
## Approfondissements
Nous avons tenté d'utiliser l'outil d'analyse de code de GitLab CI intitulé code_quality intégré au pipeline d'intégration continue, mais ce dernier nécessitait une configuration spécifique que nous n'avons pas pu mettre en place.

Pour approfondir le côté Ops du projet nous avons décidé de déployer l'application. 
Pour cela nous avons crée un compte google et déployé l'application sur Google Kubernetes Engine (GKE), la plateforme dédiée à Kubernetes de Google Cloud Provider. 
A l'aide des images Docker nous avons alors déployé l'application en tant que micro-services sur Kubernetes et exposé le frontend à l'aide d'un service spécifique nommé LoadBalancer. 
Ce dernier permet d'obtenir une adresse IP publique pour un service. 

Nous avons ensuite acheté le domaine spoon-online.fr sur Ionos et créé un enregistrement A pour faire pointer le domaine vers l'adresse IP du service frontend. 
Notre application est donc publiquement accessible : www.spoon-online.fr

## Monitor
Avec notre application déployée sur GKE, il est très facile de surveiller les données de l'applicaton en temps réel avec leur outil Cloud Monitoring. En théorie le A/B testing est également relativement facile à mettre en place à l'aide du LoadBalancer service, malheureusement nous n'avons pas pu le tester.
 

