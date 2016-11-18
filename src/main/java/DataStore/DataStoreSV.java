/*
 * Epipog, Copyright(c) 2016-17, Andrew Ferlitsch, CC-BY
 */
package epipog.datastore;

import epipog.annotations.*;
import epipog.collection.Collection;
import epipog.storage.*;

import javafx.util.Pair;
import java.util.ArrayList;

// A Layer for Accessing DataStore as a SV Store (Separated Value)
//
public abstract class DataStoreSV extends DataStore {
	
	@Constructor
	public DataStoreSV( byte separator ) {
		this.separator = this.separator;
	}
		
	private	byte	separator;			// field separator
	
	
	// Method for inserting into datastore with key (field) name
	// keyvals:
	//	L = Name of Key
	//	R = Value in String Representation
	public void Insert( ArrayList<Pair<String,String>> keyVals ) 
		throws DataStoreException, StorageException
	{
			
	}
	
	// Method for inserting into datastore by predefined column order
	// values: Value in String Representation
	public void InsertC( ArrayList<String> values ) 
		throws DataStoreException, StorageException
	{
		if ( null == values )
			return;
		
		int vlen = values.size();
		if ( collection.Schema().NCols() != vlen )
			throw new DataStoreException( "DataStoreSV.InsertC: incorrect number of values" );
		
		// Seek to the end of the Storage
		End();
		
		// Write each key value to storage
		for ( String value : values ) {
			if ( -1 != value.indexOf ( separator ) )
				Write( "\"" + value + "\"" );
			else
				Write( value );
			
			Write( (byte) separator );
		}
		
		Move( Pos() - 1 );	// remove trailing pipe symbol
		Write( "\r\n" );
	}
}


