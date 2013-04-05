import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main -> obsluguje wczytywanie pliku oraz wielosci tafli
 * zarzadza probami ulozenia klockow
 *
 * @author Michal Franczyk
 * @date 22.03.2013
 */
public class Main
{
    private static int SURFACE_SIZE;            // wielkosc planszy okreslona dlugoscia boku ...
    private static int TOTAL_SURFACE_SIZE;      // całkowita powierzchnia planszy
    private static int FILLED_AREA;             // pole powierzchni klocków ułożonych na planszy
    private static Boolean TEST = Boolean.TRUE; // test ...
    private static long DEADLINE;               //czas w jakis algorytm musi zakonczyc działanie
                                                // jeśli zostanie przekroczony to program konczy dzialanie bez wzgledu na ilosc ulozonych klockow

    public static void main(String[] args)
    {
        long startTime = 0; // czas startu programu, do testów ...
        int bestValue = 0;  // wartosc ilosci odpadow, najlepsza --> najmniejsza ze wszystkich 
        
        setDEADLINE(System.currentTimeMillis() + 225000); // (3.75*60000) -> deadline wykonania zadania 3.75 min

        if (Main.getTEST())
            startTime = System.currentTimeMillis();
        
        if (args.length < 2) // jesli zla ilosc argumentow wejsciowych to poinforuj o tym uzytkownika
        {
            System.err.println("BLAD, zla ilosc argumentow wejsciowych. Podaj <nazwa_pliku> <wielkosc_tafli>");
            System.err.println("W pliku powinny znajdowac sie pary liczb calkowitych (w oraz h)");
            System.exit(1);
        }

        try
        {
            Main.setSURFACE_SIZE(Integer.parseInt(args[1])); // czytanie z wejscia wielkosc tafli
        }
        catch (NumberFormatException e)
        {
            System.err.println("BLAD ZLY ROZMIAR" + e.getMessage());
        }

        // ustawianie poczatkowych wielkosci
        Main.setTOTAL_SURFACE_SIZE(Main.getSURFACE_SIZE() * Main.getSURFACE_SIZE());
        bestValue = Main.getTOTAL_SURFACE_SIZE();

        List<Rectangle> rectListOrigin = new ArrayList<>();

        File file = new File(args[0]); // użycie pliku wejsciowego do odczytu danych
        try
        {
            Scanner sc = new Scanner(file).useDelimiter("\\D+"); // pobieranie tylko liczb ...
            while (sc.hasNext())
            {
                Integer w = sc.nextInt();
                Integer h = sc.nextInt();

                rectListOrigin.add(new Rectangle(w, h)); // dodawanie prostokatow do listy
            }
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("BLAD Z PARSOWANIEM PLIKU WEJSCIOWEGO czy PLIK istnieje??? -> " + ex.getMessage());
        }
        catch (NoSuchElementException ex)
        {
            if (Main.getTEST())
            {
                System.err.println("BLAD ZLY FORMAT KRAWEDZI -> " + ex.getMessage());
            }
        }

        // obiekt odpowiadajacy za obsluge rozwiazania problemu
        Knapsack k = null;

        // czas po jakim spodziewamy sie skonczenia programu
        // jesli program przekroczy czas endTime to przy następnej iteracji przerwie działanie
        long endTime = System.currentTimeMillis() + 200000; // (3*60000), 3min

        // zmienna odpowidajaca za zmiane strategi kolejnosci ulozenia klockow w liscie
        // wiecej w switchShuffleCollections
        int localSortStrategy = 0;

        // zmienna odpowiedzialna za zmiane wielkosci po jakiej prostokaty beda sortowane
        // wiecej w wybierzTrybSortowania
        // 0 szerokosc, 1 wysokosc, 2 pole
        int localSortType = 0;
        
        // zmienna potrzebna do zerowania trybow sortowania
        // zerowanie nastepuje gdy zaprogramowane możliwości rozwiązania z sortowaniem zawiodły
        // wtedy program przerzuca się na losowanie kolejnosci układania klocków
        // 9 jest wartoscią stala, niezbyt wazna
        int localLimit = 9;     

        // wielkosc opisuje ilosc prob ułożeń klocków jakie zdolal wykonać progam podczas dzialania
        int localIloscProb = 0;
                
        while (true)    // wykonuj / probuj ulozyc prostokaty az 
        {
            if (System.currentTimeMillis() > endTime                    // jesli obecny czas jest wiekszy niz czas wyznaczony do zakonczenia to ..
                    || System.currentTimeMillis() > Main.getDEADLINE()  // lub jeśli przekroczyliśmy deadline (bardzo male pradopodobienstwo wystapienia)
                    || bestValue == 0)                                  // lub jesli znalezlismy optymalne rozwiazanie 
                break;                                                  // ... wtedy zakoncz petle

            if ( (localSortStrategy > localLimit)   // jesli przekroczylismy limit 
                    && (localLimit == 9) )          // lub jesli limit zostal zmieniony
            {                                       // wtedy:
                if (localSortType++ < 3)                // jesli jest mniejsze od 3
                    localSortStrategy = 0;              // zeruj
                else                                    // jesli nie
                    localLimit++;                       // zwieksz limit
            }
            else                                    // jesli nie 
                localSortType = 0;                  // zeruj typ sortowania, tak na prawde nie on juz znaczenia --> losowe wybieranie kolejnosci ale na potrzeby algorytmu jest 0

            Main.setFILLED_AREA(0);                 // za kazda proba 0 ilosc zajetej przez klocki powierzchni
            
            // stworz liste prostokatow, których kolejnosc okresla funkcja switchShuffleCollections
            List<Rectangle> rectListCopy = switchShuffleCollections(localSortStrategy++, rectListOrigin, wybierzTrybSortowania(localSortType));
            
            // przekaz kopie do obiektu 
            k = new Knapsack(rectListCopy);             
            
            // wylicz ilsoc odpadow, jako min z kazdej proby wykonania
            // pack() zwraca ilosc odpadow z obecnego ulozenia
            bestValue = Math.min(bestValue, k.pack());  
            
            localIloscProb++;
        }

        if (Main.getTEST())
        {
            System.out.println("Sort type: " + localSortType);
            System.out.println("Ilosc prob: " + localIloscProb);
            System.out.println("Czas wykonania programu: " + ((System.currentTimeMillis() - startTime) / 1000.0) + " sec");
        }

        System.out.println("Ilosc odpadow = " + bestValue);
    }

    /**
     * wybieranie sortowania sposrob 9 zaprogramowanych mozliwosci
     * 10 mozliwosc do juz tylko randomowa kolejnosc listy prostokatow
     * @param i typ sortownia
     * @param rectListOrigin lista do sortowania
     * @param ord po czym bedziemy sortowac
     * @return ulozona wybranych sposobem liste klockow
     */
    private static List<Rectangle> switchShuffleCollections(int i, List<Rectangle> rectListOrigin, Order ord)
    {
        List<Rectangle> rectListLocalCopy = new ArrayList<>(rectListOrigin);
        switch (i)
        {
            case 0:     // sortuj malejaco --> z tym algorytm pracuje najefektynej
                        Collections.sort(rectListLocalCopy, new DescRectComparator(ord));
                break;
            case 1:     // sortuj rosnaco
                Collections.sort(rectListLocalCopy, new AscRectComparator(ord));
                break;
            case 2:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 0, ord);
                break;
            case 3:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 1, ord);
                break;
            case 4:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                Collections.sort(rectListLocalCopy, new AscRectComparator(ord));
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 0, ord);
                break;
            case 5:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                Collections.sort(rectListLocalCopy, new DescRectComparator(ord));
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 0, ord);
                break;
            case 6:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                Collections.sort(rectListLocalCopy, new AscRectComparator(ord));
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 1, ord);
                break;
            case 7:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                Collections.sort(rectListLocalCopy, new DescRectComparator(ord));
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 1, ord);
                break;
            case 8:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                Collections.shuffle(rectListLocalCopy);
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 0, ord);
                break;
            case 9:     // sortuj "polowicznie" ... wiecej info w rectListSorter
                Collections.shuffle(rectListLocalCopy);
                rectListLocalCopy = rectListSorter(rectListLocalCopy, 1, ord);
                break;
            default:    // losowanie kolejnosci prostokatow
                Collections.shuffle(rectListLocalCopy);
                break;
        }
        return rectListLocalCopy;
    }

    /**
     * wybiera po czym bedziemy sortowac klocki
     * @param tryb tryb sortowania
     * @return po czym sortujemy
     */
    private static Order wybierzTrybSortowania(int tryb)
    {
        switch (tryb)
        {
            case 0:
                return Order.Width;
            case 1:
                return Order.Height;
            case 2:
                return Order.Area;
        }
        throw new RuntimeException("Natrafilem na zly case | wybierzTrybSortowania");
    }

    /**
     * "sprytniejszy" sposob sortowania
     * sprawdza sie przewaznie dla zbiorow ktorych calkowita powierzchnia jest blicka powierzchni planszy
     * @param rectList lista do ulozenia
     * @param caseNum zorpatrywany przypadek
     * @param ord po czym sortujemy
     * @return ulozona lista
     */
    private static List<Rectangle> rectListSorter(List<Rectangle> rectList, int caseNum, Order ord)
    {
        int tmpSize = rectList.size(); // kopia rozmiaru
        
        // podzial listy na dwie tymczasowe
        List<Rectangle> tmp1 = rectList.subList(0, tmpSize / 2);        
        List<Rectangle> tmp2 = rectList.subList(tmpSize / 2, tmpSize);

        switch (caseNum)
        {
            case 0:     // najpier sortuj malejaco a potem rosnaco
                Collections.sort(tmp1, new DescRectComparator(ord));
                Collections.sort(tmp2, new AscRectComparator(ord));
                break;
            case 1:     // odwrotnie do czas 0
                Collections.sort(tmp1, new AscRectComparator(ord));
                Collections.sort(tmp2, new DescRectComparator(ord));
                break;
        }

        tmp1.addAll(tmp2);
        return tmp1;
    }

    /**
     * @return zwraca dlugosc boku z jakiego jest zbudowana plansza
     */
    public static int getSURFACE_SIZE()
    {
        return SURFACE_SIZE;
    }

    /**
     * @param aSURFACE_SIZE ustawia dlugosc boku z jakiego jest zbudowana plansza the SURFACE_SIZE to set
     */
    public static void setSURFACE_SIZE(int aSURFACE_SIZE)
    {
        SURFACE_SIZE = aSURFACE_SIZE;
    }

    /**
     * @return zwraca test
     */
    public static Boolean getTEST()
    {
        return TEST;
    }

    /**
     * @return wielkosc zapelnionej powierzchni
     */
    public static int getFILLED_AREA()
    {
        return FILLED_AREA;
    }

    /**
     * @param aFILLED_AREA the FILLED_AREA to set
     */
    public static void setFILLED_AREA(int aFILLED_AREA)
    {
        FILLED_AREA = aFILLED_AREA;
    }

    /**
     * @return calkowita powierzchnie planszy
     */
    public static int getTOTAL_SURFACE_SIZE()
    {
        return TOTAL_SURFACE_SIZE;
    }

    /**
     * @param aTOTAL_SURFACE_SIZE the TOTAL_SURFACE_SIZE to set
     */
    public static void setTOTAL_SURFACE_SIZE(int aTOTAL_SURFACE_SIZE)
    {
        TOTAL_SURFACE_SIZE = aTOTAL_SURFACE_SIZE;
    }

    /**
     * @return czas w jakim program musi się zakonczyc
     */
    public static long getDEADLINE()
    {
        return DEADLINE;
    }

    /**
     * @param aDEADLINE the DEADLINE to set
     */
    public static void setDEADLINE(long aDEADLINE)
    {
        DEADLINE = aDEADLINE;
    }
}


/**
 * Enum sluzacy do porzedstawienia wielkosci po jakiej bedziemy sortowac prostokaty
 * wg algortmu, najefektywniejszym sposobem jest sortowanie po szerokosci (w sposob malejacy)
 * @author Michal Franczyk 
 */
enum Order 
{
    Area,   // pole pow
    Width,  // szerokosc
    Height  // wysokosc
}

/**
 * Klasa odpowiedzialna za możliwosc sortowania listy wlasnych obiektow prostokatow
 * klasa komparator
 * klasa abstrakcyjna
 * @author Michal Franczyk 
 */
abstract class RectComparator implements Comparator<Rectangle> 
{
    protected Order sortingBy;    // domyslny tryb sortowania

    /**
     * konstruktor
     */
    public RectComparator()
    {
        this.sortingBy = Order.Width;
    }

    @Override
    abstract public int compare(Rectangle rect1, Rectangle rect2);
}

/**
 * klasa dziedziczy po RectComparator
 * odpowiada za sortowanie prostokatow w sposob rosnacy
 * @author Michal Franczyk 
 */
class AscRectComparator extends RectComparator
{
    /**
     * konstruktor
     */
    public AscRectComparator()
    {
        super();
    }
    
    /**
     * konstruktor z wlasna inicjalizacja
     * @param sortBy 
     */
    public AscRectComparator(Order sortBy)
    {
        this.sortingBy = sortBy;
    }
    
    /**
     * funckja odpowiadajaca za porownania
     * pozwala na sortowanie
     * @param rect1 pierwszy 
     * @param rect2 drugi do porownania
     * @return zgodnie z dokumentacja javy, to samo co zwraca compareTo
     */
    public int compare(Rectangle rect1, Rectangle rect2) 
    {
        switch(sortingBy)
        {
            case Area:      return rect1.Area().compareTo(rect2.Area());
            case Width:     return rect1.GetWidth().compareTo(rect2.GetWidth());
            case Height:    return rect1.GetHeight().compareTo(rect2.GetHeight());
        }
        throw new RuntimeException("Natrafilem na zly case | AscRectComparator");
    }
}

/**
 * klasa dziedziczy po RectComparator
 * odpowiada za sortowanie prostokatow w sposob malejacy
 * @author Michal Franczyk 
 */
class DescRectComparator extends RectComparator
{
    /**
     * konstruktor
     */
    public DescRectComparator()
    {
        super();
    }
    
    /**
     * konstruktor z wlasna inicjalizacja
     * @param sortBy 
     */
    public DescRectComparator(Order sortBy)
    {
        this.sortingBy = sortBy;
    }

    /**
     * funckja odpowiadajaca za porownania
     * pozwala na sortowanie
     * @param rect1 pierwszy 
     * @param rect2 drugi do porownania
     * @return zgodnie z dokumentacja javy, to samo co zwraca compareTo
     */
    public int compare(Rectangle rect1, Rectangle rect2) 
    {
        switch(sortingBy)
        {
            case Area:      return rect2.Area().compareTo(rect1.Area());
            case Width:     return rect2.GetWidth().compareTo(rect1.GetWidth());
            case Height:    return rect2.GetHeight().compareTo(rect1.GetHeight());
        }
        throw new RuntimeException("Natrafilem na zly case | DescRectComparator");
    }
}
