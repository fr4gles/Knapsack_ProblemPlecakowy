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
    private static int TOTAL_SURFACE_SIZE;
    private static int FILLED_AREA;
    private static Boolean TEST = Boolean.TRUE;
    private static int BEST_VALUE;
    
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
            Main.setTOTAL_SURFACE_SIZE(Main.getSURFACE_SIZE()*Main.getSURFACE_SIZE());
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
        
        Knapsack k = null;
        
        long endTime = System.currentTimeMillis() + 60000;

//        System.out.println("Start: "+start);

        int switcher = 1;
        Main.BEST_VALUE = Main.TOTAL_SURFACE_SIZE;
        while(true)
        {
            if( System.currentTimeMillis() > endTime )
                break;

            Main.FILLED_AREA = 0;
            
            switchShuffleCollections(switcher++,rectList);
            k = new Knapsack(rectList);
            
            k.go();
            
//            if(Main.getTEST())
//                System.out.println("Obecna ilosc odpadow = "+k.iloscOdpadow);
            
            if(k.iloscOdpadow == 0)
                break;
            
            Main.BEST_VALUE = Math.min(Main.BEST_VALUE, k.iloscOdpadow);
        }
        
        if(Main.getTEST())
        {
            System.out.println("Ilosc prob: "+ (switcher-1) );
        }
        
        System.out.println("Ilosc odpadow = " + Main.BEST_VALUE );
        
    }    

    public static void/*List<Rectangle>*/ switchShuffleCollections(int i, List<Rectangle> rectList)
    {
        switch(i)
        {
            case 1:     Collections.sort(rectList,new AscRectComparator());
                break;
            case 2:     Collections.sort(rectList,new DescRectComparator());
                break;
            default:    Collections.shuffle(rectList);
                break;        
        }
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

    /**
     * @return the TOTAL_SURFACE_SIZE
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
}
