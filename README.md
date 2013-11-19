ProxyServer Groupe 07  PAULUS Aloïs - HABIMANA Ishak
-----------------------------------------------------

How to compile it :

- go to src folder
- run command : javac  -sourcepath . server/main/Proxy.java 

How to run it :

- go to src folder
- run command : java -cp . server.main.Proxy port

port is optionnal, default port is 1111

Example :

java -cp . server.main.Proxy
java -cp . server.main.Proxy 1234


Tested with :

Browser : Mozilla Firefox

- http://www.perdu.com
- http://www.aloispaulus.be
- http://www.w3.org/Protocols/rfc2616/rfc2616.html
- http://www.google.be/

Conception :

1) Fonctionnalité :

- Compatible HTTP 1.0 et 1.1
- Gère les connection persistente et non persistente
- Gère les packet chunked
- Compatible multi client
- Compatible avec Apache/Nginx et d'autres serveurs web
- Dispose d'une cache pour les pages déjà visitée

2) les requêtes :

Pour chaque requête d'un client(browser) le proxy effectue les actions suivantes :
- crée un nouveau thread
- analyse la requête
- construit une nouvelle requête sur base de celle analysée
- rnvoie cette requête au serveur
- recupère la réponse
- renvoie cette réponse au client(browser)

J'ai choisi cette approche car elle me permet de bien contrôller les requêtes envoyées au serveur et les réponses renvoyées par le proxy au client.

3) La cache :

La cache correspond à une HashMap qui a comme clé, l'URL et comme valeur, la réponse.
J'ai choisi cette méthode car elle est facile à mettre en place et efficace pour les performances grace à la HashMap et son acces directe.


