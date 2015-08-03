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
 * Represent the location of an error in a file
 * @author desmond
 */
public class Location {
    String file;
    int lineNo;
    Location( String file, int lineNo )
    {
        this.file = file;
        this.lineNo = lineNo;
    }
    public String toString()
    {
        return file+", line "+lineNo;
    }
}
