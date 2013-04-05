import java.util.List;

/**
 * Knapsack -> obsluguje algorytm do ukladania klockow
 *
 * @author Michal Franczyk 
 * @date 22.03.2013
 */
public class Knapsack 
{
    private Node rootNode;              // przechowuje drzewo ulozen klockow
    private Integer iloscOdpadow;       // ilosc odpadow
    private List<Rectangle> rectList;   // lista prostokatow, ktore bedziemy probowac ulozyc
    
    /**
     * konstruktor ustawiajacy poczatkowe wartosci
     * @param rectList lista prostokatow
     */
    public Knapsack(List<Rectangle> rectList)
    {
        iloscOdpadow = 0;
        this.rectList = rectList;
    }
    
    /**
     * funkcja pack
     * zarzadza ukladaniem klockow na planszy
     * opis samego ukladania w Node.insert
     * @return ilosc odpadow
     */
    public int pack() 
    {        
        InitOrResetRootNode();              // inicjalizacja lub reset wartosci poczatkowych
        
        int iloscUzytychKlockow = 0;        // zmienna na potrzeby testow, zerowanie ilosci uzytych klockow
        for(Rectangle i: rectList)          // dla kazdego prostokata ...
        {            
            if(rootNode.insert(i)!=null)    // kazdego prostokata sprobuj postawic na planszy... jesli null to sie nie udalo --> nie mozna postawic wiecej :(
            {
                Main.setFILLED_AREA(Main.getFILLED_AREA() + i.GetHeight()*i.GetWidth());    // aktualizuj wielkosc jaka wypelniaja klocki na planszy
                iloscUzytychKlockow++;  // aktualizuj ich ilosc
            }
        }
        
        // po skonczeniu ukladania, aktualizuj wielkosc odpadu
        iloscOdpadow = Main.getTOTAL_SURFACE_SIZE() - Main.getFILLED_AREA();
        
        if(Main.getTEST())
            System.out.println("Ilosc uzytych klockow: "+iloscUzytychKlockow+"      "+"Obecna ilosc odpadow = "+iloscOdpadow);
        
        return iloscOdpadow;
    }
    
    /**
     * inicjalizacja lub reset wartosci poczatkowych
     */
    public void InitOrResetRootNode()
    {
        iloscOdpadow = 0;
        rootNode = new Node();
        rootNode.setRect(new Rectangle(Main.getSURFACE_SIZE(), Main.getSURFACE_SIZE()));
    }
}

/**
 * Klasa odpowiedzialna za drzewo ulozen prostokatow
 * @author Michal Franczyk
 */
class Node
{
    private Node left;      // lewy liść
    private Node right;     // prawy liść
    private Rectangle rect; // prostokat
    private Boolean filled = Boolean.FALSE; // czy wypelniony?
    
    /**
     * Głowny funckja programu
     * rekurencyjnie układa prostokaty na planszy budujac drzewo ulozenia prostokatow
     * ogólna zasadza dzialania algorytmu, którego idea jest przedstawiona w http://www.blackpawn.com/texts/lightmaps/:
     *  --
     * 1. Jako pierwszy element wstawiany jest duży prostokąt który będzie plansza.
     * 2. Przy pórbie położenia następnego prostokąta plansza jest dzielona na "strefy" - A i B, w którepowinny się zmiejscic kolejne kladzione klocki.
     * ( wszystko przedstawione / reprezentowane w budowie drzewiastej )
     * 3. Kiedy chcemy polozyc nastepny klocek sprawdzamy czy zmiesci sie w ponad strefą A
     * 4. i jesli tak to sprawdzamy czy zmiesci sie na lewo strefy B
     * 5. jeśli nie to sprawdzamy prawa strone B
     * 6. jesli pasuje to dzieliny na nowe strefy A i B ta jak wczesniej
     * 7. w przeciwnym razie kladziemy ponad stara strefą A, w tym przypadku dzieliny na D i C
     *  --
     * @param rect
     * @return 
     */
    public Node insert(Rectangle rect)
    {
        // gdyby działanie programu znacznie przekroczyło dozwolony czas ...
        if(System.currentTimeMillis() > Main.getDEADLINE())
            return null;
        
        if(this.left != null)   // jeśli nie jesteśmy liśćiem 
        {
            Node tmpNode = this.left.insert(rect);  // spróbuj zrobić insert do pierwszego dziecka
            
            if(tmpNode != null)     // jeśli się nie powiodło
                return tmpNode;
            
            tmpNode = this.right.insert(rect);  // jednak nie ma miejsca, insert do drugiego dziecka
            return tmpNode;
        }
        
        if(this.filled)     // jesli strefa jest juz wypelionia --> jest juz tam klocek
            return null;
        
        if(!rect.fitsIn(this.rect))     // jesli strefa za mala 
            return null;
        
        if(rect.isSameSizeAs(this.rect))    // jesli jest tego samego rozmiaru to akceptuj   
        {
            this.filled = Boolean.TRUE;
            return this;
        }
        
        // w przeciwnym razie musimy podzielic lisc na kolejne ...
        this.left = new Node();
        this.right = new Node();
        
        // decydujemy w którą stronę bedziemy dzielic
        Integer widthDiff = this.rect.GetWidth() - rect.GetWidth();
        Integer heightDiff = this.rect.GetHeight() - rect.GetHeight();
        
        if(widthDiff > heightDiff)
        {
            // dzieliny na strefy prawa /lewa --> kladziemy na lewo
            this.left.setRect(new Rectangle(rect.GetWidth(), this.rect.GetHeight(),this.rect.GetX(), this.rect.GetY()));
            this.right.setRect(new Rectangle(this.rect.GetWidth() - rect.GetWidth(), this.rect.GetHeight(),this.rect.GetX() + rect.GetWidth(), this.rect.GetY()));
        }
        else
        {
            // dzieliny na strefy gora / dol -> kladziemy na gorze
            this.left.setRect(new Rectangle(this.rect.GetWidth(), rect.GetHeight(),this.rect.GetX(), this.rect.GetY()));
            this.right.setRect(new Rectangle(this.rect.GetWidth(), this.rect.GetHeight() - rect.GetHeight(), this.rect.GetX(), this.rect.GetY() + rect.GetHeight()));
        }

        // poloz na pierwszym dziecku ktory stworzylismy
        return this.left.insert(rect);
    }

    /**
     * @param rect the rect to set
     */
    public void setRect(Rectangle rect)
    {
        this.rect = rect;
    }
}

/**
 * Klasa reprezentujaca prostokat
 * @author Michal Franczyk
 */
class Rectangle
{
    private Integer x_pos;  // pozycja oX
    private Integer y_pos;  // pozycja oY
    private Integer width;  // szerokosc
    private Integer height; // wysokosc
    private Integer area;   // pole powierzchni

    /**
     * konstrkutor inicjalizujacy z pozycja 0,0
     * @param w szerokosc
     * @param h wysokosc
     */
    public Rectangle(Integer w, Integer h)
    {
        Init(w, h, 0, 0);
    }
    
    /**
     * konstrkutor inicjalizujacy z pozycja x,y
     * @param w szerokosc
     * @param h wysokosc
     * @param x pozycja na oX
     * @param y pozycja na oY
     */
    public Rectangle(Integer w, Integer h, Integer x, Integer y)
    {
        Init(w, h, x, y);
    }
    
    /**
     * funkcja sprawdzajaca czy przekazany prostokat miesci sie w obencym (strefy ...)
     * @param rect przekazany prostokat
     * @return prawda jesli tak, falsz jesli nie
     */
    public Boolean fitsIn(Rectangle rect)
    {
        return ( rect.GetWidth() >= this.GetWidth() ) 
                && (rect.GetHeight() >= this.GetHeight());
    }
    
    /**
     * funkcja sprawdzajaca czy przekazany prostokat jest taki sam jak obency (strefy ...)
     * @param rect przekazany prostokat
     * @return prawda jesli tak, falsz jesli nie
     */
    public Boolean isSameSizeAs(Rectangle rect)
    {
        return (rect.GetWidth() == this.GetWidth())
                && (rect.GetHeight() == this.GetHeight());
    }
    
    /**
     * funkcja inicjalizujaca zmienne i obliczajace pole prostokata
     * @param w szer
     * @param h wys
     * @param x oX
     * @param y oY
     */
    private void Init(Integer w, Integer h, Integer x, Integer y)
    {
        width = w;
        height = h;
        
        x_pos = x;
        y_pos = y;
        
        area = width * height;
    }
    
    /**
     * getter ... 
     * @return pozycja na oX
     */
    public Integer GetX()
    {
        return x_pos;
    }
    
    /**
     * getter ...
     * @return pozycja na oY
     */
    public Integer GetY()
    {
        return y_pos;
    }
    
    /**
     * getter ..
     * @return szerokosc
     */
    public Integer GetWidth()
    {
        return width;
    }
    
    /**
     * getter ...
     * @return wysokosc
     */
    public Integer GetHeight()
    {
        return height;
    }
    
    /**
     * getter ...
     * @return pole powierzchni
     */
    public Integer Area()
    {
        return area;
    }
}
