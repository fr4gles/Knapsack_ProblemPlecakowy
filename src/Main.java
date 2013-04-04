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
    private static long DEADLINE;
    
    public static void main(String[] args) 
    {
        long startTime = 0;
        setDEADLINE(System.currentTimeMillis() + 225000); // (3.75*60000) -> deadline wykonania zadania
        
        if(Main.getTEST())
            startTime = System.currentTimeMillis();
        
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
        
        List<Rectangle> rectListOrigin = new ArrayList<>();
        
        File file = new File(args[0]); // użycie pliku wejsciowego do odczytu danych
        try 
        {
            Scanner sc = new Scanner(file).useDelimiter("\\D+"); // pobieranie tylko liczb ...
            while(sc.hasNext())
            {
                Integer w = sc.nextInt();
                Integer h = sc.nextInt();
                
                rectListOrigin.add(new Rectangle(w, h));
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
        
        long endTime = System.currentTimeMillis() + 180000; // (3*60000)

        int switchSortStrategy = 0;
        int localBest_iloscOdpadow = 0;
        Main.BEST_VALUE = Main.getTOTAL_SURFACE_SIZE();
        while(true)
        {
            if( System.currentTimeMillis() > endTime || System.currentTimeMillis() > Main.getDEADLINE())
                break;

            Main.setFILLED_AREA(0);
            
            switchShuffleCollections(switchSortStrategy++,rectListOrigin);
            k = new Knapsack(rectListOrigin);
            
            localBest_iloscOdpadow = k.pack();
            
            Main.BEST_VALUE = Math.min(Main.BEST_VALUE, localBest_iloscOdpadow);
            
            if(Main.BEST_VALUE == 0)
                break;
        }
        
        if(Main.getTEST())
        {
            System.out.println("Ilosc prob: "+ (switchSortStrategy) );
            System.out.println("Czas wykonania programu: "+ ((System.currentTimeMillis() - startTime)/1000.0) +" sec" );
        }
        
        System.out.println("Ilosc odpadow = " + Main.BEST_VALUE );
    }    

    public static List<Rectangle> switchShuffleCollections(int i, List<Rectangle> rectList)
    {
        switch(i)
        {
            case 0:     Collections.sort(rectList,new AscRectComparator());
                break;
            case 1:     Collections.sort(rectList,new DescRectComparator());
                break;
            case 2:     rectList = rectListSorter(rectList,0);
                break;
            case 3:     rectList = rectListSorter(rectList,1);
                break;
            case 4:     Collections.sort(rectList,new AscRectComparator());
                        rectList = rectListSorter(rectList,0);
                break;
            case 5:     Collections.sort(rectList,new DescRectComparator());
                        rectList = rectListSorter(rectList,0);
                break;
            case 6:     Collections.sort(rectList,new AscRectComparator());
                        rectList = rectListSorter(rectList,1);
                break;
            case 7:     Collections.sort(rectList,new DescRectComparator());
                        rectList = rectListSorter(rectList,1);
                break;
            case 8:     Collections.shuffle(rectList);
                        rectList = rectListSorter(rectList,0);
                break;
            case 9:     Collections.shuffle(rectList);
                        rectList = rectListSorter(rectList,1);
                break;
            default:    Collections.shuffle(rectList);
                break;        
        }
        return rectList;
    }
    
            private static int i = 0;

    /**
     * @return the i
     */
    public static int getI()
    {
        return i;
    }

    /**
     * @param aI the i to set
     */
    public static void setI(int aI)
    {
        i = aI;
    }
    
    private static Order wybierzTrybSortowania()
    {

        Order o = Order.Area;
        
        return o;
    }
    private static List<Rectangle> rectListSorter(List<Rectangle> rectList, int caseNum)
    {
        int tmpSize = rectList.size();
        List<Rectangle> tmp1 = rectList.subList(0, tmpSize/2);
        List<Rectangle> tmp2 = rectList.subList(tmpSize/2, tmpSize);
        
        switch(caseNum)
        {
            case 0: 
                Collections.sort(tmp1, new DescRectComparator());
                Collections.sort(tmp2, new AscRectComparator());
                break;
            case 1:
                Collections.sort(tmp1, new AscRectComparator());
                Collections.sort(tmp2, new DescRectComparator());
                break;
        }        
        
        tmp1.addAll(tmp2);
        return tmp1;
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

    /**
     * @return the DEADLINE
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
