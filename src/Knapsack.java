import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Michal
 */
public class Knapsack 
{
    public Node rootNode;
    public Integer iloscOdpadow;
    public List<Rectangle> rectList;
    
    public Knapsack(List<Rectangle> rectList)
    {
        iloscOdpadow = 0;
        this.rectList = rectList;
    }
    
    public void go() 
    {        
        InitOrResetRootNode();
        
        int iloscUzytychKlockow = 0;
        for(Rectangle i: rectList)
        {
            if( ( Main.getTOTAL_SURFACE_SIZE() - (Main.getFILLED_AREA() + i.GetWidth()*i.GetHeight()) ) < 0)
            {
                iloscOdpadow = Main.getTOTAL_SURFACE_SIZE() - Main.getFILLED_AREA();
                break;
            }

            Node tmp = rootNode.insert(i);

            if(tmp!=null)
            {
                Main.setFILLED_AREA(Main.getFILLED_AREA() + i.GetHeight()*i.GetWidth());
                iloscUzytychKlockow++;
            }
            
            iloscOdpadow = Main.getTOTAL_SURFACE_SIZE() - Main.getFILLED_AREA();
        }
        System.out.print("Ilosc uzytych klockow: "+iloscUzytychKlockow+"      ");
    }
    
    public void InitOrResetRootNode()
    {
        iloscOdpadow = 0;
        rootNode = new Node();
        rootNode.rect = new Rectangle(Main.getSURFACE_SIZE(), Main.getSURFACE_SIZE());
    }
}

class Node
{
    public Node left;
    public Node right;
    public Rectangle rect;
    public Boolean filled = Boolean.FALSE;
    
    public Node insert(Rectangle rect)
    {
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
        
        if(rect.isSameSize(this.rect))
        {
            this.filled = Boolean.TRUE;
            return this;
        }
        
        this.left = new Node();
        this.right = new Node();
        
        Integer width_diff = this.rect.GetWidth() - rect.GetWidth();
        Integer height_diff = this.rect.GetHeight() - rect.GetHeight();
        
        Rectangle me = this.rect;
        
        if(width_diff > height_diff)
        {
            // split literally into left and right, putting the rect on the left.
            this.left.rect = new Rectangle(rect.GetWidth(), me.GetHeight(),me.GetX(), me.GetY());
            this.right.rect = new Rectangle(me.GetWidth() - rect.GetWidth(), me.GetHeight(),me.GetX() + rect.GetWidth(), me.GetY());
        }
        else
        {
            // split into top and bottom, putting rect on top.
            this.left.rect = new Rectangle(me.GetWidth(), rect.GetHeight(),me.GetX(), me.GetY());
            this.right.rect = new Rectangle(me.GetWidth(), me.GetHeight() - rect.GetHeight(), me.GetX(), me.GetY() + rect.GetHeight());
        }

        return this.left.insert(rect);
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
    
    public Boolean isSameSize(Rectangle rect)
    {
        return (rect.GetWidth() == this.GetWidth())
                && (rect.GetHeight() == this.GetHeight());
    }
    
    private void Init(Integer w, Integer h, Integer x, Integer y)
    {
        width = w;
        height = h;
        
        SetX(x);
        SetY(y);
        
        area = width * height;
    }
    
    public void SetX(Integer x)
    {
        x_pos = x;
    }
    
    public void SetY(Integer y)
    {
        y_pos = y;
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

class AscRectComparator implements Comparator<Rectangle> 
{
    public int compare(Rectangle rect1, Rectangle rect2) 
    {
        return rect1.Area().compareTo(rect2.Area());
    }
}

class DescRectComparator implements Comparator<Rectangle> 
{
    public int compare(Rectangle rect1, Rectangle rect2) 
    {
        return rect2.Area().compareTo(rect1.Area());
    }
}