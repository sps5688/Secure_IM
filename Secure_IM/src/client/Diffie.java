package client;

/*workflow

I get IP for the destination
I run diffie hellman with dest, using IMPackets:
	a. send U
	b. receive V
	c. generate key
Get key from IMPacket or whatever
Encrypt IMPacket with AES using key
Decrypt future IMPackets with AES using key*/

public class Diffie{

	private int g;
	private int p;
	private int a;

	private long key;

	public Diffie(){
		p = (int) ( Math.random()*9000.0 + 1000.0 );
		while( !isPrime( p ) ){
			p = (int) ( Math.random()*9000.0 + 1000.0 );
		}
		g = (int) ( Math.random()*(p-2) + 2 );
		while( gcd( g, p ) != 1 ){
			g = (int) ( Math.random()*(p-2) + 2 );
		}

		a = (int) ( Math.random()*98.0 + 2 );
	}
	
	public Diffie( String pgv ){

		String[] pgvSplit = pgv.split( " " );
		p = Integer.parseInt( pgvSplit[1] );
		g = Integer.parseInt( pgvSplit[3] );
		getV( Long.parseLong( pgvSplit[5] ) );
		
		a = (int) ( Math.random()*98.0 + 2 );
		
	}

	public void getV( long v ){
		key = HighMod( v, a, p );
	}

	public long sendU(){
		return HighMod( g, a, p );
	}
	
	public int getP(){
		return p;
	}

	public int getG(){
		return g;
	}
	
	public long getKey(){
		return key;
	}
	
	public String sendPGU(){
		String pS = " p " + String.valueOf( p );
		String gS = " g " + String.valueOf( g );
		String uS = " u " + String.valueOf( sendU() );
		return new String( pS + gS + uS );
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
		for( int i = 0; i < Math.sqrt( p ); i++ ){
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
