/*
 * Epipog, Copyright(c) 2016-17, Andrew Ferlitsch, CC-BY
 */
package epipog.storage;

import epipog.annotations.*;
import epipog.schema.Schema;
import epipog.datastore.DataStore;
import epipog.index.Index;

import javafx.util.Pair;
import java.util.ArrayList;

// Implementation Layer for Data Storage as a Single File
//
public class StorageMultiFile implements Storage { 

	// Method to set location for physically storing the data store
	//	volume : storage location (e.g., directory path)
	//	dirpath: directory path relative to volume
	@Setter
	public void Storage( String volume, String dirpath ) 
	{
		this.volume  = volume;
		this.dirpath = dirpath;
	}
		
	private String volume  = null;	// storage location
	private String dirpath = null;	// directory path relative to storage location
	
	// Implementation for opening (connecting) to storage
	public void Open() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation for closing (disconnecting) from storage
	public void Close() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to seek to the begin of the storage
	public void Begin() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to seek to the end of the storage
	public long End() throws StorageException { throw new StorageException("unsupported"); }
		
	// Implementation to return the current location in storage
	public long Pos() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to move to a location in storage
	public void Move( long pos ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to check for at end of file in storage
	public boolean Eof() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a padded string to the storage
	public void Write( String value, int length ) throws StorageException { throw new StorageException("unsupported"); }
		
	// Implementation to write a string to the storage
	public void Write( String value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a byte to the storage
	public void Write( byte value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a Character to the storage
	public void Write( Character value ) throws StorageException { throw new StorageException("unsupported"); }

	// Implementation to write a short to the storage
	public void Write( Short value ) throws StorageException { throw new StorageException("unsupported"); }

	// Implementation to write an integer to the storage
	public void Write( Integer value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a long to the storage
	public void Write( Long value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a float to the storage
	public void Write( Float value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a double to the storage
	public void Write( Double value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a boolean to the storage
	public void Write( Boolean value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to write a line to storage
	public void WriteLine( String value ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a String from storage
	public String Read( int length ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a byte from storage
	public byte ReadByte() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a Character from storage
	public Character ReadChar() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a short from storage
	public Short ReadShort() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read an Integer from storage
	public Integer ReadInt() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a Long from storage
	public Long ReadLong() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a float from storage
	public Float ReadFloat() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a double from storage
	public Double ReadDouble() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a boolean from storage
	public Boolean ReadBoolean() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read a line from storage
	public String ReadLine() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Write out schema to storage
	public void Write( Schema schema ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation to Read in schema from storage
	public ArrayList<Pair<String,Integer>> ReadSchema() throws StorageException { throw new StorageException("unsupported"); }
		
	// Method to write an Index to storage
	public void Write( Index index ) throws StorageException { throw new StorageException("unsupported"); }
	
	// Method to Read an Index from storage
	public ArrayList<Object> ReadIndex() throws StorageException { throw new StorageException("unsupported"); }
	
	// Implementation for Deleting storage
	public void Delete() throws StorageException { throw new StorageException("unsupported"); }
	
	private String dataStoreType = "undefined";	// data store type associated with this storage
	
	// Method to set a data store associated with this storage instance
	@Setter
	public void DataStoreType( DataStore dataStore ) {
		dataStoreType = dataStore.getClass().getSimpleName();
	}
	
	// Method to get the data store associated with this storage instance
	@Getter
	public String DataStoreType() {
		return dataStoreType;
	}
	
	// Method to List all Collections in Storage
	public ArrayList<String> List() { return null; }
}


