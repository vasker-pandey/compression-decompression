package wingph;


public interface GphGuiConstants {
    	String[] algorithmNamesArray = {		
							    			"GZip Compression",
							    			"Huffman Compression",
																																	
											"RLE Compressor",
											"LZW Compressor"
											};

	String[] extensionArray = {                                                    
											".gz",
											".huf",
											".rle",
											 ".lzw",
																			
											
											};
       
        final int COMP_GZIP = 0;
	final int COMP_HUFFMAN = 1;
	 final int COMP_LZW = 2;
	final int COMP_RLE = 3;
       
	final int COMPRESS = 32;
	final int DECOMPRESS = 33;
	
	
	
}
