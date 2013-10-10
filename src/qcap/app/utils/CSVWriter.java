package qcap.app.utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Serialize objects in .csv file based on object template. Use buffer writer
 * for better performance.
 *
 * @author Aale
 */
public class CSVWriter {

    BufferedWriter csvWriter = null;
    List<String> template = null;
    private String FIELD_SEPERATOR = "\t";
    private String LINE_SEPERATOR = "\n";

    public CSVWriter(String filePath, String fieldSeperator, String lineSeperator) {
        this(filePath);
        FIELD_SEPERATOR = fieldSeperator;
        LINE_SEPERATOR = lineSeperator;
    }

    public CSVWriter(String filePath, List<String> template) {
        this(filePath);
        this.template = template;
    }

    public CSVWriter(String filePath) {
        try {
            csvWriter = new BufferedWriter(new FileWriter(new File(filePath), true));
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    public void appendAll(Collection<?> objs) {
        for (Object obj : objs) {
            if (obj instanceof String) {
                append((String) obj);
            } else {
                append(obj);
            }
        }
    }

    public void append(Object obj) {
        try {
            for (String field : template) {
                String f = getField(obj, field);
                if (f != null) {
                    csvWriter.write(f);
                } else {
                    csvWriter.write("null");
                }
                csvWriter.write(FIELD_SEPERATOR);
            }
            csvWriter.write(LINE_SEPERATOR);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    public void append(String line) {
        try {
            if (line != null) {
                csvWriter.write(line);
            } else {
                csvWriter.write("null");
            }
            csvWriter.write(LINE_SEPERATOR);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    private String getField(Object obj, String field) {
        Object result = null;
        try {
            if (obj == null) {
                return null;
            }
            Method method = obj.getClass().getMethod("get" + field);
            if (method == null) {
                return null;
            }
            result = method.invoke(obj);
            if (result == null) {
                return null;
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.println(ex.getStackTrace());
        }
        return result.toString();
    }

    public void close() {
        try {
            csvWriter.close();
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    public List<String> getTemplate() {
        return template;
    }

    public void setTemplate(List<String> template) {
        this.template = template;
    }
}
