import java.io.*;

public class tsv2seq {
    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.err.println("Arguments: [input tsv file] [output sequence file]");
            return;
        }
        String inputFileName = args[0];
        String outputDirName = args[1];
 
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(configuration);
        Writer writer = new SequenceFile.Writer(fs, configuration, new Path(outputDirName + "/chunk-0"),
                Text.class, Text.class);
 
        int count = 0;
        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
        Text key = new Text();
        Text value = new Text();
        while(true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] tokens = line.split("\t", 3);
            if (tokens.length != 3) {
                System.out.println("Skip line: " + line);
                continue;
            }
            String category = tokens[0];
            String id = tokens[1];
            String terms = tokens[2];
            key.set("/" + category + "/" + id);
            value.set(terms);
            writer.append(key, value);
            count++;
        }
        writer.close();
        System.out.println("Wrote " + count + " entries.");
    }
}
