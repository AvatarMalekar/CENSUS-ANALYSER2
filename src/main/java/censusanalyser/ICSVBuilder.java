package censusanalyser;

import java.io.Reader;
import java.util.Iterator;

public interface ICSVBuilder {
     <E> Iterator<E> getCSVIterator(Reader reader, Class csvClass);
}
