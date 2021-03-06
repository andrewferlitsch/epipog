/*
 * Epipog, Copyright(c) 2016-17, Andrew Ferlitsch, CC-BY
 */
package epipog.schema;

import epipog.annotations.*;

import javafx.util.Pair;
import java.util.ArrayList;

// Abstract Layer for Schemas
//
public interface Schema {
	
	// Method for dynamically specifying the schema, where data type defaults to string
	@Setter
	public void Set( ArrayList<String> keys ) throws SchemaException;
	
	// Method for dynamically specifying the schema, where data type is in an integer representation
	@Setter
	public void SetI( ArrayList<Pair<String,Integer>> keys ) throws SchemaException;
	
	// Method for dynamically extending the schema, where data type defaults to string
	@Setter  
	public void Extend( ArrayList<String> keys ) throws SchemaException;
	
	// Method for dynamically extending the schema, where data type is in an integer representation
	@Setter
	public void ExtendI( ArrayList<Pair<String,Integer>> keys ) throws SchemaException;
	
	// Method for updating the data types of an existing schema, where data type is represented as a string
	@Setter
	public void Type( ArrayList<String> types ) throws SchemaException;
	
	// Method to check if specified key is in schema
	public boolean IsDefined( String key );
	
	// Method to check if specified type is valid for key in schema
	public boolean IsValid( String key, Integer type );
	
	// Method to find column position in table of key (ordinal ordering: starts at 1)
	@Getter
	public Integer ColumnPos( String key );	
	
	// Method to get name for key at the specified column position in table based schema
	@Getter
	public String GetName( Integer pos );
	
	// Method to get (BSON) data type for key at the specified column position in table based schema
	@Getter
	public Integer GetType( Integer pos );
	
	// Method to get the number of columns (keys) in a table-based schema
	@Getter
	public Integer NCols();
	
	// Method to get column (key) names and order in a table-based schema
	@Getter
	public ArrayList<String> Columns();
	
	// Method to get key/type names and order in a table-based schema
	@Getter
	public ArrayList<Pair<String,Integer>> Keys();
	
	// Method to set a default string length (vs. variable string length)
	@Setter
	public void FixedString( int length ) throws SchemaException;
	
	// Definitions for BSON data types
	public enum BSONType {
		DOUBLE	 	("double", 		(byte)1),
		STRING	 	("string", 		(byte)2),
		OBJECT	 	("object", 		(byte)3),
		ARRAY 	 	("array",  		(byte)4),
		BINDATA	 	("bindata",		(byte)5),
		UNDEFINED	("undefined",	(byte)6),
		OBJECTID 	("objectid",	(byte)7),
		BOOLEAN  	("boolean",  	(byte)8),
		DATE  	 	("date",  		(byte)9),
		NULL  	 	("null",  		(byte)10),
		REGEX  	 	("regex",  		(byte)11),
		JAVASCRIPT	("javascript",  (byte)13),
		INTEGER		("integer",  	(byte)16),
		TIMESTAMP	("timestamp",   (byte)17),
		LONG		("long",  		(byte)18),
		// extended (non-bson)
		FLOAT		("float",  		(byte)51),
		DECIMAL		("decimal",  	(byte)52),
		SHORT		("short",  		(byte)53),
		TIME 		("time",  		(byte)54),
		URL			("url",  		(byte)55),
		CHAR		("char",  		(byte)56),
		STRING16	("string16",  	(byte)57),
		STRING32	("string32",  	(byte)58),
		STRING64	("string64",  	(byte)59),
		STRING128	("string128",  	(byte)60),
		STRING256	("string256",  	(byte)61);
		
		private String type;	// string representation of type
		private byte   val;		// integer representation of type
		
		// constructor
		BSONType( String type, byte val ) { this.type = type; this.val = val; }
		
		@Getter
		public int    GetVal () { return (int) val; }
		@Getter
		public String GetType() { return type; }
		
		// Find the integer value for a BSON type from its string name
		public static int Find( String type ) {
			for ( BSONType e : BSONType.values() ) {
				if ( e.GetType().equals( type ) )
					return e.GetVal();
			}
			
			return 0; // not found
		} 

		// Check if type id is valid
		public static boolean Valid( int bson ) {
			if ( ( bson >= 1 && bson < 12 ) || bson == 13 || ( bson >= 16 && bson <= 18 ) || ( bson >= 51 && bson <= 61 ) )
				return true;
			return false;
		} 
	}
	
	// Integer constant equivalents of BSON types
	public static final int BSONDouble 		= 1;
	public static final int BSONString 		= 2;
	public static final int BSONObject 		= 3;
	public static final int BSONArray  		= 4;
	public static final int BSONBinData 	= 5;
	public static final int BSONUndefined 	= 6;
	public static final int BSONObjectID	= 7;
	public static final int BSONBoolean		= 8;
	public static final int BSONDate		= 9;
	public static final int BSONNull		= 10;
	public static final int BSONRegex		= 11;
	public static final int BSONJavaScript	= 13;
	public static final int BSONInteger		= 16;
	public static final int BSONTimeStamp	= 17;
	public static final int BSONLong		= 18;
	public static final int BSONFloat		= 51;
	public static final int BSONDecimal		= 52;
	public static final int BSONShort		= 53;
	public static final int BSONTime		= 54;
	public static final int BSONUrl			= 55;
	public static final int BSONChar		= 56;
	public static final int BSONString16	= 57;
	public static final int BSONString32	= 58;
	public static final int BSONString64	= 59;
	public static final int BSONString128	= 60;
	public static final int BSONString256	= 61;
	
	// (static) Method to convert Schema in string representation to internal representation
	//	key:type,key:type,....
	public static ArrayList<Pair<String,Integer>> SchemaFromString( String schema ) 
		throws SchemaException
	{
		ArrayList<Pair<String,Integer>> keys = new ArrayList<Pair<String,Integer>>();
		
		if ( null == schema )
			throw new SchemaException( "Schema.SchemaFromString: schema is null" );
		
		// Split into key:type pairs
		String[] skeys = schema.split( ",", -1 );
		
		for ( String arg : skeys ) { 
			if ( arg.isEmpty() )
				throw new SchemaException( "Schema.SchemaFromString: empty field" );
			 
			// Split the key:type into key and type
			String[] pair = arg.split( ":", -1 );
			
			int type;
			if ( pair.length != 2 )
				type = BSONString;
			else {
			
				//throw new SchemaException( "Schema.SchemaFromString: invalid key/value pair: " + arg );
				
				// Get the BSON id for the data type
				type = BSONType.Find( pair[ 1 ] );
				if ( 0 == type )
					throw new SchemaException( "Schema.SchemaFromString: invalid type: " + pair[ 1 ] );
			}
			
			// Add the key/type pair to the internal schema representation
			//
			keys.add( new Pair<String,Integer>( pair[ 0 ], type ) );
		}
		
		return keys;
	}
}