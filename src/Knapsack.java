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
    
    public Knapsack(List<Rectangle> rectList)
    {
        iloscOdpadow = 0;
        
        InitRootNode();
        
        for(Rectangle i: rectList)
        {
            if( ( Main.getTOTAL_SURFACE_SIZE() - (Main.getFILLED_AREA() + i.GetWidth()*i.GetHeight()) ) < 0)
                break;
            
            rootNode.insert(i);
            
            if(rootNode!=null)
            {
                Main.setFILLED_AREA(Main.getFILLED_AREA() + i.GetHeight()*i.GetWidth());                
            }
            
            iloscOdpadow = Main.getTOTAL_SURFACE_SIZE() - Main.getFILLED_AREA();
        }
        
    }
    
    public void InitRootNode()
    {
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
    
    public Boolean fitsIn(Rectangle rect)
    {
        return ( rect.GetWidth() >= this.rect.GetWidth() ) 
                && rect.GetHeight() >= this.rect.GetHeight();
    }
    
    public Boolean isSameSize(Rectangle rect)
    {
        return rect.GetWidth() == this.rect.GetWidth() 
                && rect.GetHeight() == this.rect.GetHeight();
    }
    
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
        
        if(!fitsIn(this.rect))
            return null;
        
        if(isSameSize(this.rect))
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
            this.left.rect = new Rectangle(me.GetX(), me.GetY(), rect.GetWidth(), me.GetHeight());
            this.right.rect = new Rectangle(me.GetX() + rect.GetWidth(), me.GetY(), me.GetWidth() - rect.GetWidth(), me.GetHeight());
        }
        else
        {
            // split into top and bottom, putting rect on top.
            this.left.rect = new Rectangle(me.GetX(), me.GetY(), me.GetWidth(), rect.GetHeight());
            this.right.rect = new Rectangle(me.GetX(), me.GetY() + rect.GetHeight(), me.GetWidth(), me.GetHeight() - rect.GetHeight());
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

class RectComparator implements Comparator<Rectangle> 
{
    public int compare(Rectangle rect1, Rectangle rect2) 
    {
        return rect1.Area().compareTo(rect2.Area());
    }
}