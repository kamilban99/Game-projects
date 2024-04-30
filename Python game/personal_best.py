import json

class PersonalBest:
    """
    Klasa, dzięki której możemy trzymać dla każdej z trudności z obu gier  nasz "personal best"
    (dla nas to będzie najmniejsza ilość kliknięć "check" podczas naszej gry)

    Nasze wyniki będziemy trzymać w pliku JSON, dzięki czemu w dość łatwy sposób możemy przeglądać plik i znaleźć szukane informacje
    """
    def __init__(self,game,difficulty):
        self.game = game
        self.difficulty = difficulty
        self.path = "pb.json"
        self.personal_best = None
        self.read()
    
    def save(self,score):
        """
        Zapisujemy nasz nowy wynik tylko wtedy, kiedy jest lepszy od personal best 
        """
        if score < self.personal_best or self.personal_best == -1:
            f = open(self.path, "w")
            self.data[self.game][self.difficulty] = score
            self.personal_best = score
            f.write(json.dumps(self.data))
            f.close()

    def read(self):
        """
        Otwieramy JSON i odczytujemy wymagany wynik 
        """
        if self.personal_best is None:
            f = open(self.path, "r")
            self.data = json.loads(f.read())
            f.close()
            self.personal_best = self.data[str(self.game)][str(self.difficulty)]

        return self.personal_best




