/*
 * This file is part of Equaliser.
 *
 *  Equaliser is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Equaliser is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Equaliser.  If not, see <http://www.gnu.org/licenses/>.
 *  (c) copyright Desmond Schmidt 2015
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
        {
            boolean required = false;
            String name = atts.getLocalName(i);
            if ( name.startsWith("!") )
                required = true;
            this.attrs[i] = new Attribute(name,atts.getValue(i),required);
        }
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
