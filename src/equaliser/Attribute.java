/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equaliser;

/**
 *
 * @author desmond
 */
public class Attribute {
    String name;
    String value;
    Attribute( String name, String value )
    {
        this.name = name;
        this.value = value;
    }
    boolean matches( Attribute other )
    {
        if ( this.name.equals(other.name) )
        {
            if ( this.value.equals(other.value) )
                return true;
            else if ( this.value.equals("*")||other.value.equals("*") )
                return true;
        }
        return false;
    }
}
