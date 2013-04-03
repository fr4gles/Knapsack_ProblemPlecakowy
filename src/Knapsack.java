/**
 *
 * @author Michal
 */
public class Knapsack 
{
    
}


class Rectangle
{
    private int x_pos;
    private int y_pos;
    private int width;
    private int height;
    private int area;
    
    Rectangle(int w, int h, int x, int y)
    {
        width = w;
        height = h;
        
        SetX(x);
        SetY(y);
        
        area = width * height;
    }
    
    public void SetX(int x)
    {
        x_pos = x;
    }
    
    public void SetY(int y)
    {
        y_pos = y;
    }
    
    public int GetX()
    {
        return x_pos;
    }
    
    public int GetY()
    {
        return y_pos;
    }
    
    public int GetWidth()
    {
        return width;
    }
    
    public int GetHeight()
    {
        return height;
    }
    
    public int Area()
    {
        return area;
    }
}
