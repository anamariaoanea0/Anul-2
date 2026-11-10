Lab 4



1\. Tabel inainte/dupa pentru 5 bucle inlocuite



|Clasa|Inainte|Dupa|
|-|-|-|
|UserService, metoda removeUser()|Bucla 'for-each' pentru a gasi si adauga in lista prieteniile utilizatorului ce urmeaza a fi sters.| S-a folosit Stream API cu operatiile '.filter()' (pentru a gasi prieteniile in care este implicat user-ul) si '.map()' (pentru a extrage ID-ul prieteniei), rezultatul fiind colectat cu '.collect(Collectors.toList())'.|
|CommunityService, metoda getAllCommunities()|Bucla 'for-each' peste lista de utilizatori pentru a initia cautarea DFS doar pentru nodurile nevizitate.|S-au folosit operatiile '.map()' (pentru a extrage ID-ul), '.filter()' (pentru a selecta doar nodurile nevizitate), un alt '.map()' (pentru a apela DFS si a construi comunitatea) si terminalul '.collect(Collectors.toList())'.|
| CommunityService, metoda getMostSociableCommunity()|Doua bucle 'for-each' imbricate pentru a parcurge fiecare comunitate si fiecare nod, comparand manual lungimea maxima a drumului cu variabile ajutatoare.| S-au folosit operatiile '.stream()' si '.max(Comparator.comparingInt(...))' atat pentru a gasi maximul in interiorul unei comunitati, cat si pentru a gasi comunitatea cu drumul cel mai lung.|
|ConsoleUI, metoda handlePrintUsers() (si celelalte metode de afisare)| Bucla clasica 'for-each' pentru a parcurge lista de utilizatori/prietenii/membri primita de la Service si a le afisa la consola.|S-a apelat metoda default '.forEach()' pe colectie, impreuna cu o expresie lambda pentru parsarea si afisarea datelor.|
|AbstractFileRepository, metoda writeToFile()| Bucla 'for-each' care parcurgea entitatile din memorie si apela BufferedWriter pentru a scrie linie cu linie.|S-a folosit Stream API peste entitati, cu '.map(this::createEntityAsString)', terminalul '.collect(Collectors.toList())', iar scrierea s-a facut functional printr-un singur apel 'Files.write()'.|



2\. Interfete functionale folosite si unde apar



Predicate: Folosita in interfata 'Repository' pentru metoda default 'findAll(Predicate filter)' si implementata ca referinta la metoda statica ('CriteriaUser::esteMajor' sau 'Objects::nonNull') in metodele de filtrare din Service si UI.



Function<T, R>: Folosita masiv in clasa 'RepositoryFactory'. Map-ul care creeaza instante de repository asociaza fiecarei chei (RepositoryType) o interfata 'Function<Validator, Repository<ID, E>>', inlocuind vechiul switch sau Supplier fara parametri.



Consumer: Folosita implicit ca argument pentru metodele terminale '.forEach()' (ex: 'friendshipsToDelete.forEach(friendshipRepo::delete)' sau afisarile din 'ConsoleUI').



Comparator: Folosita pentru determinarea celei mai sociabile comunitati in stream, folosind metoda ajutatoare 'Comparator.comparingInt(...)'.



3\. Justificarea trecerii la Optional in interfata Repository



Am ales ca interfata Repository sa intoarca 'Optional' (in loc de 'null') pentru metodele 'findOne', 'save' si 'delete'.



Aceasta decizie respecta standardele moderne de programare in Java 8 si asigura o scriere mult mai robusta a codului. Fortand stratul de Service sa extraga valoarea returnata (folosind metode precum '.orElseThrow()'), eliminam riscul ascuns de a primi un 'NullPointerException' la rulare, iar verificarile de existenta a entitatilor devin mult mai expresive.



4\. Justificarea pastrarii Template Method in AbstractFileRepository



Am decis sa pastrez abordarea bazata pe "Template Method" (prin clasa 'AbstractFileRepository' si mostenire pentru 'UserFileRepository' / 'FriendshipFileRepository') in loc sa folosesc compozitia cu interfața 'Function<String, E>'.



Motivul este respectarea principiului Single Responsibility. Daca as fi folosit Function direct in constructor, clasa 'RepositoryFactory' ar fi devenit extrem de aglomerata si greu de citit, deoarece ar fi trebuit sa contina pe langa datele de configurare si toata logica de parsare/formatare (split, parsare date, validare conversii) a string-urilor din fisiere. Prin Template Method, logica specifica fiecarei entitati ramane perfect incapsulata in clasa ei dedicata.



5\. Exceptii de la regula: Bucle clasice pastrate



Unde: Bucla 'for-each' din metoda 'longestPathFromNode' (clasa 'CommunityService').



Justificare: Aceasta metoda implementeaza un algoritm de Backtracking folosit pentru gasirea celui mai lung drum simplu (cea mai sociabila comunitate). Algoritmul presupune apeluri recursive si modificari constante ale starii (adaugarea si stergerea manuala a nodurilor dintr-o lista de vizitate: 'pathVisited.add()' si 'pathVisited.remove()'). Incercarea de a scrie acest algoritm recursiv cu stare mutabila folosind Stream API ar duce la un cod nefiresc, greu de depanat si care ar incalca principiile de baza ale programarii pur functionale. Buclele raman varianta optima pentru lizibilitate in cazul backtracking-ului.

