import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Michal
 */
public class Knapsack 
{
    private Node rootNode;
    private Integer iloscOdpadow;
    private List<Rectangle> rectList;
    
    public Knapsack(List<Rectangle> rectList)
    {
        iloscOdpadow = 0;
        this.rectList = rectList;
    }
    
    public int pack() 
    {        
        InitOrResetRootNode();
        
        int iloscUzytychKlockow = 0;
        for(Rectangle i: rectList)
        {            
            iloscOdpadow = Main.getTOTAL_SURFACE_SIZE() - Main.getFILLED_AREA();

//            if( ( Main.getTOTAL_SURFACE_SIZE() - (Main.getFILLED_AREA() + i.GetWidth()*i.GetHeight()) ) < 0)
//                break;

            Node tmp = rootNode.insert(i);

            if(tmp!=null)
            {
                Main.setFILLED_AREA(Main.getFILLED_AREA() + i.GetHeight()*i.GetWidth());
                iloscUzytychKlockow++;
            }
        }
        
        if(Main.getTEST())
            System.out.println("Ilosc uzytych klockow: "+iloscUzytychKlockow+"      "+"Obecna ilosc odpadow = "+iloscOdpadow);
        
        return iloscOdpadow;
    }
    
    public void InitOrResetRootNode()
    {
        iloscOdpadow = 0;
        rootNode = new Node();
        rootNode.setRect(new Rectangle(Main.getSURFACE_SIZE(), Main.getSURFACE_SIZE()));
    }
}


class Node
{
    private Node left;
    private Node right;
    private Rectangle rect;
    private Boolean filled = Boolean.FALSE;
    
    public Node insert(Rectangle rect)
    {
        if(System.currentTimeMillis() > Main.getDEADLINE())
            return null;
        
        if(this.left != null)
        {
            Node tmpNode = this.left.insert(rect);
            
            if(tmpNode != null)
                return tmpNode;
            
            tmpNode = this.right.insert(rect);
            return tmpNode;
        }
        
        if(this.filled)
            return null;
        
        if(!rect.fitsIn(this.rect))
            return null;
        
        if(rect.isSameSizeAs(this.rect))
        {
            this.filled = Boolean.TRUE;
            return this;
        }
        
        this.left = new Node();
        this.right = new Node();
        
        Integer widthDiff = this.rect.GetWidth() - rect.GetWidth();
        Integer heightDiff = this.rect.GetHeight() - rect.GetHeight();
        
        Rectangle me = this.rect;
        
        if(widthDiff > heightDiff)
        {
            // split literally into left and right, putting the rect on the left.
            this.left.setRect(new Rectangle(rect.GetWidth(), me.GetHeight(),me.GetX(), me.GetY()));
            this.right.setRect(new Rectangle(me.GetWidth() - rect.GetWidth(), me.GetHeight(),me.GetX() + rect.GetWidth(), me.GetY()));
        }
        else
        {
            // split into top and bottom, putting rect on top.
            this.left.setRect(new Rectangle(me.GetWidth(), rect.GetHeight(),me.GetX(), me.GetY()));
            this.right.setRect(new Rectangle(me.GetWidth(), me.GetHeight() - rect.GetHeight(), me.GetX(), me.GetY() + rect.GetHeight()));
        }

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

class Rectangle
{
    private Integer x_pos;
    private Integer y_pos;
    private Integer width;
    private Integer height;
    private Integer area;

    public Rectangle(Integer w, Integer h)
    {
        Init(w, h, 0, 0);
    }
    
    public Rectangle(Integer w, Integer h, Integer x, Integer y)
    {
        Init(w, h, x, y);
    }
    
    public Boolean fitsIn(Rectangle rect)
    {
        return ( rect.GetWidth() >= this.GetWidth() ) 
                && (rect.GetHeight() >= this.GetHeight());
    }
    
    public Boolean isSameSizeAs(Rectangle rect)
    {
        return (rect.GetWidth() == this.GetWidth())
                && (rect.GetHeight() == this.GetHeight());
    }
    
    private void Init(Integer w, Integer h, Integer x, Integer y)
    {
        width = w;
        height = h;
        
        x_pos = x;
        y_pos = y;
        
        area = width * height;
    }
    
    public Integer GetX()
    {
        return x_pos;
    }
    
    public Integer GetY()
    {
        return y_pos;
    }
    
    public Integer GetWidth()
    {
        return width;
    }
    
    public Integer GetHeight()
    {
        return height;
    }
    
    public Integer Area()
    {
        return area;
    }
}

enum Order 
{
    Area, 
    Width, 
    Height
}

abstract class RectComparator implements Comparator<Rectangle> 
{
    protected Order sortingBy = Order.Area;

    public RectComparator()
    {
        this.sortingBy = Order.Area;
    }

    @Override
    abstract public int compare(Rectangle rect1, Rectangle rect2);
}

class AscRectComparator extends RectComparator
{
    public AscRectComparator()
    {
        super();
    }
    
    public AscRectComparator(Order sortBy)
    {
        this.sortingBy = sortBy;
    }
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

class DescRectComparator extends RectComparator
{
    public DescRectComparator()
    {
        super();
    }
    
    public DescRectComparator(Order sortBy)
    {
        this.sortingBy = sortBy;
    }

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
