Lab 5

Justificarea stergerii prieteniilor in Service

Am ales varianta stergerii explicite a prieteniilor in Service. Motivul este ca proiectul trebuie sa suporte comutarea dinamica intre DATABASE, FILE si MEMORY. Daca m-as fi bazat pe ON DELETE CASCADE din PostgreSQL, la comutarea pe FILE sau MEMORY prieteniile nu ar mai fi fost sterse odata cu utilizatorul. Stergerea explicita in Service garanteaza un comportament uniform pemtru toate cele 3 medii de stocare.