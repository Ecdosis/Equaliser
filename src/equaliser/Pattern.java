/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equaliser;
import java.util.HashMap;
import org.xml.sax.Attributes;
/**
 * A Pattern is an element with some optional attributes and a template
 * @author desmond
 */
public class Pattern 
{
    static String[] defaultSet = 
    {"div:type=(titlepage|dedication|poempart|song|colophon)",
    "head:type=(title|subtitle|parthead),-=*",
    "lg:",
    "p:",
    "trailer:",
    "fw:",
    "q:",
    "l:rend=(indent1|indent2|indent3|indent4|indent5),part=F,-=*",
    "hi:rend=(ul|sc|b|it|dul|ss|erasure|bl|pencil|del-pencil)",
    "emph:",
    "unclear:",
    "expan:",
    "metamark:target=*,-=*",
    "author:",
    "date:",
    "sic:",
    "pb:facs=*,n=*",
    "note:name=*,resp=(Harpur|MJS|DS|AJ|Sydney Journal|Anonymous)",
    "body:",
    "add:",
    "del:",
    "TEI:",
    "graphic:url=*",
    "divider:type=(diamond|single|double)",
    "text:",
    "app:",
    "rdg:",
    "ref:target=*"};
    HashMap<String,String[]> attributes;
    /** element name */
    String element;
    int count;
    /**
     * Read the right hand side of an attribute expression: atr=(a|b|c)
     * @param rhs the rhs expression
     * @return an array of rhs values, maybe just 1
     */
    private String[] readValues( String rhs )
    {
        String parts[];
        String trimmed = rhs.trim();
        if ( trimmed.startsWith("(") && trimmed.endsWith(")") )
        {
            String kernel = trimmed.substring(1,trimmed.length()-1);
            parts = kernel.split("\\|");
            return parts;
        }
        else
        {
            parts = new String[1];
            parts[0] = rhs;
        }
        return parts;
    }
    /**
     * Create a pattern from a string definition
     * @param definition 
     */
    Pattern( String definition )
    {
        attributes = new HashMap<String,String[]>();
        String[] parts = definition.split(":");
        String[] empty = new String[0];
        if ( parts.length>0 )
        {
            this.element = parts[0];
            this.attributes = new HashMap<String,String[]>();
            if ( parts.length==1 )
                this.attributes.put("-",empty);    // NO attribute matches
            else
            {
                String[] attrs = parts[1].split(",");
                for ( int i=0;i<attrs.length;i++ )
                {
                    String[] halves = attrs[i].split("=");
                    if ( halves.length==2 )
                    {
                        String[] values = this.attributes.get(halves[0]);
                        if ( values == null )
                        {
                            values = readValues(halves[1]);
                            this.attributes.put(halves[0],values);
                        }
                        else
                        {
                            String[] newValues = new String[values.length+1];
                            for ( int j=0;j<values.length;j++ )
                                newValues[j] = values[j];
                            newValues[values.length] = halves[1];
                            this.attributes.put(halves[0],newValues);
                        }
                    }
                    else
                        System.out.println("Invalid attribute ignored "
                            +attrs[i]);
                }
            }
        }
    }
    void inc()
    {
        this.count++;
    }
    String getElement()       
    {
        return element;
    }
    boolean matchValue( String value, String[] values )
    {
        if ( values.length==1 && values[0].equals("*") )
            return true;
        else
        {
            for ( int i=0;i<values.length;i++ )
                if ( values[i].equals(value) )
                    return true;
        }
        return false;
    }
    boolean matches( Attributes atts )
    {
        boolean matched = false;
        if ( atts.getLength()==0 )
        {
            return attributes.containsKey("-");
        }
        else
        {
            for ( int i=0;i<atts.getLength();i++ )
            {
                String name = atts.getLocalName(i);
                String value = atts.getValue(i); 
                if ( attributes.containsKey(name) 
                    && matchValue(value,attributes.get(name)) )
                {
                    matched = true;
                }
                else
                    matched = false;
            }
        }
        return matched;
    }
}
