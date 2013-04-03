/**
 *
 * @author Michal
 */

class Tree
{
    private TreeNode treeRoot;
//    private int count;
    
    Tree()
    {
        treeRoot = null;
    }
    
    int size()
    {
        return treeRoot.size();
    }
    
    int height()
    {
        return treeRoot.getHeight();
    }
    
    void   print()
    {
        treeRoot.print();
    }
    
    Boolean empty()
    {
        return treeRoot.empty();
    }
    
//    //Object root() ;
//    TreeNode root()
//    {
//        
//    }
    
    TreeNode findNode(int w,int h)
    {
        return treeRoot.find(w, h);
    }
   
    Boolean insert(int width, int height)
    {
        return this.insert(width, height,0,0);
    }
        
    Boolean insert(int width, int height, int xCoordinate, int yCoordinate)
    {
        if ( /*empty()*/ treeRoot == null ) 
        {
            treeRoot = new TreeNode( width, height, xCoordinate, yCoordinate);
            return true;
        }
        else
        {
            TreeNode node = treeRoot.find(width, height);
            if (node==null)
            {
                return false;
            }
            else
            {
                node.split(width,height);
                return true;
            }
        }
    }
}

class TreeNode
{
    private int width;
    private int height;
    private int x_pos;
    private int y_pos;
    private Boolean filled;
    private TreeNode leftNode;
    private TreeNode rightNode;
    
    TreeNode( int w, int h, int xCoordinate, int yCoordinate )
    {
        width = w;
        height = h;
        x_pos = xCoordinate;
        y_pos = yCoordinate;
        filled = Boolean.FALSE;
        leftNode = null;
        rightNode = null;
    }
    
    int retrieveWidth()
    {
        return width;
    }
    
    int retrieveHeight()
    {
        return height;
    }
    
    int retrievePositionX()
    {
        return x_pos;
    }
    
    int retrievePositionY()
    {
        return y_pos;
    }
    
    TreeNode left()
    {
        return leftNode;
    }
    
    TreeNode right()
    {
        return rightNode;
    }
    
    Boolean isFilled()
    {
        return filled;
    }
    
    Boolean empty()
    {
        return (this == null);
    }
    
    Boolean is_leaf()
    {
        return (!empty() && left().empty() && right().empty());
    }
    
    int size()
    {
        return (empty() ? 0 :  1 + left().size() + right().size());
    }
    
    int getHeight()
    {
        return (empty() ? -1 :1 + Math.max(left().getHeight(), right().getHeight() ));
    }
    
    void print()
    {
        System.out.println(retrieveWidth()+" x "+retrieveHeight());
    
        if (right()!=null)
        {
            System.out.println("Right");
            right().print();
        }
        if (left()!=null) 
        {
            System.out.println("Down");
            left().print();
        }
    }
    
    void split(int w, int h)
    {
        System.out.println(w + ": (" + retrievePositionX() + ", " + retrievePositionY() + ")");
        filled=Boolean.FALSE;
        rightNode   = new TreeNode(width-w,h,x_pos+w,y_pos);
        leftNode    = new TreeNode(width,height-h,x_pos,y_pos+h);
        //print();
    }
    
    TreeNode find(int w,int h)
    {
        if (isFilled())
        {
            return ((right().find(w,h)==null)? left().find(w,h): right().find(w,h));
        }
        else if (w<=retrieveWidth() &&  h<=retrieveHeight())
        {
            return this;
        }
        else
        {
            return null;
        }
    }
}

public class Knapsack 
{
    
}
