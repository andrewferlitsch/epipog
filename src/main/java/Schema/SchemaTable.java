/*
 * Epipog, Copyright(c) 2016-17, Andrew Ferlitsch, CC-BY
 */
package epipog.schema;

import javafx.util.Pair;
import java.util.ArrayList;

// Implementation for Table Schema
//
public class SchemaTable implements Schema {
	@Constructor
	public SchemaTable( String collectionName ) {
		Collection( collectionName );
	}
	
	private String collectionName;	// collection name
	
	// table schema: (Restriction) schema definition must be in same order as table definition
	protected ArrayList<Pair<String,Integer>> keys;
	
	// Set/Get a collection name to the schema
	@Setter
	public void Collection( String collectionName ) {
		this.collectionName = collectionName;
	}
	@Getter
	public String Collection() { return collectionName; }
	
	// Method for dynamically specifying the schema, where data type is in string representation
	// Pair =
	//	L = Field Name
	//	R = Field Type ( String, Integer, Long, Float, Double, Date, Time, etc )
	@Setter
	public void SetS( ArrayList<Pair<String,String>>  keys ) 
		throws SchemaException
	{
		if ( null == keys )
			throw new SchemaException( "Schema.SetS: keys is null" );
		if ( keys.size() == 0 )
			throw new SchemaException( "Schema.SetS: keys is empty" );
		if ( null != this.keys )
			throw new SchemaException( "Schema.SetS: cannot replace existing schema" );
		
		// Allocate new Schema
		this.keys = new ArrayList<Pair<String,Integer>>();
		
		int len = keys.size();
		for ( int i = 0; i < len; i++ ) {
			String key = keys.get( i ).getKey();
			if ( key == null ) { 
				this.keys = null; throw new SchemaException( "Schema.SetS: key name is null" ); 
			}
			if ( key.equals( "" ) ) { 
				this.keys = null; throw new SchemaException( "Schema.SetS: key name is empty" ); 
			}
			
			// look for duplicate
			for ( int j = i + 1; j < len; j++ ) {
				if ( key.equals( keys.get( j ).getKey() ) ) { 
					this.keys = null; throw new SchemaException( "Schema.SetS: duplicate key : " + key ); 
				}
			}
			
			// Get the BSON id for the data type
			int type = BSONType.Find( keys.get( i ).getValue() );
			if ( 0 == type ) {
				this.keys = null; throw new SchemaException( "Schema.SetS: invalid type: " + keys.get( i ).getValue() );
			}
		
			// Add the key/type pair to the Schema
			this.keys.add( new Pair<String,Integer>( key, type ) );
		}
	}
	
	// Method for dynamically specifying the schema, where data type is in an integer representation
	@Setter
	public void SetI( ArrayList<Pair<String,Integer>> keys ) 
		throws SchemaException
	{
		if ( null == keys )
			throw new SchemaException( "Schema.SetI: keys is null" );
		if ( keys.size() == 0 )
			throw new SchemaException( "Schema.SetI: keys is empty" );
		if ( null != this.keys )
			throw new SchemaException( "Schema.SetI: cannot replace existing schema" );
		
		int len = keys.size();
		for ( int i = 0; i < len; i++ ) {
			String key = keys.get( i ).getKey();
			if ( key == null ) { 
				this.keys = null; throw new SchemaException( "Schema.SetS: key name is null" ); 
			}
			if ( key.equals( "" ) ) { 
				this.keys = null; throw new SchemaException( "Schema.SetS: key name is empty" ); 
			}
			
			// look for duplicate
			for ( int j = i + 1; j < len; j++ ) {
				if ( key.equals( keys.get( j ).getKey() ) ) {
					this.keys = null; throw new SchemaException( "Schema.SetI: duplicate key : " + key );
				}
			}
			
			// check if valid BSON type id
			if ( !BSONType.Valid( keys.get( i ).getValue() ) ) {
				this.keys = null; throw new SchemaException( "Schema.SetI: invalid BSON type: " + keys.get( i ).getValue() );
			}
		}

		// Retain the keys
		this.keys = keys;
	}
	
	// Method for dynamically extending the schema, where data type is in a string representation
	@Setter
	public void ExtendS( ArrayList<Pair<String,String>>  keys ) 
		throws SchemaException
	{
		throw new SchemaException( "SchemaTable.ExtendS: unsupported" );
	}

	// Method for dynamically extending the schema, where data type is in an integer representation
	@Setter
	public void ExtendI( ArrayList<Pair<String,Integer>> keys ) 
		throws SchemaException
	{
		throw new SchemaException( "SchemaTable.ExtendI: unsupported" );	
	}
	
	// Method to check if specified key is in schema
	public boolean IsDefined( String key ) {
		if ( null == keys ) 
			return false;
		
		for ( Pair<String,Integer> pair : keys )
			if ( pair.getKey().equals( key ) )
				return true;
		return false;
	}
	
	// Method to check if specified type is valid for key in schema
	public boolean IsValid( String key, Integer type ) {
		if ( null == keys ) 
			return false;
		
		for ( Pair<String,Integer> pair : keys )
			if ( pair.getKey().equals( key ) && pair.getValue() == type )
				return true;
		return false;
	}
	
	// Method to find column position in table of key (ordinal ordering: starts at 1)
	public Integer ColumnPos( String key ) {
		if ( null == keys ) 
			return 0;
		
		int len = keys.size();
		for ( int i = 0; i < len; i++ ) {
			if ( keys.get( i ).getKey().equals( key ) )
				return i + 1;
		}
		
		return 0;
	}
}