/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        "emph:rend=*:replace emph with hi",
        "hi::hi requires rend attribute",
        "note:type=source:remove note, type source not allowed",
        "note::supply a valid resp attribute",
        "div:type=poem:carefully remove this <div> and matching </div>"
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
                        attrs[i] = new Attribute( halves[0], halves[1] );
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
