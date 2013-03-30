import java.io.File;
import java.io.FileNotFoundException;
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
        }
        catch(NumberFormatException e)
        {
            System.err.println("BLAD ZLY ROZMIAR"+e.getMessage());
        }
        
//        Graph g = new Graph(); // nowy obiekt grafu
        
        
        File file = new File(args[0]); // użycie pliku wejsciowego do odczytu danych
        try 
        {
            Scanner sc = new Scanner(file).useDelimiter("\\D+"); // pobieranie tylko liczb ...
            while(sc.hasNext())
            {
                Integer w = sc.nextInt();
                Integer h = sc.nextInt();
                                
                // program liczy ilosc słabych! składowych spójnych
//                g.addEdge(new Edge(new Vertex(a), new Vertex(b)));  // dodanie krawędzi między wierzchołkami
//                g.addEdge(new Edge(new Vertex(b), new Vertex(a)));  
            }
        } 
        catch (FileNotFoundException ex) 
        {
            System.err.println("BLAD Z PARSOWANIEM PLIKU WEJSCIOWEGO czy PLIK istnieje??? -> "+ex.getMessage());
        }
        catch(NoSuchElementException ex)
        {
            if(Main.getTEST())
                System.err.println("BLAD ZLY FORMAT KRAWEDZI"+ex.getMessage());
        }
        
        if(Main.getTEST())
        {
//            System.out.println("Ilość Vertex'ów: "+(Main.GRAPH_SIZE-1));
//            g.PrintList();
        }
        
//        int ilosc_skladowych_spojnych = 0;
//        for(int i=1;i<Main.GRAPH_SIZE;++i)      // dla każdego wierzchołka... zaczynając od 1 (wierzchołek 0 jest ignorowany -> aby utrzymać zgodność z wymaganiami)
//        {
//            if(!g.GetDiscoveredList().get(i))   //jeśli wierzchołek nie został odwiedzony to ...
//            {
//                g.DFS(i);                       // rozpocznij procedurę DFS
//                ilosc_skladowych_spojnych++;    // zwiększ ilość składowych spójnych
//                
//                if(Main.TEST)
//                    System.out.println("");
//            }
//        }                                       // koniec ;)
//        
//        System.out.println("Skladowych spojnych : "+ilosc_skladowych_spojnych); 
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
     * @param aTEST the TEST to set
     */
    public static void setTEST(Boolean aTEST)
    {
        TEST = aTEST;
    }
}
