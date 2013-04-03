import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Główna klasa programu
 * Odpowiada za wykorzystanie logiki zawartej w Klasie Graph do znalezienia slabych skladowych spojnych
 * @author Michal
 * @date 22.03.2013
 */
public class Main 
{
    private static int SURFACE_SIZE;
    private static int FILLED_AREA;
    private static Boolean TEST = Boolean.FALSE;
    
    public static void main(String[] args) 
    {
        if(args.length < 2) // jesli zla ilosc argumentow wejsciowych to poinforuj o tym uzytkownika
        {
            System.err.println("BLAD, zla ilosc argumentow wejsciowych. Podaj <nazwa_pliku> <wielkosc_tafli>");
            System.err.println("W pliku powinny znajdowac sie pary liczb calkowitych (w oraz h)");
            System.exit(1);
        }
                
        try
        {
            Main.setSURFACE_SIZE(Integer.parseInt(args[1])); // czytanie z wejscia ilośći wierzchołków
            Main.setFILLED_AREA(0);
        }
        catch(NumberFormatException e)
        {
            System.err.println("BLAD ZLY ROZMIAR"+e.getMessage());
        }
        
        List<Rectangle> rectList = new ArrayList<>();
        
        File file = new File(args[0]); // użycie pliku wejsciowego do odczytu danych
        try 
        {
            Scanner sc = new Scanner(file).useDelimiter("\\D+"); // pobieranie tylko liczb ...
            while(sc.hasNext())
            {
                Integer w = sc.nextInt();
                Integer h = sc.nextInt();
                
                rectList.add(new Rectangle(w, h));
            }
        } 
        catch (FileNotFoundException ex) 
        {
            System.err.println("BLAD Z PARSOWANIEM PLIKU WEJSCIOWEGO czy PLIK istnieje??? -> "+ex.getMessage());
        }
        catch(NoSuchElementException ex)
        {
            if(Main.getTEST())
                System.err.println("BLAD ZLY FORMAT KRAWEDZI -> "+ex.getMessage());
        }
        
        Collections.sort(rectList,new RectComparator());
        
        Knapsack k = new Knapsack(rectList);
        
        if(Main.getTEST())
        {
//            System.out.println("Ilość Vertex'ów: "+(Main.GRAPH_SIZE-1));
//            g.PrintList();
        }
        
        System.out.println("Ilosc odpadow = " + ( (Main.getSURFACE_SIZE()*Main.getSURFACE_SIZE()) 
                                                    - Main.getFILLED_AREA()) );
        
    }    

    /**
     * @return the SURFACE_SIZE
     */
    public static int getSURFACE_SIZE()
    {
        return SURFACE_SIZE;
    }

    /**
     * @param aSURFACE_SIZE the SURFACE_SIZE to set
     */
    public static void setSURFACE_SIZE(int aSURFACE_SIZE)
    {
        SURFACE_SIZE = aSURFACE_SIZE;
    }

    /**
     * @return the TEST
     */
    public static Boolean getTEST()
    {
        return TEST;
    }

    /**
     * @return the FILLED_AREA
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
}
