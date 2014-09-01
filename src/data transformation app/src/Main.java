import core.FilesHandler;


public class Main {

	
	
	public static void main(String[] args) {
		
		FilesHandler handler = new FilesHandler();
		//handler.handleFiles("C:\\temp\\src_files");
		
		handler.sortAllFilesContents("C:\\temp\\intermediate_files");
	}

}
