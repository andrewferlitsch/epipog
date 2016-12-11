/*
 * Epipog, Copyright(c) 2016-17, Andrew Ferlitsch, CC-BY
 */
import epipog.collection.*;
import epipog.storage.*;
import epipog.datastore.*;
import epipog.schema.*;

import java.util.ArrayList;
import javafx.util.Pair;
import java.io.File;

public class epipog {
	final static String usage = "Usage: epipog <options>\r\n" +
								"\t-c collection\t# collection name\r\n" +
								"\t-D datastore\t# datastore (binary,csv,psv,tsv,json)\r\n" +
								"\t-e\t\t# extend schema\r\n" +
								"\t-i insert\t# insert\r\n" +
								"\t-I file\t\t# insert from file\r\n" +
								"\t-S schema\t# schema\r\n" +
								"\t-t type\t\t# input file type\r\n" +
								"\t-T storage\t# storage (single, multi)\r\n" +
								"\t-x\t\t# delete a collection\r\n" +
								"\t-v volume\t# storage volume\r\n";
								
	// Main entry method
	public static void main( String args[] ) {
		// Check for command line argument are present
		if ( 0 == args.length ) {
			System.err.print( usage );
			System.exit( 1 );
		}
		
		String  cOption = "tmp";	// Collection Name (default collection is called tmp)
		String  DOption = "binary";	// Data Store type (default is binary)
		Boolean eOption = false;	// Extend schema
		String  iOption = null;		// Insert
		String  IOption = null;		// Insert from file
		String  SOption = null;		// Schema (specified on command line)
		String  tOption = "csv";	// Input File Type (default: csv)
		String  TOption = "single";	// Storage type (default is single file)
		String  vOption = "/tmp";	// Storage volume (default /tmp)
		Boolean xOption = false;	// Delete a collection
		
		char opt;
		while ( ( opt = GetOpt.Parse( args, "c:D:i:I:S:t:T:v:x", usage ) ) != (char)-1 ) {
			switch ( opt ) {
			case 'c': cOption = GetOpt.Arg(); break;
			case 'i': iOption = GetOpt.Arg(); break;
			case 'I': IOption = GetOpt.Arg(); break;
			case 'D': DOption = GetOpt.Arg(); break;
			case 'S': SOption = GetOpt.Arg(); break;
			case 't': tOption = GetOpt.Arg(); break;
			case 'T': TOption = GetOpt.Arg(); break;
			case 'v': vOption = GetOpt.Arg(); break;
			case 'x': xOption = true; break;
			}
		}
		
		// Instantiate a Collection object
		Collection collection = new Collection( cOption );
		
		Storage storage = null;
		switch ( TOption ) {
		case "single": storage = new StorageSingleFile(); break;
		case "multi" : storage = new StorageMultiFile (); break;
		default		 : System.err.println( "Invalid argument for -T option: " + TOption );
					   System.err.println( usage );
					   System.exit( 1 );
		}
		
		// Verify Storage Volume
		File v = new File( vOption );
		if ( !v.exists() ) {
			System.err.println( "Storage Volume does not exist: " + vOption );
			System.exit( 1 );
		}
		else if ( !v.isDirectory() ) {
			System.err.println( "Storage Volume is not a directory: " + vOption );
			System.exit( 1 );
		}
		
		// Set the location in storage of the collection
		storage.Storage( vOption, cOption );
		
		// Read the schema from Storage
		try
		{
			storage.ReadSchema();
		}
		catch ( StorageException e ) { 
			System.err.println( e.getMessage() );
			System.exit( 1 );
		}
		
		// Check if collection has an existing schema
		if ( null != collection.Schema() ) {
			if ( SOption != null ) {
				System.err.println( "Collection already has a schema" );
				System.exit( 1 );
			}
		}
		// Set the schema
		else if ( SOption != null ) {
			SchemaDynamic schema = new SchemaDynamic();
			try {
				ArrayList<Pair<String,Integer>> keys = Schema.SchemaFromString( SOption );
				schema.SetI( keys );
				collection.Schema( schema );
			}
			catch ( SchemaException e ) { 
				System.err.println( e.getMessage() );
				System.exit( 1 );
			}
		}
		
		// Get the datastore type from the schema (if any)
		String dataStoreType = storage.DataStoreType();
		if ( dataStoreType.equals( "undefined") ) {
			// not in schema, use command line setting
			switch ( DOption ) {
			case "binary": dataStoreType = "DataStoreBinary"; break;
			case "json"  : dataStoreType = "DataStoreJSON";   break;
			case "csv"   : dataStoreType = "DataStoreCSV";    break;
			case "psv"   : dataStoreType = "DataStorePSV";    break;
			case "tsv"   : dataStoreType = "DataStoreTSV";    break;
			default      : System.err.println( "Invalid argument for -D option: " + DOption );
						   System.err.println( usage);
						   System.exit( 1 );
			}
		}
		
		// Allocate an instance of the data store
		DataStore dataStore = null;
		switch ( dataStoreType ) {
		case "DataStoreBinary": dataStore = new DataStoreBinary(); 	break;
		case "DataStoreJSON"  : dataStore = new DataStoreJSON(); 	break;
		case "DataStoreCSV"	  : dataStore = new DataStoreCSV(); 	break;
		case "DataStorePSV"	  : dataStore = new DataStorePSV(); 	break;
		case "DataStoreTSV"	  : dataStore = new DataStoreTSV(); 	break;
		}
		
		// Attach the data store to the storage
		dataStore.Storage( storage );
		
		// Assign the data store to the collection
		try {
			collection.Store( dataStore );
		}
		catch ( CollectionException e ) {
			System.err.println( e.getMessage() );
			System.exit( 1 );
		}
		
		// Delete a collection
		if ( xOption ) {
			try {
				collection.DeleteCollection();
			}
			catch ( CollectionException e ) { 
				System.err.println( e.getMessage() );
				System.exit( 1 );
			}
			System.exit( 0 );
		}
		
		// Open the Data Store
		try {
			dataStore.Open();
		}
		catch ( StorageException e ) { 
			System.err.println( e.getMessage() ); 
			System.exit( 1 );
		}
		
		// Import a file
		if ( null != IOption ) {
			File f = new File( IOption );
			if ( !f.exists() ) {
				System.err.println("File does not exist: " + IOption );
				System.exit( 1 );
			}
		}
		// Insert from command line
		else
		{
			
		}
		
		// Close the Data Store
		try
		{
			dataStore.Close();
		}
		catch ( StorageException e ) { 
			System.err.println( e.getMessage() ); 
			System.exit( 1 );
		}
	}
	
	// Open the collection data store
}