package censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder {
     <E> Iterator<E> getCSVIterator(Reader reader, Class csvClass);
     <E> List<E> getCSVFileList(Reader reader, Class csvClass);
}
