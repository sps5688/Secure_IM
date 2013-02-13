package client;

public class Diffie{

	private static final int MAX_PRIME = 37;
	private static final int MIN_PRIME = 11;
	
	private static final int MAX_A = 10;
	
	private int g;
	private int p;
	private int a;
	
	private long key;

	public Diffie(){
		int prime_range = MAX_PRIME - MIN_PRIME;
		int a_range = MAX_A - 2;
		p = (int) ( Math.random()*prime_range + MIN_PRIME );
		while( !isPrime( p ) ){
			p = (int) ( Math.random()*prime_range + MIN_PRIME );
		}
		g = (int) ( Math.random()*(p-3) + 2 );
		while( gcd( g, p ) != 1 ){
			g = (int) ( Math.random()*(p-3) + 2 );
		}

		a = (int) ( Math.random()*a_range + 2 );
		System.out.println( "Generating p, g, and a: p " + p + " g " + g + " a " + a);
	}
	
	public Diffie( String pgv ){

		String[] pgvSplit = pgv.split( " " );
		p = Integer.parseInt( pgvSplit[1] );
		g = Integer.parseInt( pgvSplit[3] );
		a = (int) ( Math.random()*98.0 + 2 );

		getV( Long.parseLong( pgvSplit[5] ) );
		
		
		System.out.println( "Received p, g : p " + p + " g " + g );
		System.out.println( "Generating a: " + a );
		
	}

	public void getV( long v ){
		System.out.println( "Received v: " + v );
		key = HighMod( v, a, p );
	}

	public long sendU(){
		System.out.println( "Sending u: " + HighMod( g, a, p ) );
		return HighMod( g, a, p );
	}
	
	public long getKey(){
		System.out.println( "Getting key: " + key );
		return key;
	}
	
	public String sendPGU(){
		String pS = "p " + String.valueOf( p );
		String gS = " g " + String.valueOf( g );
		String uS = " u " + String.valueOf( sendU() );
		String s = new String( pS + gS + uS );
		System.out.println( "Sending PGU: " + s );
		return s;
	}

	//Static helper methods
	private static int gcd( int a, int b ){
		if( b == 0 ){
			return a;			
		}else{
			return gcd(b, a % b);
		}
	}
	
	private static boolean isPrime( int p ){

		boolean isPrime = true;
		for( int i = 2; i < Math.sqrt( p ); i++ ){
			if( p % i == 0 ){
				isPrime = false;
				break;
			}
		}
		return isPrime;
	}

	private static long HighMod( long base, long power, long modulus ){
        
        long cumul = base;
        long result = 1L;
        
        String binary = Long.toBinaryString( power );
        
        for( int i = binary.length() - 1; i >= 0; i-- ){
            if( binary.charAt( i ) == '1' ){
                result *= cumul;
                result %= modulus;
            }
            
            cumul *= cumul;
            cumul %= modulus;
        }        
        
        return result;
    }
}
