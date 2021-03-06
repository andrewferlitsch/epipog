import epipog.storage.*;
import epipog.schema.*;
import epipog.index.*;
import epipog.datastore.*;

import javafx.util.Pair;
import java.util.ArrayList;

public class _Test8 {
	static int rc = 0;
	
	// Main entry method
	public static void main( String args[] ) {
		Test_Open();
		Test_Close();
		Test_Seek();
		Test_ReadWrite();
		Test_Schema();
		Test_Index();
		Test_Unicode();
		
		System.exit( rc );
	}	
	
	public static void Test_Open() {
		Title( "StorageSingleFile: constructor" );
		Storage s = new StorageSingleFile();
		Passed("");
		
		Title( "StorageSingleFile: Open no volume" );
		try {
			s.Open(); Failed("no exception");
		}
		catch ( StorageException e ) { Passed(""); }
		
		Title( "StorageSingleFile: Open no path" );
		s.Storage( "C:/tmp", null );
		try {
			s.Open(); Failed("no exception");
		}
		catch ( StorageException e ) { Passed(""); }
		
		Title( "StorageSingleFile: Open invalid volume" );
		s.Storage( "C:/tmp", null ); 
		try {
			s.Open(); Failed("no exception");
		}
		catch ( StorageException e ) { Passed(""); }
		
		Title( "StorageSingleFile: Open valid" );
		s.Storage( "C:/tmp", "foo" );
		try { 
			s.Open(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }
	}	
	
	public static void Test_Close() {
		Title( "StorageSingleFile: Close: not open" );
		Storage s = new StorageSingleFile();
		try {
			s.Close(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Close valid" );
		s.Storage( "C:/tmp", "foo" ); 
		try {
			s.Open(); 
			s.Close(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Close twice" );
		s.Storage( "C:/tmp", "foo" ); 
		try {
			s.Open(); 
			s.Close(); s.Close(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }
	}	
	
	public static void Test_Seek() {

		Title( "StorageSingleFile: Begin" );
		Storage s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		try {
			s.Open(); 
			s.Begin(); Passed("");
			if ( 0 == s.Pos() ) Passed(""); else Failed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: End" );
		s.Storage( "C:/tmp", "foo" ); 
		try {
			s.Open(); 
			s.Write( "abcd", 4 );
			s.End(); Passed("");
			if ( s.Pos() > 0 ) Passed(""); else Failed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Pos" );
		try {
			if ( s.Pos() > 0 ) Passed(""); else Failed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Move" );
		try {
			s.Move( 2 );
			if ( s.Pos()== 2 ) Passed(""); else Failed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Move invalid " );
		try {
			s.Move( -1 ); Failed( "no exception" );
		}
		catch ( StorageException e ) { Passed(""); }

		Title( "StorageSingleFile: Eof" );
		try {
			s.End();
			s.Eof();
			Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }
	}	
	
	public static void Test_ReadWrite() {

		Title( "StorageSingleFile: Read/Write Fixed String" );
		Storage s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		try {
			s.Open(); 
			s.Begin();
			s.Write( "abcd", 4 );
			if ( s.Pos() == 4 ) Passed(""); else Failed("");
			s.Begin();
			String ret = s.Read(4);
			if ( ret.equals( "abcd")) Passed(""); else Failed( ret );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Byte" );
		try {
			s.Open(); 
			s.Begin();
			byte b = 0x25;
			s.Write( b );
			if ( s.Pos() == 1 ) Passed(""); else Failed("");
			s.Begin();
			byte ret = s.ReadByte();
			if ( ret == 0x25 ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Char" );
		try {
			s.Open(); 
			s.Begin();
			Character c = 'a';
			s.Write( c );
			if ( s.Pos() == 2 ) Passed(""); else Failed("POS = " + s.Pos() );
			s.Begin();
			Character ret = s.ReadChar();
			if ( ret == 'a' ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Short" );
		try {
			s.Open(); 
			s.Begin();
			Short v = 5;
			s.Write( v );
			if ( s.Pos() == 2 ) Passed(""); else Failed("");
			s.Begin();
			Short ret = s.ReadShort();
			if ( ret == 5 ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Int" );
		try {
			s.Open(); 
			s.Begin();
			Integer v = 5;
			s.Write( v );
			if ( s.Pos() == 4 ) Passed(""); else Failed("");
			s.Begin();
			Integer ret = s.ReadInt();
			if ( ret == 5 ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Long" );
		try {
			s.Open(); 
			s.Begin();
			Long v = 5L;
			s.Write( v );
			if ( s.Pos() == 8 ) Passed(""); else Failed("");
			s.Begin();
			Long ret = s.ReadLong();
			if ( ret == 5L ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Float" );
		try {
			s.Open(); 
			s.Begin();
			Float v = 5.0F;
			s.Write( v );
			if ( s.Pos() == 4 ) Passed(""); else Failed("");
			s.Begin();
			Float ret = s.ReadFloat();
			if ( ret == 5.0 ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Double" );
		try {
			s.Open(); 
			s.Begin();
			Double v = 5.0;
			s.Write( v );
			if ( s.Pos() == 8 ) Passed(""); else Failed("");
			s.Begin();
			Double ret = s.ReadDouble();
			if ( ret == 5.0 ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Boolean" );
		try {
			s.Open(); 
			s.Begin();
			Boolean v = true;
			s.Write( v );
			if ( s.Pos() == 1 ) Passed(""); else Failed("");
			s.Begin();
			Boolean ret = s.ReadBoolean();
			if ( ret == true ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }

		Title( "StorageSingleFile: Write/Read Line" );
		try {
			s.Open(); 
			s.Begin();
			s.WriteLine( "abcd" );
			if ( s.Pos() == 6 ) Passed(""); else Failed("");
			s.Begin();
			String ret = s.ReadLine();
			if ( ret.equals( "abcd" ) ) Passed(""); else Failed( String.valueOf( ret ) );
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }
	}	
	
	public static void Test_Schema() {
		Title( "StorageSingleFile: Write null Schema" );
		Storage s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		Schema sc = null;
		try {
			s.Open(); 
			s.Write( sc ); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		
		Title( "StorageSingleFile: Write empty Schema" );
		s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		sc = new SchemaTable();
		try {
			s.Open(); 
			s.Write( sc ); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		
		Title( "StorageSingleFile: Read empty Schema" );
		ArrayList<Pair<String,Integer>> keys = null;
		try {
			keys = s.ReadSchema(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		if ( keys.size() == 0 ) Passed(""); else Failed("");
		
		Title( "StorageSingleFile: Write non-empty Schema" );
		keys.add( new Pair<String,Integer>( "field1", Schema.BSONString16 ) );
		keys.add( new Pair<String,Integer>( "field2", Schema.BSONString32 ) );
		keys.add( new Pair<String,Integer>( "field3", Schema.BSONShort ) );
		try {
			sc.SetI( keys );
			s.Write( sc ); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }
		catch ( SchemaException e ) { Failed( e.getMessage() ); }	
		
		Title( "StorageSingleFile: Read non-empty Schema" );
		try {
			keys = s.ReadSchema(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		if ( keys.size() == 3 ) Passed(""); else Failed( String.valueOf( keys.size() ) );
		if ( keys.get(0).getKey().equals( "field1")) Passed(""); else Failed(keys.get(0).getKey());
		if ( keys.get(1).getKey().equals( "field2")) Passed(""); else Failed(keys.get(1).getKey());
		if ( keys.get(2).getKey().equals( "field3")) Passed(""); else Failed(keys.get(2).getKey());
		if ( keys.get(0).getValue() == Schema.BSONString16 ) Passed(""); else Failed("");
		if ( keys.get(1).getValue() == Schema.BSONString32 ) Passed(""); else Failed("");
		if ( keys.get(2).getValue() == Schema.BSONShort ) Passed(""); else Failed("");
		
		Title( "StorageSingleFile: List Collections");
		ArrayList<String> names = s.List();
		if ( names.size() == 0 ) Failed( "no collections found" );
		if ( names.get(0).equals( "foo") ) Passed(""); else Failed(names.get(0));
		
		Title( "StorageSingleFile: Delete schema storage" );
		try
		{
			s.Delete(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		
		Title( "StorageSingleFile: Set DataStoreType");
		DataStore ds = new DataStoreBinary();
		s.DataStoreType( ds ); Passed("");
		
		Title( "StorageSingleFile: Get DataStoreType");
		if ( s.DataStoreType().equals( "DataStoreBinary") ) Passed(""); else Failed( s.DataStoreType() );
	
		Title( "StorageSingleFile: get DataStoreType after storage attached to datastore" );
		ds = new DataStoreJSON();
		ds.Storage( s );
		if ( s.DataStoreType().equals( "DataStoreJSON") ) Passed(""); else Failed( s.DataStoreType() );
	}
	
	public static void Test_Index()
	{
		Title( "StorageSingleFile: Write null Index" );
		Storage s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		Index ix = null;
		try {
			s.Open(); 
			s.Write( ix ); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		
		Title( "StorageSingleFile: Write empty Index" );
		s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		ix = new IndexLinear();
		ix.Name("field1");
		try {
			s.Open(); 
			s.Write( ix ); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		
		Title( "StorageSingleFile: Read empty Index" );
		ArrayList<Object> index = null;
		try {
			index = s.ReadIndex(); Passed("");
			s.Close();
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		if ( index.size() == 3 ) Passed(""); else Failed("");	
		if ( ((String)index.get(0)).equals( "field1") ) Passed(""); else Failed( ((String)index.get(0)) );	
		if ( ((Boolean)index.get(1))== false ) Passed(""); else Failed("");
		if ( ((ArrayList<long[]>)index.get(2)).size() == 0 ) Passed(""); else Failed("");
		
		Title( "StorageSingleFile: Write non-empty Index" );
		s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		ix = new IndexLinear();
		ix.Name("field1");
		ix.Unique( true );
		ix.Add( 0, 1, 2 );
		ix.Add( 3, 4, 5 );
		ix.Add( 6, 7, 8 );
		try {
			s.Open(); 
			s.Write( ix ); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }		
		
		Title( "StorageSingleFile: Read non-empty Index" );
		try {
			index = s.ReadIndex(); Passed("");
			s.Close();
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
		if ( index.size() == 3 ) Passed(""); else Failed("");	
		if ( ((String)index.get(0)).equals( "field1") ) Passed(""); else Failed( ((String)index.get(0)) );	
		if ( ((Boolean)index.get(1))== true ) Passed(""); else Failed("");
		if ( ((ArrayList<long[]>)index.get(2)).size() == 3 ) Passed(""); else Failed("");

		Title( "StorageSingleFile: Delete index storage" );
		try
		{
			s.Delete(); Passed("");
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }	
	}
	
	public static void Test_Unicode()
	{
		Title( "Test: Write Unicode String" );
		Storage s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		try {
			s.Open(); 
			s.Write( "abcdőű" );
			if ( s.Pos() == 8 ) Passed(""); else Failed("POS = " + s.Pos() );
			s.Begin();
			String str = s.Read( 8 );
			if ( str.equals("abcdőű")) Passed(""); else Failed("");
			s.Close();
			s.Delete();
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }
		
		Title( "Test: Write Unicode Fixed String" );
		s = new StorageSingleFile();
		s.Storage( "C:/tmp", "foo" ); 
		try {
			s.Open(); 
			s.Write( "abcdőű", 8 );
			if ( s.Pos() == 8 ) Passed(""); else Failed("POS = " + s.Pos() );
			s.Begin();
			String str = s.Read( 8 );
			if ( str.equals("abcdőű")) Passed(""); else Failed("");
			s.Close();
			s.Delete();
		}
		catch ( StorageException e ) { Failed( e.getMessage() ); }
	}

	public static void Title( String title ) {
		System.out.println( "Test: " + title );
	}
	
	public static void Passed( String arg ) {
		System.out.println( "PASSED " + arg );
	}
	
	public static void Failed( String arg ) {
		System.out.println( "FAILED " + arg );
		rc = 1;
	}
}