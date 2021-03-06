/*
 * Epipog, Copyright(c) 2016-17, Andrew Ferlitsch, CC-BY
 */
package epipog.data;

import epipog.schema.Schema;
 
// Implementation for Accessing Data Item of type Double
//
public class DataDouble extends Data {
	// Implementation for getting the data type
	public String Type() {
		return "double";
	}
	
	// Method for getting the data type (as BSON data type)
	public int BType() {
		return Schema.BSONDouble;
	}
	
	// Implementation for getting the size of the data type
	public Integer Size() {
		return 8;
	}

	// Implementation for getting the value of the data item
	public Double Get() {
		return ( Double ) value;
	}
	
	// Implementation for setting the value of the data item
	public void Set( Object v ) {
		value = ( Double ) v;
	}
	
	// Implementation for string representation of the data item
	public String AsString() {
		return String.valueOf( ( Double ) value );
	}
	
	// Implementation for converting from String representation to data item
	public void Parse( String s ) 
		throws DataException
	{
		try {
			value = Double.parseDouble( s );
		}
		catch ( NumberFormatException e ) {
			throw new DataException( "DataDouble.Parse: input invalid: " + s );
		}
	}
	
	// Implementation for equal operator for data type
	public boolean EQ( Object v ) {
		Double v1 = ( Double ) value;				// compiler workaround
		Double v2 = ( ( DataDouble ) v ).Get();
		return  ( v1 - v2 ) == 0.0;	
	}
	
	// Implementation for not equal operator for data type
	public boolean NE( Object v ) {
		Double v1 = ( Double ) value;				// compiler workaround
		Double v2 = ( ( DataDouble ) v ).Get();
		return  ( v1 - v2 ) != 0.0;	
	}
	
	// Implementation for less than operator for data type
	public boolean LT( Object v ) {
		return ( Double ) value < ( ( DataDouble ) v ).Get();
	}
	
	// Implementation for greater than operator for data type
	public boolean GT( Object v ) {
		return ( Double ) value > ( ( DataDouble ) v ).Get();
	}
	
	// Implementation for less than or equal operator for data type
	public boolean LE( Object v ) {
		return ( Double ) value <= ( ( DataDouble ) v ).Get();
	}
	
	// Implementation for greater than or equal operator for data type
	public boolean GE( Object v ) {
		return ( Double ) value >= ( ( DataDouble ) v ).Get();
	}
}