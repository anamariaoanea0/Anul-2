1. Cum functioneaza Factory-ul si ce trebuie schimbat pentru trecerea la baze de date:

Clasa RepositoryFactory centralizeaza crearea instanțelor de acces la date. La prima apelare (prin sablonul Singleton), aceasta citeste setarile din fisierul "config.properties". Cand se solicita un repository, Factory-ul interogheaza proprietatea "repository.type" si, pe baza unui bloc switch, returneaza implementarea corecta (Memorie, Fisier sau Baza de Date) implementand automat detaliile necesare (nume fisier, URL, credentiale).
Trecerea la Baza de Date:
Datorita clasei RepositoryFactory, trecerea la baza de date nu necesita nicio modificare in codul sursa al claselor Main, UserService, FriendshipService sau CommunityService. Singura schimbare necesara este in fisierul config.properties:
-Se modifica cheia: repository.type=DATABASE;
-Se configureaza credentialele: db.url, db.user, db.password.
Odata repornita aplicatia, Factory-ul va directiona automat tot fluxul de date catre clasele UserDbRepository și FriendshipDbRepository.

2. Cum sunt tratate fisierele lipsa si liniile corupte:

Daca fisierul de configurare config.properties lipseste, programul semnaleaza o eroare la initializarea RepositoryFactory aruncand un RepositoryException. Daca un fisier de date (ex: users.txt) lipseste, Java arunca un IOException care este prins si transformat intr-un RepositoryException, informand utilizatorul fara a opri brusc executia.
Citirea datelor se face linie cu linie in metoda loadData() din AbstractFileRepository. Daca o linie are un numar gresit de campuri sau tipuri de date incorecte, metoda abstracta extractEntity din repository-urile concrete arunca o exceptie te tipul NumberFormatException transformat in RepositoryException. Aceasta exceptie este prinsa intr-un bloc catch specific in interiorul buclei while de citire. Eroarea este afisata in consola, iar linia corupta este pur si simplu ignorata. Aplicatia continua sa incarce restul liniilor valide.

3. Principii de Proiectare (SOLID) Utilizate:

Proiectul respecta urmatoarele principii SOLID:
-Single Responsibility Principle: Codul este impartit in module. Exista clase separate pentru validare (UserValidator), stocare (UserFileRepository), și logica de business. Gestiunea utilizatorilor este in UserService, gestiunea prieteniilor in FriendshipService, iar logica matematica a grafurilor in CommunityService.
-Open-Closed Principle: Clasa AbstractFileRepository este inchisa pentru modificare (logica de citire/scriere este deja definita) dar deschisa pentru extindere (se pot crea noi repository-uri pentru alte entitati implementand metodele extractEntity si createEntityAsString).
-Liskov Substitution Principle: Orice instanta de repository (InMemoryRepository, UserFileRepository, UserDbRepository) poate fi substituita in cadrul service-urilor fara ca acestea sa se blocheze sau sa funcționeze incorect, deoarece toate respecta interfata Repository.
-Dependency Inversion Principle: Clasele de tip Service (UserService, FriendshipService) depind de abstractiuni (interfata Repository<ID, T>), nu de implementari concrete. Astfel, Service-ul nu stie si nu este afectat de modul in care datele sunt efectiv stocate.

4. Sabloane de Proiectare (Design Patterns):

-Factory: Regasit in clasa RepositoryFactory, care ascunde logica complexa de instantiere a repository-urilor si ofera aplicatiei direct obiectele gata configurate.
-Template Method: Regasit in AbstractFileRepository. Metoda loadData() defineste scheletul de deschidere, citire linie cu linie si inchidere a fisierului, directionand pasul specific de parsare a textului catre metoda abstracta extractEntity(), implementata in subclasele UserFileRepository si FriendshipFileRepository.
-Strategy: Regasit in modul in care este implementata validarea. Interfata Validator<T> defineste o strategie de validare. Diferite strategii (UserValidator, FriendshipValidator) sunt introduse dinamic in repository-uri la momentul crearii acestora de catre Factory.
-Decorator: Acest sablon este folosit la lucrul cu fisiere. Un obiect simplu, care stie doar sa citeasca date de baza (precum FileReader), este "decorat" intr-un alt obiect (BufferedReader). Facand asta, aceste primeste o abilitate noua, si anume capacitatea de a citi textul usor, linie cu linie, fara sa fim nevoiti sa modificam codul obiectului initial.