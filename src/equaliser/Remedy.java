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

/**
 * Provide a plain text hint how to fix a coding anomaly
 * @author desmond
 */
public class Remedy {
    String elemName;
    Attribute[] attrs;
    String message;
    static String[] defaultSet = 
    {
        "rdg:rend=brackets:remove this attribute",
        "divider:rend=diamond:normalise unrecgonised divider type",
        "lb::replace empty lb with CR in prose and <l>...</l> in poetry",
        "emph:rend=*:replace emph with hi, enclose in empty <emph>",
        "hi::hi requires rend attribute",
        "note:type=source:remove note, type source not allowed",
        "note::supply a valid resp attribute",
        "div:type=poem:carefully remove this <div> and matching </div>",
        "l:type=F:replace type with part"
    };
    /**
     * Initialise a single remedy from its definition in 3 parts
     * @param spec the defn: name:attrs:message
     */
    Remedy( String spec )
    {
        String[] parts = spec.split(":");
        if ( parts.length == 3 )
        {
            elemName = parts[0];
            if ( parts[1].length()>0 )
            {
                String[] attrDefs = parts[1].split(",");
                attrs = new Attribute[attrDefs.length];
                for ( int i=0;i<attrDefs.length;i++ )
                {
                    String[] halves = attrDefs[i].split("=");
                    if ( halves.length==2 )
                    {
                        attrs[i] = new Attribute( halves[0], halves[1], false );
                    }
                }
            }
            else
                attrs = new Attribute[0];
            message = parts[2];
        }
    }
    boolean match( String name, Attribute[] errAttrs )
    {
        boolean matched = false;
        if ( name.equals(elemName) )
        {
            matched = true;
            for ( int i=0;i<attrs.length;i++ )
            {
                int j=0;
                for ( ;j<errAttrs.length;j++ )
                {
                    if ( errAttrs[j].matches(attrs[i]) )
                        break;
                }
                if ( j == errAttrs.length )
                {
                    matched = false;
                    break;
                }
            }
        }
        return matched;
    }
    String getMessage()
    {
        return message;
    }
}
