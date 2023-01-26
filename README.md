# Space Lab 13

Klasyczny rougelike, tyle że w kosmosie. Bohater budzi się na stacji kosmicznej wypełnionej masą kosmitów i potworów powstałych w wyniku eksperymentów, ma za zadanie naprawić generator znajdujący się na najniższym pokładzie stacji i uciec żywy. Inspirowany Obcym, Predatorem, Blobem itd.

### Użyte technologie
* Java
* [libGDX](https://libgdx.com/)

### Założenia programistyczne
* **Permadeath** - łatwe do zaprogramowania i dodaje do klimatu kosmicznego horroru
* **Losowa generacja poziomów** - urozmaicenie rozgrywki, gracz musi znaleźć przejście do następnego poziomu
* **Eksploracja i odkrywanie** - gracz jest zachęcany do eksploracji poprzez losowe rozłożenie cennych przedmiotów na mapie
* **Mapa oparta o kwadratową siatkę** - łatwe do zaprograowania i ułatwia prowadzenie walki
* **Turowość** - łatwe do zaprogramowania i daje graczowi okazję do przemyślenia swoich ruchów
* **Walka** - wręcz i dystansowa
* **Niemodalność** - jeden tryb gry
* **System ekwipunku i zarządzanie zasobami** - gracz posiada ograniczoną ilość miejsc w ekwipunku i musi decydować co ze sobą zabrać oraz w jakim momencie tego użyć
* **Jedna postać na jednego gracza** - łatwe do zaprogramowania i dodaje do klimatu horroru
* **Grafika ASCII pomieszana z graficznymi elementami GUI** - łączy klimatyczny, klasyczny wygląd rougelike'a z przejrzystością nowszych gier
* **Zamknięte przestrzenie** - wymusza na graczu radzenie sobie z przeciwnikami poprzez walkę lub umiejętne ich omijanie, zamiast ciągłe uciekanie
* **Statystyki postaci w postaci liczbowej** - jasne określenie tego w czym postać jest dobra, daje poczucie postępu 
* **Różnorodni przeciwnicy** - przeciwnicy o różnych zachowaniach i słabościach, wchodzący też w interakcje między sobą, dodają do klimatu i są większy wyzwaniem taktycznym

### Interfejs
[Interfejs gry](https://i.imgur.com/JBhjzHY.png)

### Sterowanie
Numpad - poruszanie się po mapie
Tab - otwarcie ekwipunku/ekranu statystyk i rozwoju

### Gameplay
#### Statystyki postaci
##### Podstawowe
* **Health Points (HP)** - fizyczne zdrowie postaci. Postać traci je podczas otrzymywania obrażeń w walce. Kiedy się skończy, postać umiera.
* **Stress Points (SP)** - psychiczne zdrowie postaci. Postać otrzymuje je podczas otrzymywania obrażeń w walce oraz w innych nieprzyjaznych warunkach. Kiedy przekroczy limit, postać wpada w panikę (tymczasowy efekt zmieniający statystyki) i otrzymuje jedną Traumę. Kiedy postać skończy panikować, limit SP zwiększa się.
* **Traumy (TR)** - trwałe debuffy do statystyk.
* **Creditsy (CR)** - waluta do wydawania w sklepach.
* **Poziom (LVL)/Expirience Points (EXP)** - interaktując ze światem, postać zdobywa EXP. Po zdobyciu odpowiedniej ilości, postać osiąga nowy LVL i może ulepszyć swoje umiejętności.
##### Umiejętności
###### Walka (Fighting):
* **Walka wręcz (Melee)**
* **Walka dystansowa (Ranged)**
* **Siła woli (Willpower)** - zwiększa limit SP
###### Praca (Labor)
* **Wytrzymałość (Toughness)** - zwiększa maksymalną ilość HP
* **Siła (Strength)** - zwiększa udźwig postaci, pozwalając na noszenie większej ilości przedmiotów
* **Majsterkowanie (Tinkering)** - pozwala na wytwarzanie i naprawianie przedmiotów
###### Nauka (Science)
* **Identyfikacja (Identifying)** - znajomość obsługi i przeznaczenia przedmiotów
* **Hakowanie (Hacking)** - wpływanie na środowisko stacji kosmicznej za pomocą terminali
* **Negocjacja (Negotiation)** - wpływa na interakcje z przyjaznymi NPC
