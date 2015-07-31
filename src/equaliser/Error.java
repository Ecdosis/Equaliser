/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equaliser;

import org.xml.sax.Attributes;
import java.util.ArrayList;
/**
 * Store information about an incorrect element+attribute combo
 * @author desmond
 */
public class Error {
    ArrayList<Location> locations;
    Attribute[] attrs;
    Error( Attributes atts, Location loc )
    {
        this.attrs = new Attribute[atts.getLength()];
        for ( int i=0;i<atts.getLength();i++ )
            this.attrs[i] = new Attribute(atts.getLocalName(i),atts.getValue(i));
        this.locations = new ArrayList<Location>();
        this.locations.add(loc);
    }
    void addLocation( Location loc )
    {
        this.locations.add(loc);
    }
    boolean equalsAtts( Attributes a )
    {
        if ( a.getLength() != attrs.length )
            return false;
        else
        {
            for ( int i=0;i<a.getLength();i++ )
            {
                boolean matched = false;
                for ( int j=0;j<attrs.length;j++ )
                {
                    if ( a.getLocalName(i).equals(attrs[i].name) 
                        && a.getValue(i).equals(attrs[i].value) )
                    {
                        matched = true;
                        break;
                    }
                }
                if ( !matched )
                    return false;
            }
            return true;
        }
    }
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for ( int i=0;i<attrs.length;i++ )
        {
            sb.append(attrs[i].name);
            sb.append("=\"");
            sb.append(attrs[i].value);
            sb.append("\" ");
        }
        return sb.toString();
    }
    int numAttrs()
    {
        return attrs.length;
    }
    Location[] getLocations()
    {
        Location[] locs = new Location[this.locations.size()];
        this.locations.toArray(locs);
        return locs;
    }
}
