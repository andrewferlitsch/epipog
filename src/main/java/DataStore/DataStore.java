/*
 * Epipog, Copyright(c) 2016-17, Andrew Ferlitsch, CC-BY
 */
package epipog.datastore;

import epipog.annotations.*;
import epipog.storage.*;
import epipog.data.*;
import epipog.schema.*;
import epipog.index.*;
import epipog.collection.Collection;

import javafx.util.Pair;
import java.util.ArrayList;

// Abstract Layer for Accessing DataStore
//
public abstract class DataStore { 

	private 	Storage    		storage    = null;					// data storage
	protected	Collection 		collection = null;					// assigned collection
	protected	Data.DataModel  dataModel  = Data.DataModel.DATA;	// data model to use
	protected 	boolean 		validate = false;					// validate the input according to the data type
	protected 	int 			auto_incr_key = 1;					// (Internal )Auto Increment Key
	
	// Method to set collection assigned to this instance of a data store
	@Setter
	public void Collection( Collection collection ) {
		this.collection = collection;
	}
	
	// Method to set storage type for this instance of a data store
	@Setter
	public void Storage( Storage storage ) {
		this.storage = storage;
		
		// Set the DataStore type associated with the Storage
		if ( storage != null )
			storage.DataStoreType( this );
	}
	
	// Method to get storage type for this instance of the data store
	@Getter
	public Storage Storage() {
		return storage;
	}
	
	// Method to set the data model to use for validating input
	@Setter
	public void DataModel ( Data.DataModel dataModel ) {
		this.dataModel = dataModel;
	}
	
	// Method to set validating input against its data type
	@Setter
	public void Validate( boolean validate ) {
		this.validate = validate;
	}
	
	// Open the storage 
	public void Open()
		throws StorageException
	{
		if ( null != storage ) {
			// open the data store
			storage.Open();
			
			if ( null != collection ) {
				// Read in the schema
				ArrayList<Pair<String,Integer>> keys = storage.ReadSchema();

				if ( null != keys ) {
					// Allocate a (empty) schema if one does not exist
					if ( null == collection.Schema() ) {
						SchemaDynamic sc = new SchemaDynamic();
						collection.Schema( sc );
					
						// Add the read in schema to the collection
						try {
							collection.Schema().SetI( keys );
						} catch ( SchemaException e ) { throw new StorageException( e.getMessage() ); }

					}				
				}
				
				// Read in the Indexes
				ArrayList<Object> index = storage.ReadIndex();
			}
		}
	}
	
	// Close the storage 
	public void Close()
		throws StorageException
	{
		if ( null != storage ) {
			// close the data store
			storage.Close();
			
			if ( null != collection ) {
				storage.Write( collection.Schema() );
				// TODO storage.Write( collection.Index() );
			}
		}
	}
	
	// Seek to the begin of the storage 
	protected void Begin()
		throws StorageException
	{
		if ( null != storage )
			storage.Begin();
	}
	
	// Seek to the end of the storage 
	protected long End()
		throws StorageException
	{
		if ( null != storage ) {
			return storage.End();
		}
		
		return -1;
	}
	
	// Seek to a location in the storage 
	protected void Move( long pos )
		throws StorageException
	{
		if ( null != storage )
			storage.Move( pos );
	}
	
	// Get current position storage 
	protected long Pos()
		throws StorageException
	{
		if ( null != storage )
			return storage.Pos();
		return -1;
	}
	
	// Check if at the end of file
	protected boolean Eof()
		throws StorageException
	{
		if ( null != storage )
			return storage.Eof();
		return false;
	}
	
	// Write a padded string to storage
	protected void Write( String value, int length )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value, length );
	}
	
	// Write a string 
	protected void Write( String value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write a character
	protected void Write( Character value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write a byte 
	protected void Write( byte value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write a short to storage
	protected void Write( short value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write an integer to storage
	protected void Write( int value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write a long to storage
	protected void Write( long value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write a float to storage
	protected void Write( float value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write a double to storage
	protected void Write( double value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Write a boolean to storage
	protected void Write( boolean value )
		throws StorageException
	{
		if ( null != storage )
			storage.Write( value );
	}
	
	// Read string from storage
	protected String Read( int length )
		throws StorageException
	{
		if ( null != storage )
			return storage.Read( length );
		return null;
	}
	
	// Read character from storage
	protected Character ReadChar()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadChar();
		return (Character) '\uFFFF';
	}
	
	// Read byte from storage
	protected byte ReadByte()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadByte();
		return -1;
	}
	
	// Read short from storage
	protected Short ReadShort()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadShort();
		return -1;
	}
	
	// Read integer from storage
	protected Integer ReadInt()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadInt();
		return -1;
	}
	
	// Read long from storage
	protected Long ReadLong()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadLong();
		return -1L;
	}
	
	// Read float from storage
	protected Float ReadFloat()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadFloat();
		return -1.0F;
	}
	
	// Read double from storage
	protected Double ReadDouble()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadDouble();
		return -1.0;
	}
	
	// Read boolean from storage
	protected boolean ReadBoolean()
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadBoolean();
		return false;
	}
	
	// Read a line from storage
	protected String ReadLine() 
		throws StorageException
	{
		if ( null != storage )
			return storage.ReadLine();
		return null;
	}
	
	// Delete the collection from storage
	public void Delete() 
		throws StorageException
	{
		if ( null != storage )
			storage.Delete();
	}
	
	// Method to verify string representation of data according to data type
	protected String DataCheck( Data.DataModel dataModel, Integer type, String value ) 
		throws DataStoreException
	{
		try {
			if ( Data.DataModel.DATA == dataModel ) {
				Data d;
				switch ( type ) {
				case Schema.BSONString	  : d = new DataString();  		  d.Parse( value ); break;
				case Schema.BSONString16  : d = new DataStringFixed(16);  d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString32  : d = new DataStringFixed(32);  d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString64  : d = new DataStringFixed(64);  d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString128 : d = new DataStringFixed(128); d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString256 : d = new DataStringFixed(256); d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONShort 	  : d = new DataShort();   		  d.Parse( value ); break;
				case Schema.BSONInteger	  : d = new DataInteger(); 		  d.Parse( value ); break;
				case Schema.BSONLong	  : d = new DataLong();    		  d.Parse( value ); break;
				case Schema.BSONFloat	  : d = new DataFloat();   		  d.Parse( value ); break;
				case Schema.BSONDouble	  : d = new DataDouble();  		  d.Parse( value ); break;
				case Schema.BSONBoolean	  : d = new DataBoolean(); 		  d.Parse( value ); break;
				case Schema.BSONChar	  : d = new DataChar();    		  d.Parse( value ); break;
				case Schema.BSONDate	  : d = new DataDate();    		  d.Parse( value ); break;
				case Schema.BSONTime	  : d = new DataTime();    		  d.Parse( value ); break;
				}
			}
			else if ( Data.DataModel.DATASTATE == dataModel ) {
				DataState d;
				switch ( type ) {
				case Schema.BSONString	  : d = new DataStateString();  	   	d.Parse( value ); break;
				case Schema.BSONString16  : d = new DataStateStringFixed(16);  	d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString32  : d = new DataStateStringFixed(32);  	d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString64  : d = new DataStateStringFixed(64);  	d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString128 : d = new DataStateStringFixed(128); 	d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONString256 : d = new DataStateStringFixed(256); 	d.Parse( value ); value = StringNoNull( value ); break;
				case Schema.BSONShort 	  : d = new DataStateShort();     	  	d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Short: " + value );
																								  value = d.AsString();
																								  break;
				case Schema.BSONInteger	  : d = new DataStateInteger();   		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Integer: " + value );
																								  value = d.AsString();
																								  break;
				case Schema.BSONLong	  : d = new DataStateLong();      		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Long: " + value );
																								  value = d.AsString();
																								  break;
				case Schema.BSONFloat	  : d = new DataStateFloat();     		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Float: " + value );
																								  break;
				case Schema.BSONDouble	  : d = new DataStateDouble(); 	  		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Double: " + value );
																								  break;
				case Schema.BSONBoolean	  : d = new DataStateBoolean();   		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Boolean: " + value );
																								  value = d.AsString();
																								  break;
				case Schema.BSONChar	  : d = new DataStateChar();      		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Char: " + value );
																								  value = d.AsString();
																								  break;
				case Schema.BSONDate	  : d = new DataStateDate();      		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Date: " + value );
																								  break;
				case Schema.BSONTime	  : d = new DataStateTime();      		d.Parse( value ); if ( d.IsNotValid() ) 
																									throw new DataStoreException("DataStoreBinary.InsertC: invalid input for Time: " + value );
																								  break;

				}
			}
		}
		catch ( DataException e ) { throw new DataStoreException( e.getMessage() ); }
		return value;
	}
	
	// Method to return string without ending nulls
	protected String StringNoNull( String str ) {
		int len = str.length() - 1;
		
		// string has no ending nulls, return original string
		if ( str.charAt( len ) != '\0' )
			return str;
		
		for ( ; len > 0; len-- )
			if ( str.charAt( len ) != '\0' )
				break;
		
		// Return copy of string without nulls
		return str.substring( 0, len + 1 );
	}
	
	// Method for inserting into datastore with key (field) name
	// keyvals:
	//	L = Name of Key
	//	R = Value in String Representation
	public abstract void Insert( ArrayList<Pair<String,String>> keyVals ) throws DataStoreException, StorageException;
	
	// Method for inserting into datastore by predefined column order
	// values: Value in String Representation
	public abstract void InsertC( ArrayList<String> values ) throws DataStoreException, StorageException;
	
	// Method for selecting from data store
	public abstract ArrayList<Data[]> Select( ArrayList<String> fields, ArrayList<Where> where ) throws DataStoreException, StorageException;
}

