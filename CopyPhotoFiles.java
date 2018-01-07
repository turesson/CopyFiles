import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CopyPhotoFiles {
	
	private Map<String, List<File>> fileMap = new HashMap<String, List<File>>();
	private File inputDirectory = new File("/home/magnus/Dropbox/Camera Uploads");
	private File outputDirectory = new File("/home/magnus/tmp");
	
	public CopyPhotoFiles() {
		extractFileMap();
		
		try {
			createAndCopyFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createAndCopyFiles() throws IOException {
		for (Entry<String, List<File>> entry : fileMap.entrySet()) {
			String date = entry.getKey();
			String[] split = date.split("-");
			
			System.out.println(date);
			File outPutDirectory;
			try {
                            File year = new File(outputDirectory, split[0] );
                            outPutDirectory = new File(year, split[1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            outPutDirectory = new File(outputDirectory, split[0] );
                         }
			
			//System.out.println(outPutDirectory);
			outPutDirectory.mkdirs();
			for (File file : entry.getValue()) {
				
				copyFile(file, new File(outPutDirectory, file.getName()));
			}
			
		}
		
	}

	public static void copyFile(File in, File out) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = new	FileInputStream(in).getChannel();
		 	outChannel = new FileOutputStream(out).getChannel();
			inChannel.transferTo(0, inChannel.size(),
					outChannel);
		}
		finally {
			if (inChannel != null) inChannel.close();
			if (outChannel != null) outChannel.close();
		}
	}

	private void extractFileMap() {
		for (File file : inputDirectory.listFiles()) {
			if (file.getName().equals(".dropbox")) {
				continue;
			}
			
			String[] split = file.getName().split(" ");
			String dirName = split[0];
			//Handle 20130427_125118
			String[] s = dirName.split("-");
			if (s.length == 3) {
                            try {
                                dirName = s[0] + "-" + s[1] + "-" + s[2];
                            } catch (StringIndexOutOfBoundsException e) {
                                dirName = "other";
                            }
			} else {
			    dirName = "other";
                        }
		
			if (!fileMap.containsKey(dirName)) {
				fileMap.put(dirName, new ArrayList<File>());
			}
			fileMap.get(dirName).add(file);
		}
		System.out.println(inputDirectory.listFiles().length + " " + fileMap.size());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CopyPhotoFiles();
	}

}
