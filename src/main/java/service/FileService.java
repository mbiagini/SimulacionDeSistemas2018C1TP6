package service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public interface FileService {
	
	/**
	 * Parses the json passed as parameter and returns the object
	 * representation.
	 * 
	 * @throws RuntimeException if the parse cannot be made.
	 */
	public <T> T getFromValidatedJson(String json, Type typeOfT);
	
	/**
	 * Reads the file passed as parameter and returns its content in a string.
	 */
	public String readFileToString(File file) throws IOException;
	
	/**
	 * Writes the data to the file. If the file already had something, it is
	 * replaced with the new data.
	 * 
	 * @throws RuntimeException if there was an error in the writing.
	 */
	public void writeStringToFile(File file, String data);
	
}
